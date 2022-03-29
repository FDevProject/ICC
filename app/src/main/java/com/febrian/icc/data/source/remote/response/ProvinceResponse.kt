package com.febrian.icc.data.source.remote.response

data class ProvinceResponse(
    val lastUpdate : Long? = null,
    val lat : Double? = null,
    val long : Double? = null,
    var confirmed : Int? = null,
    var recovered : Int? = null,
    var deaths : Int? = null,
    var active : Int? = null,
    val combinedKey : String? = null
)
