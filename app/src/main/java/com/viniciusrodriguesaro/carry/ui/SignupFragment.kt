package com.viniciusrodriguesaro.carry.ui

import android.os.Bundle
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

        addAuthErrorListener()
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
        binding.signinAnonymouslyButton.setOnClickListener { _ -> authViewModel.signInAnonymously() }
    }


    private fun addAuthErrorListener() {
        observeErrorCode() { mapErrorCodeToLocalizedString(it) }
    }

    private fun observeErrorCode(translateErrorCode: (errorCode: String) -> Int) {
        val errorCodeObserver = Observer<String> { code ->
            val snack = Snackbar.make(
                binding.signupLayout,
                translateErrorCode(code),
                Snackbar.LENGTH_SHORT
            )
            snack.show()
        }
        authViewModel.errorCode.observe(this, errorCodeObserver)
    }

    private fun mapErrorCodeToLocalizedString(errorCode: String?): Int {
        val message = when (errorCode) {
            "ERROR_WEAK_PASSWORD" -> R.string.signup_weak_password_error
            "ERROR_INVALID_EMAIL" -> R.string.signup_invalid_email_error
            "ERROR_EMAIL_ALREADY_IN_USE" -> R.string.signup_email_already_in_use_error
            "ERROR_TOO_MANY_REQUESTS" -> R.string.auth_account_blocked_error
            "ERROR_NETWORK_REQUEST_FAILED" -> R.string.auth_network_request_failed_error
            else -> R.string.signup_generic_error
        }
        return message
    }

    private fun handleSignin() {
        val email = binding.emailEditText.text.toString()
        val pass = binding.passwordEditText.text.toString()
        authViewModel.signUpWithEmailAndPassword(email, pass) {
            val snack = Snackbar.make(
                binding.signupLayout,
                R.string.signup_success_message,
                Snackbar.LENGTH_SHORT
            )

            snack.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)

                    findNavController().navigate(R.id.action_signupFragment_to_signinFragment)
                }
            })

            snack.show()
        }
    }
}