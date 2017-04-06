package debug_artist.reporter_pivotaltracker

import android.os.Parcel
import android.os.Parcelable
import debug_artist.menu.report_bug.BugRepository

class PivotalBugRepositoryBuilder(val apiKey: String,
                                  val projectId: String,
                                  val properties: MutableMap<String, String>,
                                  val labels: Array<String>?)
  : BugRepository.Builder, Parcelable {
  override fun build(): BugRepository {
    return PivotalBugRepository(apiKey, projectId, properties, labels)
  }

  companion object {
    @JvmField val CREATOR: Parcelable.Creator<PivotalBugRepositoryBuilder> = object : Parcelable.Creator<PivotalBugRepositoryBuilder> {
      override fun createFromParcel(source: Parcel): PivotalBugRepositoryBuilder = PivotalBugRepositoryBuilder(source)
      override fun newArray(size: Int): Array<PivotalBugRepositoryBuilder?> = arrayOfNulls(size)
    }
  }

  constructor(source: Parcel) : this(source.readString(), source.readString(),
      mutableMapOf<String, String>().apply { source.readMap(this, MutableMap::class.java.classLoader) },
      source.createStringArray())

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel?, flags: Int) {
    dest?.writeString(apiKey)
    dest?.writeString(projectId)
    dest?.writeMap(properties)
    dest?.writeStringArray(labels)
  }
}
