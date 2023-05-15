package com.rgnotes.notesapp.home_screens

import android.app.AlertDialog
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
import com.rgnotes.notesapp.R
import com.rgnotes.notesapp.data.Note
import com.rgnotes.notesapp.data.status.DataStatus
import com.rgnotes.notesapp.data.viewmodel.EditNoteViewModel
import com.rgnotes.notesapp.databinding.FragmentEditNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class EditNoteFragment : Fragment() {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val viewmodel: EditNoteViewModel by activityViewModels()
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding
    private var noteId: String? = null
    private var note: Note = Note()
    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        binding?.apply {

            viewLifecycleOwner.lifecycleScope.launch(mainDispatcher) {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewmodel.status.collectLatest {
                        when (it) {
                            is DataStatus.SetNote<*> -> {
                                Toast.makeText(
                                    requireContext().applicationContext,
                                    it.data as String,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                findNavController().popBackStack()
                                viewmodel.clearUpdate()
                            }
                            is DataStatus.GetNote<*> -> {
                                note = it.data as Note
                                notetitle.setText(note.title)
                                notebody.setText(note.body)


                                viewmodel.clearUpdate()
                            }
                            is DataStatus.DeleteNote<*> -> {
                                Toast.makeText(
                                    requireContext().applicationContext,
                                    it.data as String,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                findNavController().popBackStack()
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

            noteId = requireArguments().getString("noteid")

            val id = noteId
            if (id != null) {
                val toolbar: Toolbar = toolbar as Toolbar
                toolbar.inflateMenu(R.menu.edit_menu_action_bar)
                toolbar.setOnMenuItemClickListener {
                    // Handle item selection
                    when (it.itemId) {
                        R.id.delete -> {
                            val confirmationDialog = AlertDialog.Builder(requireContext())
                            confirmationDialog.setMessage("Are you sure you want to delete this note?")
                                .setCancelable(true).setPositiveButton("Delete") { _, _ ->
                                    viewmodel.deleteNote(id)

                                }.setNegativeButton("Back") { dialog, _ -> dialog.dismiss() }

                            confirmationDialog.create().show()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
                viewmodel.readNote(id)
            }

            finished.setOnClickListener {
                note.title = notetitle.text.toString()
                note.body = notebody.text.toString()
                note.dateTime = LocalDateTime.now().toString()
                if (id == null) {

                    viewmodel.createNote(note)
                } else {
                    viewmodel.updateNote(id, note)
                }
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