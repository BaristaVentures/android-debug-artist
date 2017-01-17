package com.barista_v.debug_artist.repositories.pivotal

import com.barista_v.debug_artist.repositories.BugReportRepository
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable

class PivotalReportRepository(apiToken: String, val projectId: String) : BugReportRepository {
  private val url = "https://www.pivotaltracker.com/"
  private val service: PivotalService

  var properties = mapOf<String, String>()

  init {
    val okHttpClient = OkHttpClient.Builder().apply {
      addInterceptor(PivotalTrackerHeaderInterceptor(apiToken))
    }.build()

    val gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build()

    service = retrofit.create(PivotalService::class.java)
  }


  override fun createBug(name: String, description: String)
      : Observable<Response<Any>> {
    //TODO: manage errors
    val fullDescription = if (properties.isEmpty()) {
      description
    } else {
      "$description \n${toListOfItems((properties))}"
    }

    return service.postStory(projectId, Story(name, fullDescription))
  }

  private fun toListOfItems(properties: Map<String, String>): String {
    return StringBuilder("*Properties*:").apply {
      for ((k, v) in properties) {
        append("\n- ").append(k).append(": ").append(v)
      }
    }.toString()
  }

}

class PivotalTrackerHeaderInterceptor(val apiToken: String) : Interceptor {

  override fun intercept(chain: Interceptor.Chain?): okhttp3.Response? {
    val builder = chain?.request()?.newBuilder()?.addHeader("X-TrackerToken", apiToken)
    return chain?.proceed(builder?.build())
  }

}