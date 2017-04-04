package debug_artist.reporter_pivotaltracker

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import debug_artist.menu.report_bug.Answer
import debug_artist.menu.report_bug.BugRepository
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
                           var properties: Map<String, String> = mapOf<String, String>(),
                           val labels: Array<String>?)
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

  override fun create(name: String, description: String,
                      screenshotFilePath: String?, logsFilePath: String?): Observable<Answer<Any>> {
    val uploadFileObservable = screenshotFilePath?.let { uploadFile("image/jpg", it) } ?: just(null)
    val uploadLogsObservable = logsFilePath?.let { uploadFile("text/plain", it) } ?: just(null)

    return zip(createStory(name, description, labels), uploadFileObservable, uploadLogsObservable,
        { story, uploadedScreenshot, uploadedLogs ->
          val possibleStory = story?.body
          val possibleScreenshot = uploadedScreenshot?.body
          val possibleLogs = uploadedLogs?.body

          if (possibleStory == null) {
            just(story)
          } else if (possibleScreenshot == null) {
            just(uploadedLogs)
          } else if (possibleLogs == null) {
            just(uploadedScreenshot)
          } else {
            val attachments = arrayOf(possibleScreenshot, possibleLogs)
            createComment(possibleStory.id, "Attach", attachments)
          }
        }).switchMap { observable -> observable }
  }

  private fun createStory(name: String, description: String, labels: Array<String>? = null): Observable<Answer<Story>> {
    val fullDescription = if (properties.isEmpty()) {
      description
    } else {
      "$description \n\n${toListOfItems((properties))}"
    }

    return service.postStory(projectId, StoryRequestBody(name, fullDescription, labels)).map { Answer.from(it) }
  }

  private fun uploadFile(contentType: String, filePath: String): Observable<Answer<Attachment>> {
    val file = File(filePath)
    val requestFile = RequestBody.create(MediaType.parse(contentType), file)
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

  override fun intercept(chain: Interceptor.Chain?): Response? {
    val builder = chain?.request()?.newBuilder()?.addHeader("X-TrackerToken", apiToken)
    return chain?.proceed(builder?.build())
  }

}