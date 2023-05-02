package com.viniciusrodriguesaro.carry.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.databinding.FragmentSignupBinding


class SignupFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeErrorMessage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signinButton.setOnClickListener { _ -> handleSignin() }
        binding.signupButton.setOnClickListener { _ -> findNavController().navigate(R.id.action_signupFragment_to_signinFragment) }
    }

    private fun observeErrorMessage() {
        val errorMessageObserver = Observer<String> { newMessage ->
            val snack = Snackbar.make(
                binding.signupLayout,
                newMessage,
                Snackbar.LENGTH_SHORT
            )
            snack.show()
        }
        authViewModel.errorMessage.observe(this, errorMessageObserver)
    }

    private fun handleSignin() {
        val email = binding.emailEditText.text.toString()
        val pass = binding.passwordEditText.text.toString()
        authViewModel.signUpWithEmailAndPassword(email, pass)
    }
}