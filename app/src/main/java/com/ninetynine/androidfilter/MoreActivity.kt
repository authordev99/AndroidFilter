package com.ninetynine.androidfilter

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableArrayList
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.gson.Gson
import com.ninetynine.androidfilter.Interface.BinderHandler
import com.ninetynine.androidfilter.Interface.ClickHandler
import com.ninetynine.androidfilter.Model.Pages
import com.ninetynine.androidfilter.Model.Row


class MoreActivity : AppCompatActivity(), BinderHandler<Any> {


    var listRow: ObservableArrayList<Row> = ObservableArrayList()
    var pages: Pages? = null
    var row = Row()
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    var value: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)

        recyclerView = findViewById(R.id.recyclerView)
        pages = Gson().fromJson(intent.extras!!.getString("ITEM"), Pages::class.java)

        supportActionBar!!.title = pages!!.title

        pages!!.sections!!.forEach {
            listRow.addAll(it.rows!!)
        }

        recyclerView.layoutManager = FlexibleFlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP)
        recyclerView.adapter = RecyclerViewAdapter(this@MoreActivity, listRow, this@MoreActivity.clickHandler()!!)
    }

    override fun onBackPressed() {

        if (pages!!.key.equals("more_filters_page", true)) {
            val returnIntent = Intent()
            returnIntent.putExtra("returnValue", Gson().toJson(pages))
            setResult(RESULT_OK, returnIntent)
        }

        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id: Int = item.itemId

        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun clickHandler(): ClickHandler<Any>? {
        return ClickHandler { item, position ->
            row = item as Row
            listRow.clear()
            listRow.add(row)
            pages!!.sections!!.forEach {
                it.rows = listRow
            }
        }
    }


}
