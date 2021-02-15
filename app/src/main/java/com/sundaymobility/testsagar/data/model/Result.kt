package com.sundaymobility.testsagar.data.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Result {
    @SerializedName("page")
    @Expose
    var page: Int? = null

    @SerializedName("per_page")
    @Expose
    var perPage: Int? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null

    @SerializedName("data")
    @Expose
    var users: List<User>? = null

    @SerializedName("support")
    @Expose
    var support: Support? = null

    inner class Support {
        @SerializedName("url")
        @Expose
        var url: String? = null

        @SerializedName("text")
        @Expose
        var text: String? = null
    }
}