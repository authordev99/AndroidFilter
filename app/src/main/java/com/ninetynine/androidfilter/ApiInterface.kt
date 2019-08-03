package com.ninetynine.androidfilter

import com.ninetynine.androidfilter.Model.Model

import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {

    companion object {
        var BASE_URL = "https://api.myjson.com/bins/"
    }

    @GET("1ekgtx")
    fun getForm(): Call<Model>

}