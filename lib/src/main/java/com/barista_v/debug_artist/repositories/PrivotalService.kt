package com.barista_v.debug_artist.repositories

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import rx.Observable

interface PivotalService {

  @POST("/services/v5/projects/{project_id}//stories")
  fun postStory(@Path("project_id") projectId: String, @Body story: Story): Observable<Response<Any>>

}

class Story(val name: String,
            val description: String,
            @SerializedName("story_type") val type: String = "bug")