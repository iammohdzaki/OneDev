# FixLag SDK

FixLag is a lightweight performance diagnostics SDK for Android. It detects thread blocks, frame drops, memory leaks, and ANRs in real-time, helping you maintain a butter-smooth 120 FPS interface.

## Key Diagnostics
- **ANR Detection**: Instant stack traces when the main thread stalls.
- **Recomposition Tracker**: Identifies redundant Compose recompositions.
- **Memory Leak Finder**: Auto-leak tracing using bytecode injection.

## Installation
Add the gradle dependency to your app module:
```kotlin
dependencies {
    implementation("com.one.fixlag:core:1.2.0")
}
```
Initialize the SDK in your Application class:
```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FixLag.initialize(this)
    }
}
```
