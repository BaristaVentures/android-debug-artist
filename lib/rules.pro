-dontobfuscate
-keepattributes InnerClasses, EnclosingMethod
-dontskipnonpubliclibraryclasses

# Debug Artist
-keep class debug_artist.menu.** { *; }

# Falcon screenshots
-keep class com.jraska.falcon.** { *; }

#
-keep class com.android.org.conscrypt.SSLParametersImpl
-keep class org.apache.harmony.xnet.provider.jsse.SSLParametersImpl
-keep class sun.security.ssl.SSLContextImpl

# leakcanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
-keep class com.squareup.haha.** { *; }
-dontwarn com.squareup.leakcanary.**
-dontwarn com.squareup.haha.guava.**
-dontwarn com.squareup.haha.perflib.**
-dontwarn com.squareup.haha.trove.**

# Marshmallow removed Notification.setLatestEventInfo()
-dontwarn android.app.Notification

# Stetho
-keepclasseswithmembers class com.facebook.stetho.** { *; }
-keep class com.facebook.stetho.** { *; }
-dontwarn com.facebook.stetho.**

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
-keep class com.github.pedrovgs.lynx.LynxActivity
-keep class com.mikepenz.iconics.typeface.IIcon
-keep class com.mikepenz.iconics.IconicsDrawable
-keep class com.mikepenz.materialize.view.OnInsetsCallback
-keep class com.squareup.haha.perflib.ClassInstance$FieldValue
-keep class com.squareup.haha.perflib.Field
-keep class com.squareup.haha.perflib.Instance
-keep class com.squareup.haha.perflib.ClassObj
-keep class com.squareup.haha.perflib.Snapshot
-keep class com.squareup.okhttp.MediaType

# Jacoco
## Keep everything for the jacoco classes

# Keep everything for the emma classes
-keep class com.vladium.** { *; }
# Keep everything for the jacoco classes
-keep class org.jacoco.** { *; }


# Bug Reporter
## RXJava
-dontwarn java.lang.invoke.*

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8

# OkHttp3
-dontwarn okhttp3.**
-dontwarn okio.**

-dontwarn com.squareup.picasso.**



-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }