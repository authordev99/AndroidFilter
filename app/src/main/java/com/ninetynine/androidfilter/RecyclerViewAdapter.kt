package com.ninetynine.androidfilter

import android.content.Context
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ninetynine.androidfilter.Model.Row
import kotlinx.android.synthetic.main.list_item_bedroom.view.*
import kotlinx.android.synthetic.main.list_item_type.view.*
import org.w3c.dom.Text

class RecyclerViewAdapter() :
    RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder<*>>() {

    lateinit var context: Context
    lateinit var listRow: ObservableArrayList<Row>
    var clickListener: OnItemClickListener? = null
    var isRent = false
    var isExpanded = false

    constructor(context: Context, listRow: ObservableArrayList<Row>, clickListener: OnItemClickListener) : this() {
        this.clickListener = clickListener
        this.context = context
        this.listRow = listRow
    }

    companion object {
        const val ITEM_SELECTION = 1
        const val ITEM_GROUP_SELECTION = 2
        const val ITEM_TEXT = 3
        const val ITEM_NUMERIC_RANGE = 4
        const val ITEM_CHECKBOX = 5
        const val ITEM_PAGE = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_SELECTION -> ViewHolderSelection(inflater.inflate(R.layout.linear_layout, null))
            ITEM_GROUP_SELECTION -> ViewHolderRentalType(inflater.inflate(R.layout.linear_layout, null))
            ITEM_NUMERIC_RANGE -> ViewHolderPriceRange(inflater.inflate(R.layout.list_item_price, null))
            ITEM_TEXT -> ViewHolderText(inflater.inflate(R.layout.list_item_edit, null))
            ITEM_PAGE -> ViewHolderPage(inflater.inflate(R.layout.list_item_page, null))
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
            ITEM_NUMERIC_RANGE -> {
                val viewHolderPriceRange = holder as ViewHolderPriceRange
                viewHolderPriceRange.bind(element, position)
            }
            ITEM_TEXT -> {
                val viewHolderText = holder as ViewHolderText
                viewHolderText.bind(element, position)
            }
            ITEM_PAGE -> {
                val viewHolderPage = holder as ViewHolderPage
                viewHolderPage.bind(element, position)
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
            listRow[position].type.equals("page") -> ITEM_PAGE
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
//
//                val tv = TextView(context)
//                val paramsText = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                tv.layoutParams = paramsText
//                tv.text = item.title
//
//                linearLayout.addView(tv)


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

    inner class ViewHolderPriceRange(itemView: View) : BaseViewHolder<Row>(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvMaxPrice = itemView.findViewById<TextView>(R.id.tvMaximumPrice)
        val tvMinPrice = itemView.findViewById<TextView>(R.id.tvMinimumPrice)
        val tvRange = itemView.findViewById<TextView>(R.id.tvRange)
        val linearLayoutTitle = itemView.findViewById<LinearLayout>(R.id.linearLayoutPrice)
        val layoutRange = itemView.findViewById<LinearLayout>(R.id.layoutRange)
        val expandIcon = itemView.findViewById<ImageView>(R.id.expandIcon)
        override fun bind(item: Row, position: Int) {
            title.text = item.title
            tvMaxPrice.text = item.max_title
            tvMinPrice.text = item.min_title

            linearLayoutTitle.setOnClickListener {
                if (isExpanded) {
                    layoutRange.visibility = View.GONE
                    expandIcon.setImageResource(R.drawable.ic_expand_more_black)
                    isExpanded = false
                } else {
                    layoutRange.visibility = View.VISIBLE
                    expandIcon.setImageResource(R.drawable.ic_expand_less_black)
                    isExpanded = true
                }

            }
        }
    }

    inner class ViewHolderText(itemView: View) : BaseViewHolder<Row>(itemView) {
        val etKeywords = itemView.findViewById<EditText>(R.id.etKeywords)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        override fun bind(item: Row, position: Int) {
            tvTitle.text = item.title
            etKeywords.hint = item.placeholder
            etKeywords.setSingleLine(!item.multiple_line)
        }
    }

    inner class ViewHolderPage(itemView: View) : BaseViewHolder<Row>(itemView) {
        val linearLayoutMore = itemView.findViewById<LinearLayout>(R.id.linearLayoutMore)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        override fun bind(item: Row, position: Int) {
            tvTitle.text = item.title
            linearLayoutMore.setOnClickListener {
                clickListener!!.onItemClick(item)
            }
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    interface OnItemClickListener {
        fun onItemClick(item: Any)
    }

}