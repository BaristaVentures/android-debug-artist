package com.barista_v.debug_artist.report_bug.pivotal

import com.google.gson.annotations.SerializedName

class Story(val id: String,
            val name: String,
            val description: String)

class StoryRequestBody(val name: String,
                       val description: String) {

  @SerializedName("story_type") val type = "bug"

}

class Comment(val text: String,
              val fileAttachments: Array<Attachment>)

class Attachment(val id: String, val filename: String)