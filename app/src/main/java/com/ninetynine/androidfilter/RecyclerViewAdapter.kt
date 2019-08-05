package com.ninetynine.androidfilter

import android.content.Context
import androidx.databinding.ObservableArrayList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.flexbox.*
import com.ninetynine.androidfilter.Interface.ClickHandler
import com.ninetynine.androidfilter.Model.Page
import com.ninetynine.androidfilter.Model.Pages
import com.ninetynine.androidfilter.Model.Row
import kotlinx.android.synthetic.main.list_item_bedroom.view.*
import kotlinx.android.synthetic.main.list_item_type.view.*

class RecyclerViewAdapter() :
    androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder<*>>() {


    lateinit var context: Context
    lateinit var listRow: ObservableArrayList<Row>
    var page: Page? = null
    //    var clickListener: OnItemClickListener? = null
    var isType = "sale"
    var isExpanded = false
    var clickHandler: ClickHandler<Any>? = null

    constructor(
        context: Context,
        listRow: ObservableArrayList<Row>,
        clickHandler: ClickHandler<Any>
    ) : this() {
        this.clickHandler = clickHandler
        this.context = context
        this.listRow = listRow
    }

    companion object {
        const val ITEM_SELECTION = 1
        const val ITEM_GROUP_SELECTION = 2
        const val ITEM_TEXT = 3
        const val ITEM_NUMERIC_RANGE = 4
        const val ITEM_CHECKBOX = 5
        const val ITEM_PAGE = 6
        const val ITEM_SECTION = 7
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_SELECTION -> ViewHolderSelection(inflater.inflate(R.layout.linear_layout, null))
            ITEM_GROUP_SELECTION -> ViewHolderRentalType(inflater.inflate(R.layout.flexbox_layout, null))
            ITEM_NUMERIC_RANGE -> ViewHolderPriceRange(inflater.inflate(R.layout.list_item_price, null))
            ITEM_TEXT -> ViewHolderText(inflater.inflate(R.layout.list_item_edit, null))
            ITEM_PAGE -> ViewHolderPage(inflater.inflate(R.layout.list_item_page, null))
            ITEM_CHECKBOX -> ViewHolderCheckBox(inflater.inflate(R.layout.list_item_checkbox, null))
            ITEM_SECTION -> ViewHolderSection(inflater.inflate(R.layout.list_item_section, null))
            else -> ViewHolderRentalType(inflater.inflate(R.layout.flexbox_layout, null))
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
            ITEM_CHECKBOX -> {
                val viewHolderCheckBox = holder as ViewHolderCheckBox
                viewHolderCheckBox.bind(element, position)
            }
            ITEM_SECTION -> {
                val viewHolderSection = holder as ViewHolderSection
                viewHolderSection.bind(element, position)
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
            listRow[position].type.equals("section") -> ITEM_SECTION
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
                        isType = "rent"
                        item.value = isType
                    } else {
                        isType = "sale"
                        item.value = isType

                    }
                    //hardcore 11 itemCount (notify until 11 item)
                    //error will use notifyDataSetChange related with FlexLayoutManager
                    notifyItemRangeChanged(position, 11)
                }
                linearLayout.addView(selection)
            }


        }


    }


    inner class ViewHolderRentalType(itemView: View) : BaseViewHolder<Row>(itemView) {
        val flexboxLayout = itemView.findViewById<FlexboxLayout>(R.id.flexboxLayout)
        var itemValue: Any? = null
        var valueList: ArrayList<String?> = ArrayList()
        override fun bind(item: Row, position: Int) {
            flexboxLayout.removeAllViews()

            //hardcore get visibleCondition Class on index 0
            if (item.visible_conditions != null && (item.visible_conditions!![0].listing_type.equals(
                    isType,
                    true
                )) || item.visible_conditions == null
            ) {
                flexboxLayout.visibility = View.VISIBLE
                flexboxLayout.flexDirection = FlexDirection.ROW
                flexboxLayout.alignContent = AlignContent.FLEX_START
                flexboxLayout.alignItems = AlignItems.FLEX_START
                flexboxLayout.flexWrap = FlexWrap.WRAP


                if (item.value != null) {
                    if (item.value is String) {
                        itemValue = item.value as String
                    } else {
                        itemValue = item.value as ArrayList<*>
                    }
                }


                item.options!!.forEach { option ->
                    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val selection = inflater.inflate(R.layout.list_item_bedroom, null)
                    val params = FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    val dp = context.resources.displayMetrics.density.toInt()
                    params.setMargins(16 * dp, 16 * dp, 8 * dp, 16 * dp)
                    selection.layoutParams = params
                    selection.textType.text = option.label

                    if (item.value != null) {
                        selection.textType.isActivated =
                            if (item.value is ArrayList<*>) (itemValue as ArrayList<*>).contains(option.value) else (itemValue as String).equals(
                                option.value,
                                true
                            )
                    }


                    selection.setOnClickListener {
                        selection.textType.isActivated = !selection.textType.isActivated
                        if (!valueList.contains(option.label))
                            valueList.add(option.label)

                        item.value = valueList
                        clickHandler!!.onClick(item,position)
                    }
                    flexboxLayout.addView(selection)
                }
            } else {
                flexboxLayout.visibility = View.GONE
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
        val etMinimum = itemView.findViewById<EditText>(R.id.etMinimum)
        val etMaximum = itemView.findViewById<EditText>(R.id.etMaximum)
        override fun bind(item: Row, position: Int) {
            title.text = item.title
            tvMaxPrice.text = item.max_title
            tvMinPrice.text = item.min_title

            linearLayoutTitle.setOnClickListener {
                if (isExpanded) {
                    layoutRange.visibility = View.GONE
                    expandIcon.setImageResource(R.drawable.ic_expand_more_black)
                    tvRange.text =
                        if (etMinimum.text.toString() == "" || etMaximum.text.toString() == "") "Any - Any" else etMinimum.text.toString() + " - " + etMaximum.text.toString()
                    isExpanded = false

                    item.value = tvRange.text.toString()
                    clickHandler!!.onClick(item, position)

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
                clickHandler!!.onClick(item, position)
            }
        }
    }

    inner class ViewHolderCheckBox(itemView: View) : BaseViewHolder<Row>(itemView) {
        val switchButton = itemView.findViewById<Switch>(R.id.switchButton)
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        override fun bind(item: Row, position: Int) {
            tvTitle.text = item.title
            switchButton.isChecked = !(item.value as String).equals("false", true)
            switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
                item.value = isChecked.toString()
                clickHandler!!.onClick(item, position)
            }
        }
    }

    inner class ViewHolderSection(itemView: View) : BaseViewHolder<Row>(itemView) {
        override fun bind(item: Row, position: Int) {
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }


}