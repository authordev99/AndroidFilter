package com.ninetynine.androidfilter

import android.content.Intent
import androidx.databinding.ObservableArrayList
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.gson.Gson
import com.ninetynine.androidfilter.Interface.ApiInterface
import com.ninetynine.androidfilter.Interface.BinderHandler
import com.ninetynine.androidfilter.Interface.ClickHandler
import com.ninetynine.androidfilter.Model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), BinderHandler<Any> {


    var listRow: ObservableArrayList<Row> = ObservableArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var btnSave: Button
    lateinit var page: Page
    var answerMap: HashMap<String?, Any?> = HashMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getForm()
        recyclerView = findViewById(R.id.recyclerView)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            val intent = Intent(this@MainActivity, ResultActivity::class.java)
            intent.putExtra("result", Gson().toJson(answerMap))
            startActivity(intent)
        }

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
            }

            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                if (response.isSuccessful) {
                    listRow.clear()
                    println("response body = ${response.body().toString()}")

                    page = response.body()!!.form!!.Page!!
                    val mainPageSection = page.main_page!!.sections

                    val row = Row()
                    row.type = "section"
                    mainPageSection!!.forEachIndexed { index, section ->
                        listRow.addAll(section.rows!!)
                        if (mainPageSection.size - 1 != index)
                            listRow.add(row)
                    }

                    println("listRow response = $listRow")
                    recyclerView.layoutManager =
                        FlexibleFlexboxLayoutManager(this@MainActivity, FlexDirection.ROW, FlexWrap.WRAP)
                    recyclerView.adapter =
                        RecyclerViewAdapter(this@MainActivity, listRow, this@MainActivity.clickHandler())


                }
            }
        })
    }

    override fun clickHandler(): ClickHandler<Any> {
        return ClickHandler { item, position ->
            val row = item as Row
            answerMap[row.title] = row.value

            if (row.type.equals("page", true)) {
                var pages: Pages? = null
                if (row.refer.equals("more_filters_page", true)) {
                    pages = page.more_filters_page!!
                } else if (row.refer.equals("property_type_page", true)) {
                    pages = page.property_type_page!!
                }
                val intent = Intent(this@MainActivity, MoreActivity::class.java)
                intent.putExtra("ITEM", Gson().toJson(pages, Pages::class.java))
                startActivityForResult(intent, 100)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }

        if (requestCode == 100) {
            page.more_filters_page = Gson().fromJson(data!!.extras!!.getString("returnValue"), Pages::class.java)
        }
    }


}
