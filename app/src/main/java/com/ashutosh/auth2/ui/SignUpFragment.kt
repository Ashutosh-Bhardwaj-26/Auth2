package com.ashutosh.auth2.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ashutosh.auth2.R
import com.ashutosh.auth2.data.Resource
import com.ashutosh.auth2.databinding.FragmentSignUpBinding
import com.ashutosh.auth2.ui.auth.AuthViewModel
import com.ashutosh.auth2.ui.auth.GoogleSignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    private lateinit var authviewModel: AuthViewModel

    private val viewModel: GoogleSignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        authviewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        binding.authUser = authviewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //AuthViewModel
        authviewModel.isSignedIn.observe(viewLifecycleOwner, Observer { isSignedIn ->
            if(isSignedIn){
                Log.d("signup","isSignupin=true")
               findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
            }
        })


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("611967289199-4cp7aq4fi55s367u0ii8bv96kduv4q7e.apps.googleusercontent.com")
            .requestEmail()
            .build()
        Log.d("first","reach")
        // Build a GoogleSignInClient with the options specified by gso.
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        val signInButton = binding.googleBtn

        signInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        viewModel.isSignedIn.observe(viewLifecycleOwner, Observer { isSignedIn ->
            if (isSignedIn) {
                // User is signed in, navigate to the home screen
                Log.d("signup","isSignup=true")
                findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
            }
        })

        viewModel.signInResult.observe(viewLifecycleOwner, Observer { signInResult ->
            when (signInResult) {
                is Resource.Success -> {
                    // Signed in successfully
//                    val account = signInResult.account
                    Toast.makeText(activity,"SignInSuccess",Toast.LENGTH_SHORT).show()
                    // TODO: Use the account to authenticate with your backend server
                }
                is Resource.Failure -> {
                    // Sign in failed
//                    val exception = signInResult.exception
                    Toast.makeText(activity,"Exception",Toast.LENGTH_SHORT).show()
                    Toast.makeText(activity,signInResult.toString(),Toast.LENGTH_SHORT).show()
                    Log.d("2. ", signInResult.toString())
                    // TODO: Handle the exception
                }
                else -> {
                    Toast.makeText(activity,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            viewModel.signInWithGoogle(requestCode, data)
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}