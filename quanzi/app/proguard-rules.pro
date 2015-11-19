# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/qixingchen/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-dontwarn com.squareup.okhttp.**
#
#-keepattributes Signature
#-dontwarn com.jcraft.jzlib.**
#-keep class com.jcraft.jzlib.**  { *;}
#
#-dontwarn sun.misc.**
#-keep class sun.misc.** { *;}
#
#-dontwarn com.alibaba.fastjson.**
#-keep class com.alibaba.fastjson.** { *;}
#
#-dontwarn sun.security.**
#-keep class sun.security.** { *; }
#
#-dontwarn com.google.**
#-keep class com.google.** { *;}
#
#-dontwarn com.avos.**
#-keep class com.avos.** { *;}
#
#-keep public class android.net.http.SslError
#-keep public class android.webkit.WebViewClient
#
#-dontwarn android.webkit.WebView
#-dontwarn android.net.http.SslError
#-dontwarn android.webkit.WebViewClient
#
#-dontwarn android.support.**
#
#-dontwarn org.apache.**
#-keep class org.apache.** { *;}
#
#-dontwarn org.jivesoftware.smack.**
#-keep class org.jivesoftware.smack.** { *;}
#
#-dontwarn com.loopj.**
#-keep class com.loopj.** { *;}
#
#-dontwarn com.squareup.okhttp.**
#-keep class com.squareup.okhttp.** { *;}
#-keep interface com.squareup.okhttp.** { *; }
#
#-dontwarn okio.**
#
#-dontwarn org.xbill.**
#-keep class org.xbill.** { *;}
#
#-keepattributes *Annotation*
#
#-keep class com.facebook.stetho.** { *; }
#
#-keepclassmembers class ** {
#    @com.squareup.otto.Subscribe public *;
#    @com.squareup.otto.Produce public *;
#}
#
#-keep class retrofit.** { *; }
#
#-dontwarn retrofit.**
#-dontwarn rx.**
#
#-keep class com.google.gson.stream.** { *; }
#-keep class com.google.gson.** { ; }
#
#-keepclasseswithmembers class * {
#    @retrofit.http.* <methods>;
#}
#
## If in your rest service interface you use methods with Callback argument.
#-keepattributes Exceptions
#
## If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
#-keepattributes Signature

