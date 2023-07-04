package com.ashutosh.auth2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ashutosh.auth2.R
import com.ashutosh.auth2.databinding.FragmentHomeBinding
import com.ashutosh.auth2.adapter.ProductAdapter
import com.ashutosh.auth2.ui.auth.MainViewModel
import com.ashutosh.auth2.ui.auth.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var mainViewMdoel: MainViewModel
    lateinit var binding : FragmentHomeBinding

    @Inject
    lateinit var mainViewMdoelFactory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //working
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        mainViewMdoel =ViewModelProvider(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = this



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewMdoel.productsLiveData.observe(viewLifecycleOwner, Observer {
            binding.listRecyclerId.apply {
                adapter = ProductAdapter(it)
                layoutManager =GridLayoutManager(context,2)
            }
        })

    }

}