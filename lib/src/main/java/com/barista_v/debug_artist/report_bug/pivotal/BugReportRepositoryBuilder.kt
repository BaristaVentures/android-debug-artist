package com.barista_v.debug_artist.report_bug.pivotal

import android.os.Parcel
import android.os.Parcelable
import com.barista_v.debug_artist.repositories.BugReportRepository

class BugReportRepositoryBuilder(val apiKey: String,
                                 val projectId: String,
                                 val properties: Map<String, String>)
  : BugReportRepository.Builder {

  override fun build(): BugReportRepository {
    return PivotalReportRepository(apiKey, projectId, properties)
  }

  companion object {
    @JvmField val CREATOR: Parcelable.Creator<BugReportRepositoryBuilder> = object : Parcelable.Creator<BugReportRepositoryBuilder> {
      override fun createFromParcel(source: Parcel): BugReportRepositoryBuilder = BugReportRepositoryBuilder(source)
      override fun newArray(size: Int): Array<BugReportRepositoryBuilder?> = arrayOfNulls(size)
    }
  }

  constructor(source: Parcel) : this(source.readString(), source.readString(),
      mapOf<String, String>().apply { source.readMap(this, Map::class.java.classLoader) })

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel?, flags: Int) {
    dest?.writeString(apiKey)
    dest?.writeString(projectId)
    dest?.writeMap(properties)
  }
}
