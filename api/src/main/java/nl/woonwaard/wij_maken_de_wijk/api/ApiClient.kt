package nl.woonwaard.wij_maken_de_wijk.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.CertificatePinner
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ApiClient {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .certificatePinner(
                CertificatePinner.Builder()
                    .add(ApiDomainInfo.domain, *ApiDomainInfo.certificateHashes)
                    .build()
            )
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val retrofitApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://${ApiDomainInfo.domain}/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .build()
            .create(WmdwApiSpecification::class.java)
    }
}
