package com.febrian.icc.data.source.remote.response

import com.github.mikephil.charting.data.Entry

data class StatisticResponse(
    val listCases: ArrayList<Entry>? = null,
    val listActive: ArrayList<Entry>? = null,
    val listRecovered: ArrayList<Entry>? = null,
    val listDeath: ArrayList<Entry>? = null
)
