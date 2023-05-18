package com.viniciusrodriguesaro.carry.auth.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.databinding.FragmentSigninBinding
import com.viniciusrodriguesaro.carry.shoppingitem.ui.AuthViewModel

class SigninFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addAuthErrorListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupButton.setOnClickListener { _ -> findNavController().navigate(R.id.action_signinFragment_to_signupFragment) }
        binding.signinButton.setOnClickListener { _ -> handleSignin() }
        binding.signinAnonymouslyButton.setOnClickListener { _ -> authViewModel.signInAnonymously() }
        binding.emailEditText.addTextChangedListener { _ -> updateButtonEnabledAttribute() }
        binding.passwordEditText.addTextChangedListener { _ -> updateButtonEnabledAttribute() }
    }

    private fun addAuthErrorListener() {
        observeErrorCode() { mapErrorCodeToLocalizedString(it) }
    }

    private fun observeErrorCode(translateErrorCode: (errorCode: String) -> Int) {
        val errorCodeObserver = Observer<String> { code ->
            val snack = Snackbar.make(
                binding.signinLayout,
                translateErrorCode(code),
                Snackbar.LENGTH_SHORT
            )
            snack.show()
        }
        authViewModel.errorCode.observe(this, errorCodeObserver)
    }

    private fun mapErrorCodeToLocalizedString(errorCode: String?): Int {
        val message = when (errorCode) {
            "ERROR_INVALID_EMAIL" -> R.string.signin_wrong_credentials_error
            "ERROR_WRONG_PASSWORD" -> R.string.signin_wrong_credentials_error
            "ERROR_USER_NOT_FOUND" -> R.string.signin_wrong_credentials_error
            "ERROR_TOO_MANY_REQUESTS" -> R.string.auth_account_blocked_error
            "ERROR_NETWORK_REQUEST_FAILED" -> R.string.auth_network_request_failed_error
            else -> R.string.signin_generic_error
        }
        return message
    }

    fun handleSignin() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        authViewModel.signInWithEmailAndPassword(email, password)
    }

    private fun updateButtonEnabledAttribute() {
        val email = binding.emailEditText.text?.toString()
        val pass = binding.passwordEditText.text?.toString()

        binding.signinButton.isEnabled = !email.isNullOrEmpty() && !pass.isNullOrEmpty()
    }
}