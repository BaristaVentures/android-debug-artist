package debug_artist.reporter_pivotaltracker

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface PivotalService {

  @POST("/services/v5/projects/{project_id}/stories")
  fun postStory(@Path("project_id") projectId: String,
                @Body story: StoryRequestBody): Observable<Response<Story>>

  @POST("/services/v5/projects/{project_id}/stories/{story_id}/comments ")
  fun postComment(@Path("project_id") projectId: String,
                  @Path("story_id") storyId: String,
                  @Body comment: Comment): Observable<Response<Any>>

  @Multipart
  @POST("/services/v5/projects/{project_id}/uploads")
  fun upload(@Path("project_id") projectId: String,
             @Part file: MultipartBody.Part): Observable<Response<Attachment>>

}