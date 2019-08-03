package com.ninetynine.androidfilter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.ninetynine.androidfilter.Model.Pages

class MoreActivity : AppCompatActivity() {

    var pages : Pages? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)

        pages = Gson().fromJson(intent.extras.getString("ITEM"),Pages::class.java)

        supportActionBar!!.title = pages!!.title
    }
}
