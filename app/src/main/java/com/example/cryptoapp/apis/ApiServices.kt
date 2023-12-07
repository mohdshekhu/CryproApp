package com.example.cryptoapp.apis

import com.example.cryptoapp.modals.CryptoModal
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {

    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=500")
    fun getCryptoData() : Call<CryptoModal>
}