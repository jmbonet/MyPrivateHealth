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

# Keep MPAndroidChart classes
-keep class com.github.mikephil.charting.** { *; }
-keep class com.github.mikephil.charting.data.** { *; }
-keep class com.github.mikephil.charting.renderer.** { *; }
-keep class com.github.mikephil.charting.utils.** { *; }
-keep class com.github.mikephil.charting.components.** { *; }
-keep class com.github.mikephil.charting.formatter.** { *; }
-keep class com.github.mikephil.charting.interfaces.** { *; }
-keep class com.github.mikephil.charting.listener.** { *; }
-keep class com.github.mikephil.charting.animation.** { *; }
-keep class com.github.mikephil.charting.highlight.** { *; }
-keep class com.github.mikephil.charting.jobs.** { *; }
-keep class com.github.mikephil.charting.matrix.** { *; }
-keep class com.github.mikephil.charting.notification.** { *; }
-keep class com.github.mikephil.charting.open.** { *; }
-keep class com.github.mikephil.charting.painter.** { *; }
-keep class com.github.mikephil.charting.plots.** { *; }
-keep class com.github.mikephil.charting.scale.** { *; }
-keep class com.github.mikephil.charting.styles.** { *; }
-keep class com.github.mikephil.charting.tools.** { *; }
-keep class com.github.mikephil.charting.view.** { *; }
-keep class com.github.mikephil.charting.widget.** { *; } 