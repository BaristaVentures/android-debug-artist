package debug_artist.reporter_pivotaltracker

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import debug_artist.menu.report_bug.Answer
import debug_artist.menu.report_bug.BugRepository
import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.Observable.zip
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    service = retrofit.create(PivotalService::class.java)
  }

  @Suppress("UNCHECKED_CAST")
  override fun create(name: String, description: String,
                      screenshotFilePath: String?, logsFilePath: String?): Observable<Answer<Any>> {
    val observables = mutableListOf<Observable<*>>(createStory(name, description, labels))

    screenshotFilePath?.let { observables.add(uploadFile("image/jpg", it)) }
    logsFilePath?.let { observables.add(uploadFile("text/plain", it)) }

    return zip(observables) { result ->
      val storyAnswer = (result.getOrNull(0) as? Answer<Story>)
      val possibleStory = storyAnswer?.body

      val possibleScreenshot = (result.getOrNull(1) as? Answer<Attachment>)?.body
      val possibleLogs = (result.getOrNull(2) as? Answer<Attachment>)?.body

      when (possibleStory) {
        null -> just(storyAnswer)
        else -> {
          val attachments = mutableListOf<Attachment>()

          possibleScreenshot?.let { attachments.add(it) }
          possibleLogs?.let { attachments.add(it) }

          if (attachments.size > 0) {
            createComment(possibleStory.id, "Attach", attachments.toTypedArray())
          } else {
            Observable.just(Answer())
          }
        }
      }
    }.switchMap { observable -> observable }
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

class PivotalTrackerHeaderInterceptor(private val apiToken: String) : Interceptor {

  override fun intercept(chain: Interceptor.Chain?): Response? {
    val builder = chain?.request()?.newBuilder()?.addHeader("X-TrackerToken", apiToken)
    return chain?.proceed(builder?.build())
  }

}