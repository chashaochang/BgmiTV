package cn.xiaobai.bgmi.core.network.retrofit

import cn.xiaobai.bgmi.bean.BgmiIndexData
import cn.xiaobai.bgmi.core.network.BGmiNetworkDataSource
import com.google.gson.Gson
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitBGmiNetworkApi {

    @GET("/api/index")
    suspend fun getIndex(): BgmiIndexData

}

const val BGmiBaseUrl = "http://192.168.31.183"

@Singleton
class RetrofitBGmiNetwork @Inject constructor(
    gson: Gson,
    okhttpCallFactory: Call.Factory
) : BGmiNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BGmiBaseUrl)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            GsonConverterFactory.create(gson)
        )
        .build()
        .create(RetrofitBGmiNetworkApi::class.java)

    override suspend fun getIndex(): BgmiIndexData = networkApi.getIndex()
}