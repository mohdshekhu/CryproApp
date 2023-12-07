package com.example.cryptoapp.fragments

import android.os.Binder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.R
import com.example.cryptoapp.adapters.MarketAdapter
import com.example.cryptoapp.apis.ApiServices
import com.example.cryptoapp.apis.RetroInstance
import com.example.cryptoapp.database.MyDbHelper
import com.example.cryptoapp.databinding.FragmentWatchlistBinding
import com.example.cryptoapp.modals.CryptoCurrency
import com.example.cryptoapp.modals.CryptoModal
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WatchlistFrag : Fragment() {

    lateinit var adapter: MarketAdapter
    lateinit var binder: FragmentWatchlistBinding
    lateinit var listofData : ArrayList<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binder = FragmentWatchlistBinding.inflate(layoutInflater)
        listofData = ArrayList()
        makeNetworkCall()
        showList()
        return binder.root
    }

    private fun showList() {
        adapter = MarketAdapter(requireContext(),listofData,"watchlist")
        binder.watchlistRecyclerView.adapter = adapter
        binder.spinKitView.visibility = View.GONE
    }

    private fun makeNetworkCall() {
        val helper = MyDbHelper(requireContext())
        RetroInstance.getInstance().create(ApiServices::class.java).getCryptoData().enqueue(object : Callback<CryptoModal?> {
            override fun onResponse(call: Call<CryptoModal?>, response: Response<CryptoModal?>) {
                val list = response.body()!!.data.cryptoCurrencyList

                var i =0

               while (i<list.size){
                   if (helper.isAvail(list[i].id)){
                       listofData.add(list[i])
                   }
                   i += 1
               }

                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CryptoModal?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}