package com.barista_v.debug_artist.repositories.pivotal

import com.google.gson.annotations.SerializedName

class Story(val name: String,
            val description: String) {

  @SerializedName("story_type") val type = "bug"

}
