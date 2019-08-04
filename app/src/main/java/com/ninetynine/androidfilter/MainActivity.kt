package com.ninetynine.androidfilter

import android.content.Intent
import androidx.databinding.ObservableArrayList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.gson.Gson
import com.ninetynine.androidfilter.Model.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    var listRow: ObservableArrayList<Row> = ObservableArrayList()
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getForm()
        recyclerView = findViewById(R.id.recyclerView)


    }

    private fun getForm() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiInterface = retrofit.create(ApiInterface::class.java)
        val getForms = apiInterface.getForm()

        getForms.enqueue(object : Callback<Model> {
            override fun onFailure(call: Call<Model>, t: Throwable) {
                //can create custom error message handling to show meaningfull message to client
                Toast.makeText(this@MainActivity, "Unavailable", Toast.LENGTH_SHORT).show()
                println("t = $t")
                println("call = $call")
            }

            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                println("response = $response")
                if (response.isSuccessful) {
                    listRow.clear()
                    println("response body = ${response.body().toString()}")
                    val form: Model = response.body()!!
                    println("error = " + response.errorBody())
                    val page = form.form!!.Page!!

                    val sectionList = form.form!!.Page!!.main_page!!.sections

                    val row = Row()
                    row.type = "section"

                    sectionList!!.forEach {
                        listRow.addAll(it.rows!!)
                        listRow.add(row)
                    }

                    println("listRow response = $listRow")
                    recyclerView.layoutManager =
                        FlexibleFlexboxLayoutManager(this@MainActivity, FlexDirection.ROW, FlexWrap.WRAP)
                    recyclerView.adapter = RecyclerViewAdapter(this@MainActivity, listRow, object :
                        RecyclerViewAdapter.OnItemClickListener {
                        override fun onItemClick(item: Any) {
                            val row = item as Row
                            if (row.type.equals("page", true)) {
                                var pages: Pages? = null
                                if (row.refer.equals("more_filters_page", true)) {
                                    pages = page.more_filters_page!!
                                } else if (row.refer.equals("property_type_page", true)) {
                                    pages = page.property_type_page!!
                                }
                                val intent = Intent(this@MainActivity, MoreActivity::class.java)
                                intent.putExtra("ITEM", Gson().toJson(pages, Pages::class.java))
                                startActivity(intent)
                            }

                        }

                    })

                }
            }
        })
    }
}
