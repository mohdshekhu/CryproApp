package com.example.cryptoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cryptoapp.adapters.MarketAdapter
import com.example.cryptoapp.apis.ApiServices
import com.example.cryptoapp.apis.RetroInstance
import com.example.cryptoapp.databinding.FragmentMarketBinding
import com.example.cryptoapp.modals.CryptoCurrency
import com.example.cryptoapp.modals.CryptoModal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MarketFrag : Fragment() {

    lateinit var list: List<CryptoCurrency>
    lateinit var adapter: MarketAdapter

    lateinit var binding : FragmentMarketBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarketBinding.inflate(layoutInflater)

        list= listOf()
        adapter = MarketAdapter(requireContext(),list,"market")
        binding.currencyRecyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {

            RetroInstance.getInstance().create(ApiServices::class.java).getCryptoData().enqueue(object : Callback<CryptoModal?> {
                override fun onResponse(
                    call: Call<CryptoModal?>,
                    response: Response<CryptoModal?>
                ) {
                    if (response.body()!=null){
                        list= response.body()!!.data.cryptoCurrencyList
                        adapter.updateData(list)
                        binding.spinKitView.visibility = View.GONE

                    }
                }

                override fun onFailure(call: Call<CryptoModal?>, t: Throwable) {
                    t.printStackTrace()
                }
            })

        }

        searchCoin()
        return binding.root
    }

    lateinit var searchText : String
    private fun searchCoin() {

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = p0.toString().toLowerCase()
                updateRC()
            }
        })
    }

    private fun updateRC() {
        val data = ArrayList<CryptoCurrency>()

        for (item in list){
            val coinName = item.name.lowercase(Locale.getDefault())
            val coinSymbol = item.symbol.lowercase(Locale.getDefault())

            if (coinName.contains(searchText) || coinSymbol.contains(searchText)){
                data.add(item)
            }
        }
        adapter.updateData(data)
    }

}