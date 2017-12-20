-dontobfuscate
-keepattributes InnerClasses, EnclosingMethod, *Annotation*

-dontskipnonpubliclibraryclasses

-keepclasseswithmembers class debug_artist.reporter_pivotaltracker.Story { *; }
-keepclasseswithmembers class debug_artist.reporter_pivotaltracker.StoryRequestBody { *; }
-keepclasseswithmembers class debug_artist.reporter_pivotaltracker.Comment { *; }
-keepclasseswithmembers class debug_artist.reporter_pivotaltracker.Attachment { *; }

-dontwarn kotlin.reflect.jvm.internal.**