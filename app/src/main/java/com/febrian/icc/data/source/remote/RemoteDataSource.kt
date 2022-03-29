package com.febrian.icc.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.febrian.icc.data.source.remote.network.ApiConfig
import com.febrian.icc.data.source.remote.network.ApiResponse
import com.febrian.icc.data.source.remote.response.CovidResponse
import com.febrian.icc.data.source.remote.response.ProvinceResponse
import com.febrian.icc.data.source.remote.response.StatisticResponse
import com.febrian.icc.data.source.remote.response.info.DropdownInfoData
import com.febrian.icc.data.source.remote.response.info.DropdownInfoResponse
import com.febrian.icc.data.source.remote.response.info.ListInfoData
import com.febrian.icc.data.source.remote.response.info.ListInfoResponse
import com.febrian.icc.data.source.remote.response.news.NewsResponse
import com.febrian.icc.utils.DateUtils
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.FirebaseFirestore
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource().apply { instance = this }
            }
    }

    fun getCountries(
        country: String,
        isLoading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<CovidResponse>> {

        val result = MutableLiveData<ApiResponse<CovidResponse>>()
        isLoading.value = true

        ApiConfig.apiCovid.getCountries(country).enqueue(object : Callback<CovidResponse> {
            override fun onResponse(
                call: Call<CovidResponse>,
                response: Response<CovidResponse>
            ) {

                isLoading.value = false
                if (response.isSuccessful)
                    result.value = ApiResponse.Success(response.body() as CovidResponse)
            }

            override fun onFailure(call: Call<CovidResponse>, t: Throwable) {
                isLoading.value = false
                result.value = ApiResponse.Error(t.message.toString())
            }

        })

        return result
    }

    fun getStatistic(query: String): LiveData<ApiResponse<StatisticResponse>> {

        val resultData = MutableLiveData<ApiResponse<StatisticResponse>>()

        val client = AsyncHttpClient()
        val url = "https://disease.sh/v3/covid-19/historical/$query?lastdays=30"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                try {
                    val responseObject = JSONObject(result)
                    val timeline = responseObject.getJSONObject("timeline")
                    val cases: JSONObject = timeline.getJSONObject("cases")
                    val recoveredCase = timeline.getJSONObject("recovered")
                    val deathCase = timeline.getJSONObject("deaths")

                    val listCases = ArrayList<Entry>()
                    val listActive = ArrayList<Entry>()
                    val listRecovered = ArrayList<Entry>()
                    val listDeath = ArrayList<Entry>()
                    var j = 1f

                    for (i in 8 downTo 2) {
                        var getCases = 0

                        if (cases.getInt(DateUtils.getDate(-i)) != 0)
                            getCases = cases.getInt(DateUtils.getDate(-i)) - cases.getInt(
                                DateUtils.getDate(-(i + 1))
                            )

                        listCases.add(Entry(j, getCases.toFloat()))

                        var getRecovered = 0
                        if (recoveredCase.getInt(DateUtils.getDate(-i)) != 0) {
                            getRecovered =
                                recoveredCase.getInt(DateUtils.getDate(-i)) - recoveredCase.getInt(
                                    DateUtils.getDate(-(i + 1))
                                )
                        }

                        listRecovered.add(Entry(j, getRecovered.toFloat()))
                        var getDeath = 0

                        if (deathCase.getInt(DateUtils.getDate(-i)) != 0)
                            getDeath = deathCase.getInt(DateUtils.getDate(-i)) - deathCase.getInt(
                                DateUtils.getDate(-(i + 1))
                            )

                        listDeath.add(Entry(j, getDeath.toFloat()))

                        if (getCases == 0 || getRecovered == 0)
                            listActive.add(Entry(j, 0f))
                        else {
                            val getActive = getCases - (getRecovered - getDeath)
                            listActive.add(Entry(j, getActive.toFloat()))
                        }
                        j++
                    }

                    resultData.value = ApiResponse.Success(
                        StatisticResponse(
                            listCases,
                            listActive,
                            listRecovered,
                            listDeath
                        )
                    )
                } catch (e: java.lang.Exception) {
                    resultData.value = ApiResponse.Error(e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                resultData.value = ApiResponse.Error(error?.message.toString())
            }
        })

        return resultData
    }

    fun getGlobal(isLoading: MutableLiveData<Boolean>): LiveData<ApiResponse<CovidResponse>> {
        val result = MutableLiveData<ApiResponse<CovidResponse>>()
        isLoading.value = true

        ApiConfig.apiCovid.getGlobal().enqueue(object : Callback<CovidResponse> {
            override fun onResponse(call: Call<CovidResponse>, response: Response<CovidResponse>) {
                isLoading.value = false
                if (response.isSuccessful)
                    result.value = ApiResponse.Success(response.body() as CovidResponse)
            }

            override fun onFailure(call: Call<CovidResponse>, t: Throwable) {
                isLoading.value = false
                result.value = ApiResponse.Error(t.message.toString())
            }
        })

        return result
    }

    fun getProvince(isLoading: MutableLiveData<Boolean>): LiveData<ApiResponse<ArrayList<ProvinceResponse>>> {

        val result = MutableLiveData<ApiResponse<ArrayList<ProvinceResponse>>>()
        isLoading.value = true
        ApiConfig.apiCovidProvince.getProvince()
            .enqueue(object : Callback<ArrayList<ProvinceResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<ProvinceResponse>>,
                    response: Response<ArrayList<ProvinceResponse>>
                ) {
                    isLoading.value = false

                    if (response.isSuccessful) {
                        val body = response.body() as ArrayList<ProvinceResponse>
                        result.value = ApiResponse.Success(body)
                    }
                }

                override fun onFailure(call: Call<ArrayList<ProvinceResponse>>, t: Throwable) {
                    isLoading.value = false
                    result.value = ApiResponse.Error(t.message.toString())
                }

            })

        return result

    }

    fun getNews(
        query: String,
        isLoading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<NewsResponse>> {

        isLoading.value = true
        val result = MutableLiveData<ApiResponse<NewsResponse>>()

        ApiConfig.news.getNews(query, "health", "16ad27417fef4cd3a674c3b6339af476")
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    if (response.isSuccessful) result.value =
                        ApiResponse.Success(response.body() as NewsResponse)
                    isLoading.value = false

                    Log.d("TAG NEWS", response.body()?.articles.toString())
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    result.value = ApiResponse.Error(t.message.toString())
                    isLoading.value = false
                }

            })

        return result
    }

    fun getListInfo(
        title: String,
        loading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<ListInfoResponse>> {

        val resultListInfo = MutableLiveData<ApiResponse<ListInfoResponse>>()
        val listData = ArrayList<ListInfoData>()
        loading.value = true

        val db = FirebaseFirestore.getInstance()
        db.collection(title)
            .get()
            .addOnSuccessListener { result ->

                loading.value = false

                for (document in result) {
                    for (i in 0 until document.data.size) {
                        val key = document.data.keys.toList()
                        val data = document.data.values.toList()[i] as Map<String, String>

                        listData.add(
                            ListInfoData(
                                description = data["description"],
                                image = data["image"],
                                key = key[i]
                            )
                        )
                    }
                }

                resultListInfo.value = ApiResponse.Success(
                    ListInfoResponse(listData)
                )
            }
            .addOnFailureListener { exception ->

                loading.value = false

                resultListInfo.value = ApiResponse.Error(exception.message.toString())
            }

        return resultListInfo
    }

    fun getDropdownInfo(
        title: String,
        loading: MutableLiveData<Boolean>
    ): LiveData<ApiResponse<DropdownInfoResponse>> {
        val resultListInfo = MutableLiveData<ApiResponse<DropdownInfoResponse>>()
        val listData = ArrayList<DropdownInfoData>()

        loading.value = true

        val db = FirebaseFirestore.getInstance()
        db.collection(title)
            .get()
            .addOnSuccessListener { result ->

                loading.value = false

                for (document in result) {
                    for (i in 0 until document.data.size) {
                        val data = DropdownInfoData(
                            document.data.keys.toList()[i],
                            document.data.values.toList()[i].toString()
                        )
                        listData.add(data)
                    }

                }

                resultListInfo.value = ApiResponse.Success(
                    DropdownInfoResponse(listData)
                )
            }
            .addOnFailureListener { exception ->

                loading.value = false

                resultListInfo.value = ApiResponse.Error(exception.message.toString())
            }

        return resultListInfo
    }
}