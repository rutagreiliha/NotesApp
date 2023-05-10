package com.rgnotes.notesapp.home_screens

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rgnotes.notesapp.R
import com.rgnotes.notesapp.data.Note
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.DataStatus
import com.rgnotes.notesapp.data.viewmodel.HomeViewModel
import com.rgnotes.notesapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewmodel:HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    var notes: ArrayList<Note> = arrayListOf()
    private val adapter  = NoteListAdapter(notes)
    private var currentOrder = "Newest first"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.apply {

            viewLifecycleOwner.lifecycleScope.launch(mainDispatcher) {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewmodel.status.collectLatest {
                        when (it) {
                            is DataStatus.GetNote<*> -> {
                                notes.clear()
                                notes.addAll(it.data as ArrayList<Note>)
                                if (currentOrder == "Newest first"){
                                    notes.sortByDescending { it.dateTime}
                                }else{notes.sortBy { it.dateTime }}

                                adapter.notifyDataSetChanged()


                                viewmodel.clearUpdate()
                            }
                            is AuthStatus.Success<*> -> {

                                viewmodel.readAllNotes()

                                viewmodel.clearUpdate()
                            }
                            is AuthStatus.Error -> {
                                findNavController().navigate(R.id.action_homeFragment_to_signInFragment)

                                viewmodel.clearUpdate()
                            }
                            is DataStatus.Error -> {
                                Toast.makeText(
                                    requireContext().applicationContext,
                                    it.message as String,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                viewmodel.clearUpdate()
                            }
                            else -> {}
                        }
                    }
                }
            }


            viewmodel.isUserSignedIn()
            notesRecyclerView.adapter = adapter
            notesRecyclerView.setHasFixedSize(true)
            notesRecyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
            val toolbar: Toolbar = toolbar as Toolbar
            toolbar.inflateMenu(R.menu.home_menu_action_bar)

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.sort -> {
                        val confirmationDialog = android.app.AlertDialog.Builder(requireContext())
                        val orders = arrayOf("Newest first", "Oldest first")
                        confirmationDialog.setTitle("Sort by date edited").setItems(orders,DialogInterface.OnClickListener{dialog,which ->
                            when (orders[which]){
                                "Newest first" -> {
                                    currentOrder = "Newest first"
                                    notes.sortByDescending { it.dateTime }
                                    adapter.notifyDataSetChanged()}
                                "Oldest first" -> {
                                    currentOrder = "Oldest first"
                                    notes.sortBy { it.dateTime }
                                    adapter.notifyDataSetChanged()}
                            }
                        })
                        confirmationDialog.setNegativeButton("Back", null)
                        val dialog = confirmationDialog.create().show()
                        true
                    }
                    R.id.manageaccount -> {
                        findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                        true
                    }
                    else -> {false}
                }
            }



            adapter.setOnItemClickListener(object : NoteListAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val result = notes[position].id
                    findNavController().navigate(R.id.action_homeFragment_to_editNoteFragment,Bundle().apply { putString("noteid",result) })}})


            add.setOnClickListener{
                findNavController().navigate(R.id.action_homeFragment_to_editNoteFragment)
            }


        }

        return binding?.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}