package com.ninetynine.androidfilter.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Row : Serializable {
//    @SerializedName("info")
//
//    var info: String? = null

    var subtitle: Any? = null

    var title: String? = null

    var type: String? = null

    var value: Any? = null

    var visible_conditions: List<VisibleCondition>? = null

    var key: String? = null

    var hidden: Any? = null

    var options: List<Option>? = null
    @SerializedName("groups")

    var groups: List<Group>? = null
    @SerializedName("postfix")

    var postfix: String? = null
    @SerializedName("min")

    var min: Int = 0
    @SerializedName("formatted")

    var formatted: Boolean = false
    @SerializedName("min_title")

    var min_title: String? = null
    @SerializedName("prefix")

    var prefix: Any? = null
    @SerializedName("max")

    var max: Int = 0
    @SerializedName("max_title")

    var max_title: String? = null
    @SerializedName("listing_attribute")

    var listing_attribute: Any? = null
    @SerializedName("text_style")

    var text_style: String? = null
    @SerializedName("text_color")

    var text_color: String? = null
    @SerializedName("preview_format")

    var preview_format: List<String>? = null
    @SerializedName("refer")

    var refer: String? = null
    @SerializedName("placeholder")

    var placeholder: String? = null
    @SerializedName("multiple_line")

    var multiple_line: Boolean = false
}