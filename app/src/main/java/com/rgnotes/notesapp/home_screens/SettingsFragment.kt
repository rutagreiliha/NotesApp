package com.rgnotes.notesapp.home_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rgnotes.notesapp.R
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.status.DataStatus
import com.rgnotes.notesapp.data.viewmodel.SettingsViewModel
import com.rgnotes.notesapp.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val viewmodel: SettingsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding?.apply {

            viewLifecycleOwner.lifecycleScope.launch(mainDispatcher) {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewmodel.status.collectLatest {
                        when (it) {
                            is AuthStatus.Success<*> -> {

                                findNavController().navigate(R.id.action_settingsFragment_to_homeFragment)

                                viewmodel.clearUpdate()
                            }
                            is AuthStatus.Error -> {
                                findNavController().navigate(R.id.action_settingsFragment_to_homeFragment)

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

            signout.setOnClickListener {
                val confirmationDialog = android.app.AlertDialog.Builder(requireContext())
                confirmationDialog.setMessage("Are you sure you want to sign out?")
                    .setCancelable(true).setPositiveButton("Sign out") { _, _ ->

                        viewmodel.signOut()

                    }.setNegativeButton("Back") { dialog, _ -> dialog.dismiss() }

                confirmationDialog.create().show()
            }


            deleteaccount.setOnClickListener {
                val confirmationDialog = android.app.AlertDialog.Builder(requireContext())
                confirmationDialog.setMessage("Are you sure you want to permanently delete your account?")
                    .setCancelable(true).setPositiveButton("Delete") { _, _ ->

                        viewmodel.deleteAccount()

                    }.setNegativeButton("Back") { dialog, _ -> dialog.dismiss() }

                confirmationDialog.create().show()
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




