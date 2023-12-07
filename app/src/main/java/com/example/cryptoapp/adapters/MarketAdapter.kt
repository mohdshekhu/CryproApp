package com.example.cryptoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.CurrencyItemLayoutBinding
import com.example.cryptoapp.fragments.HomeFragDirections
import com.example.cryptoapp.fragments.MarketFragDirections
import com.example.cryptoapp.fragments.WatchlistFrag
import com.example.cryptoapp.fragments.WatchlistFragDirections
import com.example.cryptoapp.modals.CryptoCurrency

class MarketAdapter(val context: Context, var list: List<CryptoCurrency>, var s: String) : RecyclerView.Adapter<MarketAdapter.MyHolderView>() {

    inner class MyHolderView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = CurrencyItemLayoutBinding.bind(itemView)
    }

    fun updateData(dataset : List<CryptoCurrency>){
        list = dataset
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        return MyHolderView(LayoutInflater.from(context).inflate(R.layout.currency_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) {
        var item = list[position]
        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol
        holder.binding.currencyPriceTextView.text = "$${String.format("%.02f",item.quotes[0].price)}%"

        Glide.with(context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id+".png")
            .thumbnail(Glide.with(context).load(R.drawable.loading)).into(holder.binding.currencyImageView)


        Glide.with(context).load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/"+item.id+".png")
            .thumbnail(Glide.with(context).load(R.drawable.loading)).into(holder.binding.currencyChartImageView)


        if (item.quotes[0].percentChange24h>0){
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text = "+${String.format("%.02f",item.quotes[0].percentChange24h)}%"
        }else{
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text = "${String.format("%.02f",item.quotes[0].percentChange24h)}%"
        }

        holder.itemView.setOnClickListener(){
            if (s=="home") {
                findNavController(it).navigate(
                    HomeFragDirections.actionHomeFragToDetailsFragment(item)
                )
            }else if(s=="market"){
                findNavController(it).navigate(
                    MarketFragDirections.actionDashboardFragToDetailsFragment(item)
                )
            }else if (s=="watchlist"){
                findNavController(it).navigate(
                    WatchlistFragDirections.actionLeaderBoardFragToDetailsFragment(item)
                )
            }
        }

    }
}