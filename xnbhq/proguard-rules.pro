-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dontoptimize
-keepattributes *Annotation*
-keepattributes Signature
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn org.codehaus.jackson.map.ext.**
-keep class org.codehaus.jackson.map.ext.** { *;}

-dontwarn jcifs.http.**
-keep class jcifs.http.** { *;}

-dontwarn freemarker.**
-keep class freemarker.** { *;}

-dontwarn jsqlite.**
-keep class jsqlite.** { *;}

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *;}

-dontwarn jcifs.**
-keep class jcifs.** { *;}

-dontwarn com.esri.android.toolkit.**
-keep class com.esri.android.toolkit.** { *;}

-dontwarn jxl.**
-keep class jxl.** { *;}

-dontwarn org.codehaus.jackson.**
-keep class org.codehaus.jackson.** { *;}

-dontwarn com.google.gson.**
-keep class com.google.gson.** { *;}

-dontwarn de.greenrobot.dao.**
-keep class de.greenrobot.dao.** { *;}

-dontwarn com.baidu.location.**
-keep class com.baidu.location.** { *;}

-dontwarn org.apache.commons.lang3.**
-keep class org.apache.commons.lang3.** { *;}

-dontwarn org.kobjects.**
-keep class org.kobjects.** { *;}

-dontwarn org.ksoap2.**
-keep class org.ksoap2.** { *;}

-dontwarn org.kxml2.**
-keep class org.kxml2.** { *;}

-dontwarn org.achartengine.internal.**
-keep class org.achartengine.internal.** { *;}

-dontwarn com.esri.core.**
-keep class com.esri.core.** { *;}

-dontwarn com.esri.android.**
-keep class com.esri.android.** { *;}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
