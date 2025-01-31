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

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt

-keep,includedescriptorclasses class nl.woonwaard.wij_maken_de_wijk.api.models.**$$serializer { *; }
-keepclassmembers class nl.woonwaard.wij_maken_de_wijk.api.models.* {
    *** Companion;
}
-keepclasseswithmembers class nl.woonwaard.wij_maken_de_wijk.api.models.* {
    kotlinx.serialization.KSerializer serializer(...);
}

-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt

-keep,includedescriptorclasses class nl.woonwaard.wij_maken_de_wijk.domain.models.**$$serializer { *; }
-keepclassmembers class nl.woonwaard.wij_maken_de_wijk.domain.models.* {
    *** Companion;
}
-keepclasseswithmembers class nl.woonwaard.wij_maken_de_wijk.domain.models.* {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class nl.woonwaard.wij_maken_de_wijk.domain.utils.**$$serializer { *; }
-keepclassmembers class nl.woonwaard.wij_maken_de_wijk.domain.utils.* {
    *** Companion;
}
-keepclasseswithmembers class nl.woonwaard.wij_maken_de_wijk.domain.utils.* {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**
