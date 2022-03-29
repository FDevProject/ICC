package com.febrian.icc.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CovidResponse(
    @SerializedName("cases")
    val cases: Int,
    @SerializedName("recovered")
    val recovered: Int,
    @SerializedName("deaths")
    val deaths: Int,
    @SerializedName("active")
    val active: Int,
    @SerializedName("updated")
    val updated: Long,
)
