package com.ninetynine.androidfilter.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Page : Serializable {
    @SerializedName("more_filters_page")
    var more_filters_page : Pages? = null
    @SerializedName("property_type_page")
    var property_type_page : Pages? = null
    @SerializedName("main_page")
    var main_page : Pages? = null
}