package com.ashutosh.auth2.ui.auth.phone_auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ashutosh.auth2.R
import com.ashutosh.auth2.databinding.FragmentCheckPhoneNumberAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckCodeAuthFragment: Fragment() {
    private lateinit var binding: FragmentCheckPhoneNumberAuthBinding
    private lateinit var viewModel: PhoneAuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_check_phone_number_auth,container,false)
        viewModel = ViewModelProvider(this).get(PhoneAuthViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(PhoneAuthViewModel::class.java)

        binding.backButton.setOnClickListener{
            findNavController().navigate(R.id.action_checkCodeAuthFragment_to_phoneAuthFragment)
        }

    }

}
