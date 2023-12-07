package com.example.cryptoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cryptoapp.adapters.MarketAdapter
import com.example.cryptoapp.apis.ApiServices
import com.example.cryptoapp.apis.RetroInstance
import com.example.cryptoapp.databinding.FragmentTopGainLoseBinding
import com.example.cryptoapp.modals.CryptoCurrency
import com.example.cryptoapp.modals.CryptoModal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class TopLossGainFragment : Fragment() {

private lateinit var binding: FragmentTopGainLoseBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopGainLoseBinding.inflate(layoutInflater)

        getMarketData()
        return binding.root
    }



    private fun getMarketData() {
        val res = RetroInstance.getInstance().create(ApiServices::class.java).getCryptoData()

        val position = requireArguments().getInt("position")


        //
        res.enqueue(object : Callback<CryptoModal?> {
            override fun onResponse(call: Call<CryptoModal?>, response: Response<CryptoModal?>) {
                if (response.isSuccessful){
                    binding.spinKitView.visibility = View.GONE
                    val listCrypto = response.body()!!.data.cryptoCurrencyList
                    Collections.sort(listCrypto){
                            o1 ,o2 -> (o2.quotes[0].percentChange24h.toInt()).compareTo(o1.quotes[0].percentChange24h.toInt())
                    }

                    var list = ArrayList<CryptoCurrency>()

                    if (position==0){
                        list.clear()
                        for (i in 0..9){
                            list.add(listCrypto[i])
                        }
                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )
                    }else{
                        list.clear()
                        for (i in 0..9){
                            list.add(listCrypto[listCrypto.size-1-i])
                        }
                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )
                    }
                }
            }

            override fun onFailure(call: Call<CryptoModal?>, t: Throwable) {
                Toast.makeText(activity?.applicationContext,"Failure",Toast.LENGTH_LONG).show()
                binding.spinKitView.visibility = View.GONE
                t.printStackTrace()
            }
        })
    }
}