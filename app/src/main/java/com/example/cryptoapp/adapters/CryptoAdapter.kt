package com.example.cryptoapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.TopCurrencyLayoutBinding
import com.example.cryptoapp.modals.CryptoCurrency

class CryptoAdapter(var context : Context, val list: List<CryptoCurrency>) : RecyclerView.Adapter<CryptoAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoAdapter.MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.top_currency_layout,parent,false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoAdapter.MyHolder, position: Int) {
        val item = list[position]
        holder.binding.topCurrencyNameTextView.text = item.name
        holder.binding.topCurrencyNameTextView.setTextColor(Color.MAGENTA)

        Glide.with(context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id+".png")
            .thumbnail(Glide.with(context).load(R.drawable.loading)).into(holder.binding.topCurrencyImageView)

        if (item.quotes[0].percentChange24h>0){
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.topCurrencyChangeTextView.text = "+${String.format("%.02f",item.quotes[0].percentChange24h)}%"
        }else{
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.topCurrencyChangeTextView.text = "${String.format("%.02f",item.quotes[0].percentChange24h)}%"
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyHolder(view: View) : RecyclerView.ViewHolder(view){

        val binding = TopCurrencyLayoutBinding.bind(view)

    }
}