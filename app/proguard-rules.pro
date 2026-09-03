# Keep JARVIS app classes
-keep class com.darrenai.jarvis.** { *; }

# Keep model classes
-keep class com.darrenai.jarvis.data.** { *; }

# Kotlin coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }
