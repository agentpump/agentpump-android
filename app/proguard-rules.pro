# AgentPump ProGuard Rules

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson
-keep class fun.agentpump.arena.data.models.** { *; }

# Compose
-dontwarn androidx.compose.**
