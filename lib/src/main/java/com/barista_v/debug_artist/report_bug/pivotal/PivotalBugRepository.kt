package com.barista_v.debug_artist.report_bug.pivotal

import com.barista_v.debug_artist.repositories.Answer
import com.barista_v.debug_artist.repositories.BugRepository
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.Observable.just
import rx.Observable.zip
import java.io.File

class PivotalBugRepository(apiToken: String,
                           val projectId: String,
                           var properties: Map<String, String> = mapOf<String, String>())
  : BugRepository {

  private val url = "https://www.pivotaltracker.com/"
  private val service: PivotalService

  init {
    val logger = HttpLoggingInterceptor(SimpleHttpLogger("PivotalTrackerRepo")).apply {
      level = HttpLoggingInterceptor.Level.BODY
    }
    val okHttpClient = OkHttpClient.Builder().apply {
      addInterceptor(logger)
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

  override fun createBug(name: String, description: String, screenshotFilePath: String?, logsFilePath: String?)
      : Observable<Answer<Any>> {

    val uploadFileObservable = screenshotFilePath?.let { uploadFile(it) } ?: just(null)
    val uploadLogsObservable = logsFilePath?.let { uploadFile(it) } ?: just(null)

    return zip(createStory(name, description), uploadFileObservable, uploadLogsObservable,
        { story, uploadedScreenshot, uploadedLogs ->
          if (story.body == null) {
            just(story)
          } else if (uploadedScreenshot?.body == null) {
            just(uploadedLogs)
          } else if (uploadedLogs?.body == null) {
            just(uploadedScreenshot)
          } else {
            val attachments = arrayOf(uploadedScreenshot?.body, uploadedLogs?.body)
            createComment(story.body.id, "Attach", attachments)
          }
        }).switchMap { observable -> observable }
  }

  private fun createStory(name: String, description: String): Observable<Answer<Story>> {
    val fullDescription = if (properties.isEmpty()) {
      description
    } else {
      "$description \n\n${toListOfItems((properties))}"
    }

    return service.postStory(projectId, StoryRequestBody(name, fullDescription)).map { Answer.from(it) }
  }

  private fun uploadFile(filePath: String): Observable<Answer<Attachment>> {
    val file = File(filePath)
    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

    return service.upload(projectId, body).map { Answer.from(it) }
  }

  private fun createComment(storyId: String, text: String, attachments: Array<Attachment>): Observable<Answer<Any>> {
    return service.postComment(projectId, storyId, Comment(text, attachments))
        .map { Answer.from(it) }
  }

  private fun toListOfItems(properties: Map<String, String>): String {
    return StringBuilder("**Properties**:\n").apply {
      for ((k, v) in properties) {
        append("\n- *").append(k).append("*: ").append(v)
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