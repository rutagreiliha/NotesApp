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
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.viewmodel.ResetPasswordViewModel
import com.rgnotes.notesapp.databinding.FragmentResetPasswordBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ResetPasswordFragment : Fragment() {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val viewmodel: ResetPasswordViewModel by activityViewModels()
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        binding?.apply {

            viewLifecycleOwner.lifecycleScope.launch(mainDispatcher) {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewmodel.status.collectLatest {
                        when (it) {
                            is AuthStatus.Loading -> {
                                progress.visibility = View.VISIBLE
                                viewmodel.clearUpdate()
                            }
                            is AuthStatus.Success<*> -> {
                                progress.visibility = View.GONE
                                Toast.makeText(
                                    requireContext().applicationContext,
                                    it.data as String,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                findNavController().popBackStack()
                                viewmodel.clearUpdate()
                            }
                            is AuthStatus.Error -> {
                                progress.visibility = View.GONE
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
            resetpasswordbutton.setOnClickListener {
                val email = email.text.toString()
                viewmodel.resetPassword(email)
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