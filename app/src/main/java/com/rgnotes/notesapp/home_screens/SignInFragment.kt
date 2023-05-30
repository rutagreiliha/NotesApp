package com.rgnotes.notesapp.home_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rgnotes.notesapp.R
import com.rgnotes.notesapp.data.status.AuthStatus
import com.rgnotes.notesapp.data.viewmodel.SignInViewModel
import com.rgnotes.notesapp.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    private val viewmodel: SignInViewModel by activityViewModels()
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
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
            signinbutton.setOnClickListener {
                val email = emailinput.text.toString()
                val password = passwordinput.text.toString()
                viewmodel.signInUser(email, password)
            }
            registerButton.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_registerFragment)
            }
            forgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_resetPasswordFragment)
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