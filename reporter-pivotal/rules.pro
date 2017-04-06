-dontobfuscate
-keepattributes InnerClasses, EnclosingMethod
-dontskipnonpubliclibraryclasses

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }

-keepclasseswithmembers class debug_artist.reporter_pivotaltracker.Story { *; }
-keepclasseswithmembers class debug_artist.reporter_pivotaltracker.StoryRequestBody { *; }
-keepclasseswithmembers class debug_artist.reporter_pivotaltracker.Comment { *; }
-keepclasseswithmembers class debug_artist.reporter_pivotaltracker.Attachment { *; }