package com.ashutosh.auth2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ashutosh.auth2.databinding.ListItem3Binding
import com.bumptech.glide.Glide
import com.storeApp.Product

class ProductAdapter(private val prodcutData : List<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ListItem3Binding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Product){
            binding.priceView.text = item.price.toString()
            binding.titleView.text = item.title.toString()
            Glide.with(binding.root)
                .load(item.image.toString())
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItemBinding = ListItem3Binding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(listItemBinding)
    }


    override fun getItemCount(): Int {
        return prodcutData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = prodcutData[position]
        holder.bind(product)
    }

}