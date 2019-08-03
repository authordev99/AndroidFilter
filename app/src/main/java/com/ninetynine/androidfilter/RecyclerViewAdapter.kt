package com.ninetynine.androidfilter

import android.content.Context
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ninetynine.androidfilter.Model.Row
import kotlinx.android.synthetic.main.list_item_bedroom.view.*
import kotlinx.android.synthetic.main.list_item_type.view.*

class RecyclerViewAdapter() :
    RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder<*>>() {

    lateinit var context: Context
    lateinit var listRow: ObservableArrayList<Row>
    var isRent = false

    constructor(context: Context, listRow: ObservableArrayList<Row>) : this() {
        this.context = context
        this.listRow = listRow
    }

    companion object {
        const val ITEM_SELECTION = 1
        const val ITEM_GROUP_SELECTION = 2
        const val ITEM_TEXT = 3
        const val ITEM_NUMERIC_RANGE = 4
        const val ITEM_CHECKBOX = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_SELECTION -> ViewHolderSelection(inflater.inflate(R.layout.linear_layout, null))
            ITEM_GROUP_SELECTION -> ViewHolderRentalType(inflater.inflate(R.layout.linear_layout, null))
            else -> ViewHolderRentalType(inflater.inflate(R.layout.linear_layout, null))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val viewType = holder.itemViewType
        val element = listRow[position]
        println("viewType = $viewType")
        when (viewType) {
            ITEM_SELECTION -> {
                val viewHolderSelection = holder as ViewHolderSelection
                viewHolderSelection.bind(element, position)
            }
            ITEM_GROUP_SELECTION -> {
                val viewHolderRentalType = holder as ViewHolderRentalType
                viewHolderRentalType.bind(element, position)
            }
        }
    }

    override fun getItemCount(): Int = listRow.size

    override fun getItemViewType(position: Int): Int {
        return when {
            listRow[position].type.equals("selection") -> ITEM_SELECTION
            listRow[position].type.equals("group_selection") -> ITEM_GROUP_SELECTION
            listRow[position].type.equals("text") -> ITEM_TEXT
            listRow[position].type.equals("numeric_range") -> ITEM_NUMERIC_RANGE
            listRow[position].type.equals("checkbox") -> ITEM_CHECKBOX
            else -> -1
        }

    }


    inner class ViewHolderSelection(itemView: View) : BaseViewHolder<Row>(itemView) {
        private val linearLayout = itemView.findViewById<LinearLayout>(R.id.linearLayout)
        override fun bind(item: Row, position: Int) {
            linearLayout.removeAllViews()
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            println("value = ${item.value}")
            item.options!!.forEach {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val selection = inflater.inflate(R.layout.list_item_type, null)
                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                params.setMargins(10, 0, 10, 20)
                selection.layoutParams = params
                println("it.label = " + it.label)
                selection.textSelection.text = it.label

                selection.iconDone.visibility = if (item.value as String == it.value) View.VISIBLE else View.GONE

                selection.textSelection.setOnClickListener {
                    if (selection.textSelection.text == "For rent") {
                        item.value = "rent"
                        isRent = true
                    } else {
                        item.value = "sale"
                        isRent = false
                    }
                    notifyDataSetChanged()
//                    val viewHolderRentalType = ViewHolderRentalType
//                    viewHolderRentalType.itemView.visibility = View.VISIBLE
                }
                linearLayout.addView(selection)
            }


        }


    }


    inner class ViewHolderRentalType(itemView: View) : BaseViewHolder<Row>(itemView) {
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.linearLayout)
        override fun bind(item: Row, position: Int) {
            linearLayout.removeAllViews()
            if (isRent) {
                linearLayout.visibility = View.VISIBLE
                linearLayout.orientation = LinearLayout.HORIZONTAL
                val paramsLinear = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                paramsLinear.setMargins(20, 20, 20, 20)
                linearLayout.layoutParams = paramsLinear
                item.options!!.forEach {
                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val selection = inflater.inflate(R.layout.list_item_bedroom, null)
                    val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    params.leftMargin = 16
                    params.rightMargin = 16
                    selection.layoutParams = params
                    println("it.label ViewHolderRentalType = " + it.label)
                    selection.textType.text = it.label
                    linearLayout.addView(selection)
                }
            } else {
                linearLayout.visibility = View.GONE
            }


        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

}