package com.ashutosh.auth2.ui.auth.phone_auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ashutosh.auth2.R
import com.ashutosh.auth2.databinding.FragmentPhoneAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhoneAuthFragment : Fragment() {

    private lateinit var binding: FragmentPhoneAuthBinding
    private lateinit var viewModel: PhoneAuthViewModel
    private var validPhoneNumber: String = ""
    private var verificationTimeOut: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_phone_auth,container,false)
        viewModel = ViewModelProvider(this).get(PhoneAuthViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.isSignedIn.observe(viewLifecycleOwner, Observer { isSignedIn ->
            if(isSignedIn){
                findNavController().navigate(R.id.action_phoneAuthFragment_to_homeFragment)
            }
        })


        viewModel = ViewModelProvider(this).get(PhoneAuthViewModel::class.java)


        binding.authByPhoneNumberFAB.setOnClickListener(){
            viewModel.onSendOtpClick(requireActivity())
            findNavController().navigate(R.id.action_phoneAuthFragment_to_checkCodeAuthFragment)
        }

    }

}