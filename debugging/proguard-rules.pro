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