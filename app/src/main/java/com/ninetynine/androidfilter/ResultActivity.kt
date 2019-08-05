package com.ninetynine.androidfilter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.list_item_bedroom.view.*
import kotlinx.android.synthetic.main.list_item_text.view.*
import java.util.ArrayList

class ResultActivity : AppCompatActivity() {

    var answerMap: HashMap<String?, Any?> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        answerMap =
            Gson().fromJson(intent.extras.getString("result"), object : TypeToken<HashMap<String?, Any?>>() {}.type)
        println("answerMap result = $answerMap")

        answerMap.forEach {
            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val textLayout = inflater.inflate(R.layout.list_item_text, null)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val dp = resources.displayMetrics.density.toInt()
            params.setMargins(16 * dp, 16 * dp, 8 * dp, 16 * dp)
            textLayout.layoutParams = params
            var value = ""
            if (it.value is ArrayList<*>) {
                (it.value as ArrayList<String>).forEach {
                    value += "$it,"
                }
                textLayout.textResult.text = it.key + " = " + value
            } else {
                textLayout.textResult.text = it.key + " = " + it.value
            }

            linearLayoutResult.addView(textLayout)
        }


    }

}
