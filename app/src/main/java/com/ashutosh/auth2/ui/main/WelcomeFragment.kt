package com.ashutosh.auth2.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ashutosh.auth2.R
import com.ashutosh.auth2.databinding.FragmentWelcomeBinding
import com.ashutosh.auth2.ui.auth.phone_auth.PhoneAuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@AndroidEntryPoint
class WelcomeFragment : Fragment() {


    private lateinit var binding: FragmentWelcomeBinding

    private val authViewModel: PhoneAuthViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).apply {
            launch(Dispatchers.Main){
                println("?@@@@ ${Thread.currentThread().name}")
            }
            launch {
                println("?@@@@2 ${Thread.currentThread().name}")
                withContext(Dispatchers.Main){
                    println("@@@@@ HELLO WITH CONTEXT")
                    delay(5000)
                }
                println("@@@@@ HELLO 1 ${Thread.currentThread().name}")
                launch(Dispatchers.Main.immediate) {
                    println("@@@@@ HELLO LAUNCH")
                    delay(5000)
                }
                println("@@@@@ HELLO 2 ${Thread.currentThread().name}")

            }
        }
        val isLoggedIn = authViewModel.checkIfUserLoggedIn()
        if(isLoggedIn){
            findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
        }else{
            findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_welcome, container, false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    fun openSignupFragment() {
        findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)
    }
}
