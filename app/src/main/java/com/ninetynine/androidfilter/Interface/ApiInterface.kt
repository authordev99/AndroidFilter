package com.ninetynine.androidfilter.Interface

import com.ninetynine.androidfilter.Model.Model

import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {

    companion object {
        var BASE_URL = "https://api.myjson.com/bins/"
    }
    @GET("j46hh")
    fun getForm(): Call<Model>

}