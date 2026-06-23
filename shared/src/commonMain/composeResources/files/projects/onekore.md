# OneKore Multiplatform SDK

OneKore is a modular **Kotlin Multiplatform (KMP)** SDK designed to standardize and speed up mobile application development across Android, iOS, and Web platforms.

## Core Features
- **Shared ViewModels**: Business logic resides in a single, platform-independent layer using standard Kotlin Coroutines and Flow.
- **Dependency Injection**: Preconfigured DI engine powered by Koin Multiplatform.
- **Type-safe Navigation**: Fully unified navigation system that eliminates route template string boilerplate.

## Architecture
```
┌───────────────────────────────────────┐
│              App UI                   │
│  (Compose Multiplatform / SwiftUI)    │
└──────────────────┬────────────────────┘
                   ▼
┌───────────────────────────────────────┐
│           Shared ViewModels           │
│         (StateFlow / MVI)             │
└──────────────────┬────────────────────┘
                   ▼
┌───────────────────────────────────────┐
│         KMP Core Architecture         │
│          (DI, Ktor, LocalDB)          │
└───────────────────────────────────────┘
```

## Getting Started
To include OneKore in your KMP project, add the dependency to your `build.gradle.kts`:
```kotlin
commonMain.dependencies {
    implementation("com.one.kore:onekore-core:1.0.0")
}
```
