package com.rgnotes.notesapp.home_screens

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rgnotes.notesapp.R
import com.rgnotes.notesapp.data.NoteTitleId
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
    var notes: ArrayList<NoteTitleId> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.apply {

            val toolbar: Toolbar = toolbar as Toolbar
           toolbar.inflateMenu(R.menu.home_menu_action_bar)

            toolbar.setOnMenuItemClickListener {
                // Handle item selection
                when (it.itemId) {
                    R.id.sort -> {
                        //add sort
                        true
                    }
                    R.id.signout -> {
                        //sign out
                        true
                    }
                    R.id.deleteaccount -> {
                        //delete
                        true
                    }
                    else -> {false}
                }
            }

            viewmodel.isUserSignedIn()
            val adapter  = NoteListAdapter(notes)
            notesRecyclerView.adapter = adapter
            notesRecyclerView.setHasFixedSize(true)
            notesRecyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)

            viewLifecycleOwner.lifecycleScope.launch(mainDispatcher) {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewmodel.status.collectLatest {
                        when (it) {
                            is DataStatus.GetNote<*> -> {
                                notes.clear()
                                notes.addAll(it.data as ArrayList<NoteTitleId>)
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