package com.ashutosh.auth2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.ashutosh.auth2.MainActivity
import com.ashutosh.auth2.R
import com.ashutosh.auth2.databinding.FragmentPhoneAuthBinding
import com.ashutosh.auth2.ui.auth.PhoneAuthViewModel
import com.google.firebase.auth.PhoneAuthOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class PhoneAuthFragment : Fragment() {

    private lateinit var binding: FragmentPhoneAuthBinding
    private lateinit var viewModel: PhoneAuthViewModel

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
        binding.sendId.setOnClickListener(){
               viewModel.onSendOtpClick(requireActivity())
        }

        binding.enterId.setOnClickListener(){
            viewModel.onOptEnter()
        }
    }

}