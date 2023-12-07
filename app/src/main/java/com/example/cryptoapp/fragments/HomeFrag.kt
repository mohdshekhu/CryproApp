package com.example.cryptoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.cryptoapp.R
import com.example.cryptoapp.adapters.CryptoAdapter
import com.example.cryptoapp.adapters.TopLossGainerAdapter
import com.example.cryptoapp.apis.ApiServices
import com.example.cryptoapp.apis.RetroInstance
import com.example.cryptoapp.databinding.FragmentHome2Binding
import com.example.cryptoapp.modals.CryptoModal
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFrag : Fragment() {

    private lateinit var binding: FragmentHome2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHome2Binding.inflate(layoutInflater)
        getData()
        setTabLayout()
        return binding.root
    }

    private fun setTabLayout() {

        val adapter = TopLossGainerAdapter(this)
        binding.contentViewPager.adapter = adapter

        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position==0){
                    binding.topGainIndicator.visibility = View.VISIBLE
                    binding.topLoseIndicator.visibility = View.GONE
                }else{
                    binding.topGainIndicator.visibility = View.GONE
                    binding.topLoseIndicator.visibility = View.VISIBLE
                }

            }
        })

        TabLayoutMediator(binding.tabLayout, binding.contentViewPager){
            tab , position ->
            var title = if (position==0){
                "Top Gainers"
            }else{
                "Top Losers"
            }
            tab.text=title
        }.attach()
    }

    private fun getData() {
        lifecycleScope.launch(Dispatchers.Main) {
            RetroInstance.getInstance().create(ApiServices::class.java).getCryptoData()
                .enqueue(object : Callback<CryptoModal?> {
                    override fun onResponse(
                        call: Call<CryptoModal?>,
                        response: Response<CryptoModal?>
                    ) {
                        if (response.isSuccessful) {
//                            Toast.makeText(
//                                activity!!.applicationContext,
//                                "Response successful",
//                                Toast.LENGTH_LONG
//                            ).show()
                            binding.topCurrencyRecyclerView.adapter = CryptoAdapter(requireContext(),response.body()!!.data.cryptoCurrencyList)

                        } else {
                            Toast.makeText(
                                activity!!.applicationContext,
                                "Response is not successful",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<CryptoModal?>, t: Throwable) {
                        Toast.makeText(
                            activity!!.applicationContext,
                            "Response Failure",
                            Toast.LENGTH_LONG
                        ).show()
                        t.printStackTrace()
                    }
                })

        }
    }
}