-dontobfuscate
-keepattributes InnerClasses, EnclosingMethod
-dontskipnonpubliclibraryclasses

#
-keep class com.android.org.conscrypt.SSLParametersImpl
-keep class org.apache.harmony.xnet.provider.jsse.SSLParametersImpl
-keep class sun.security.ssl.SSLContextImpl

# leakcanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
-dontwarn com.squareup.leakcanary.**

# Stetho
-keep class com.facebook.stetho.** { *; }

# okio
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.animal_sniffer.**

-dontwarn com.squareup.haha.**
-dontwarn com.github.stkent.bugshaker.**

# To remove "the configuration keeps the entry point ... but not the descriptor class ..."
-keep class okio.BufferedSource
-keep class okio.Sink
-keep class okio.Source
-keep class okio.BufferedSink
-keep class okio.Buffer
-keep class okio.ByteString
-keep class okio.Buffer
-keep class okio.ForwardingTimeout
-keep class okio.AsyncTimeout
-keep class okio.Buffer

-keep class org.apache.commons.cli.CommandLineParser
-keep class org.apache.commons.cli.CommandLine
-keep class com.squareup.okhttp.Interceptor$Chain
-keep class com.squareup.okhttp.ResponseBody
-keep class com.squareup.okhttp.Request
-keep class com.squareup.okhttp.Response
-keep class com.squareup.okhttp.Connection
-keep class com.github.pedrovgs.lynx.LynxConfig
-keep class com.github.pedrovgs.lynx.presenter.LynxPresenter
-keep class com.mikepenz.iconics.typeface.IIcon
-keep class com.mikepenz.iconics.IconicsDrawable
-keep class com.mikepenz.materialize.view.OnInsetsCallback
-keep class com.squareup.haha.perflib.ClassInstance$FieldValue
-keep class com.squareup.haha.perflib.Field
-keep class com.squareup.haha.perflib.Instance
-keep class com.squareup.haha.perflib.ClassObj
-keep class com.squareup.haha.perflib.Snapshot
-keep class com.squareup.okhttp.MediaType

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

-keepclasseswithmembers class com.barista_v.debug_artist.repositories.pivotal.Story { *; }
-keepclasseswithmembers class com.barista_v.debug_artist.repositories.pivotal.StoryRequestBody { *; }
-keepclasseswithmembers class com.barista_v.debug_artist.repositories.pivotal.Comment { *; }
-keepclasseswithmembers class com.barista_v.debug_artist.repositories.pivotal.Attachment { *; }