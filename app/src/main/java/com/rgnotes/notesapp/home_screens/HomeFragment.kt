package com.rgnotes.notesapp.home_screens

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rgnotes.notesapp.R
import com.rgnotes.notesapp.data.utils.Note
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
    private val viewmodel: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    var notes: ArrayList<Note> = arrayListOf()
    private val adapter = NoteListAdapter(notes)
    private var currentOrder = "Newest first"
    var counter:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(counter == 0){
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Press again to exit!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    counter = 1
                }
                else{
                    requireActivity().finish()
                    counter = 0
                }


            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        counter=0
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.apply {

            viewLifecycleOwner.lifecycleScope.launch(mainDispatcher) {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewmodel.status.collectLatest {
                        when (it) {
                            is AuthStatus.Loading -> {
                                progress.visibility = VISIBLE
                                viewmodel.clearUpdate()
                            }
                            is DataStatus.Loading -> {
                                progress.visibility = VISIBLE
                                viewmodel.clearUpdate()
                            }
                            is DataStatus.GetNote<*> -> {
                                progress.visibility = GONE
                                notes.clear()
                                notes.addAll(it.data as ArrayList<Note>)
                                if(notes.isEmpty()){nonotes.visibility= VISIBLE}
                                if (currentOrder == "Newest first") {
                                    notes.sortByDescending { it.dateTime }
                                } else {
                                    notes.sortBy { it.dateTime }
                                }
                                adapter.notifyDataSetChanged()
                                viewmodel.clearUpdate()
                            }
                            is AuthStatus.Success<*> -> {
                                viewmodel.readAllNotes()
                                viewmodel.clearUpdate()
                            }
                            is AuthStatus.Error -> {
                                progress.visibility = GONE
                                findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
                                viewmodel.clearUpdate()
                            }
                            is DataStatus.Error -> {
                                progress.visibility = GONE
                                Toast.makeText(
                                    requireContext().applicationContext,
                                    it.message as String,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                viewmodel.clearUpdate()
                            }
                            else -> {
                                viewmodel.clearUpdate()
                            }
                        }
                    }
                }
            }
            viewmodel.isUserSignedIn()
            notesRecyclerView.adapter = adapter
            notesRecyclerView.setHasFixedSize(true)
            notesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext().applicationContext)
            val toolbar: Toolbar = toolbar as Toolbar
            toolbar.inflateMenu(R.menu.home_menu_action_bar)

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.sort -> {
                        val confirmationDialog = android.app.AlertDialog.Builder(requireContext())
                        val orders = arrayOf("Newest first", "Oldest first", "A to Z", "Z to A")
                        confirmationDialog.setTitle("Sort by")
                            .setItems(orders, DialogInterface.OnClickListener { dialog, which ->
                                when (orders[which]) {
                                    "Newest first" -> {
                                        counter=0
                                        currentOrder = "Newest first"
                                        notes.sortByDescending { it.dateTime }
                                        adapter.notifyDataSetChanged()
                                    }
                                    "Oldest first" -> {
                                        counter=0
                                        currentOrder = "Oldest first"
                                        notes.sortBy { it.dateTime }
                                        adapter.notifyDataSetChanged()
                                    }
                                    "A to Z" -> {
                                        counter=0
                                        currentOrder = "A to Z"
                                        notes.sortBy { it.title }
                                        adapter.notifyDataSetChanged()
                                    }
                                    "Z to A" -> {
                                        counter=0
                                        currentOrder = "Z to A"
                                        notes.sortByDescending { it.title }
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                            })
                        confirmationDialog.setNegativeButton("Back", null)
                        confirmationDialog.create().show()
                        true
                    }
                    R.id.manageaccount -> {
                        findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }

            adapter.setOnItemClickListener(object : NoteListAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val result = notes[position].id
                    findNavController().navigate(
                        R.id.action_homeFragment_to_editNoteFragment,
                        Bundle().apply { putString("noteid", result) })
                }
            })

            add.setOnClickListener {
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