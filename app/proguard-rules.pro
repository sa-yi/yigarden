# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 保留 com.sayi.vdim.dz_entity 包下的所有类及其字段
-keep class com.sayi.vdim.dz_entity.** { *; }
-keep class com.sayi.vdim.sayi_music_entity.** {*;}
-keep class com.sayi.vdim.customentity.** {*;}

-optimizationpasses 5


-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-keep class javax.xml.stream.** { *; }
-keep class org.apache.tika.** { *; }


-dontwarn java.awt.Color
-dontwarn java.awt.Font
-dontwarn java.awt.Point
-dontwarn java.awt.Rectangle
-dontwarn javax.ws.rs.Consumes
-dontwarn javax.ws.rs.Produces
-dontwarn javax.ws.rs.core.Response
-dontwarn javax.ws.rs.core.StreamingOutput
-dontwarn javax.ws.rs.ext.MessageBodyReader
-dontwarn javax.ws.rs.ext.MessageBodyWriter
-dontwarn javax.ws.rs.ext.Provider
-dontwarn org.glassfish.jersey.internal.spi.AutoDiscoverable
-dontwarn springfox.documentation.spring.web.json.Json
-dontwarn sun.reflect.annotation.AnnotationType

-dontwarn aQute.bnd.annotation.Version
-dontwarn javax.xml.stream.XMLInputFactory
-dontwarn javax.xml.stream.XMLResolver
-dontwarn org.osgi.framework.BundleActivator
-dontwarn org.osgi.framework.BundleContext
-dontwarn org.osgi.framework.ServiceReference
-dontwarn org.osgi.util.tracker.ServiceTracker
-dontwarn org.osgi.util.tracker.ServiceTrackerCustomizer


-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
-assumenosideeffects class java.io.PrintStream {
    public *** println(...);
    public *** print(...);
}