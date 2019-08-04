package com.ninetynine.androidfilter

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableArrayList
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.ninetynine.androidfilter.Model.Pages
import com.ninetynine.androidfilter.Model.Row


class MoreActivity : AppCompatActivity() {

    var listRow: ObservableArrayList<Row> = ObservableArrayList()
    var pages: Pages? = null
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)

        recyclerView = findViewById(R.id.recyclerView)
        pages = Gson().fromJson(intent.extras!!.getString("ITEM"), Pages::class.java)

        supportActionBar!!.title = pages!!.title

        pages!!.sections!!.forEach {
            listRow.addAll(it.rows!!)
        }

        recyclerView.layoutManager = FlexibleFlexboxLayoutManager(this,FlexDirection.ROW,FlexWrap.WRAP)

        recyclerView.adapter = RecyclerViewAdapter(this@MoreActivity, listRow, object :
            RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(item: Any) {

            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id: Int = item.itemId

        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
