package com.ninetynine.androidfilter.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Form : Serializable{
    @SerializedName("entry_page")
    var entryPage : String? = null
    @SerializedName("pages")
    var Page : Page? = null
}