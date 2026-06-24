# OneManage Kotlin Multiplatform SDK

The OneManage SDK is a unified telemetry, bug reporting, and remote configuration SDK built from the ground up for Kotlin Multiplatform. It provides a single API to instrument your app across Android, iOS, and Desktop (JVM) while automatically handling offline-caching, batching, and native hardware metadata.

## 🌟 Key Features
- **Cross-Platform**: Supports Android, iOS, and Desktop JVM out of the box.
- **Battery & Network Efficient**: Employs debounced background batching to minimize network requests.
- **Offline First**: All logs and bug reports are safely cached locally in SQLDelight until a network connection is available.
- **Remote Config**: Syncs server-side feature flags directly to a reactive state flow.
- **Native Logging**: Pipes internal API diagnostics directly into Android Logcat, iOS Console, or JVM Standard Out.

---

## 📦 Installation

Add the OneManage module to your `commonMain` dependencies in your `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":onemanage")) // Or via maven if published
        }
    }
}
```

---

## 🚀 Initialization

The SDK must be initialized once during your application's startup phase.

### Step 1: Create the Platform Context

Because the SDK needs access to native APIs (like `Context` on Android), you must provide the platform context.

**On Android** (Inside your `Application` class):
```kotlin
import com.onemanage.internal.PlatformContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Android PlatformContext requires the application instance
        val context = PlatformContext(this)
        
        OneManage.initialize(
            context = context,
            config = OneManageConfig(
                apiKey = "YOUR_API_KEY",
                appId = "YOUR_APP_ID",
                enableLogging = BuildConfig.DEBUG // Highly recommended for dev
            )
        )
    }
}
```

**On iOS** (Inside `MainViewController.kt` or `AppDelegate.swift` interop):
```kotlin
val context = PlatformContext() // iOS Context is empty
OneManage.initialize(...)
```

**On Desktop / JVM** (Inside `fun main()`):
```kotlin
val context = object : PlatformContext() {} // JVM uses an anonymous abstract class
OneManage.initialize(...)
```

### Step 2: Observing Initialization State
The SDK verifies your API key and syncs Remote Config immediately upon initialization. You can observe the setup state reactively:

```kotlin
val state by OneManage.state.collectAsState()

when (state) {
    SdkState.INITIALIZING -> { /* Show splash screen */ }
    SdkState.INITIALIZED -> { /* SDK is ready! */ }
    SdkState.FAILED -> { /* API Key is invalid or network blocked */ }
}
```

---

## 👤 User Identity

By default, the SDK assigns a persistent anonymous UUID to the device. However, when your user logs in, you should tie their session to the SDK:

```kotlin
// Call this right after a successful login
OneManage.setUserId("usr_987654321")

// If they log out, revert to anonymous tracking
OneManage.setUserId(null)
```
*All logs, crashes, and bug reports will now automatically attach this User ID.*

---

## 📝 1. Telemetry & Logging

The OneManage Logger replaces your standard `println` or `Log.d` statements. Logs are temporarily held in memory and **automatically flushed** 15 seconds after the last log, or when the queue hits 50 items.

```kotlin
// Basic Usage
OneManage.logger.info("PaymentScreen", "User tapped checkout")
OneManage.logger.debug("Network", "Response took 450ms")
OneManage.logger.warn("Storage", "Disk space running low")
OneManage.logger.error("Auth", "Failed to parse JWT token")

// Force an immediate flush (useful before app shutdown)
scope.launch {
    OneManage.logger.flush()
}
```

---

## 🐛 2. Bug Reporting

Allow your users (or internal QA) to submit detailed bug reports. The SDK automatically appends hardware metadata, OS version, app version, and localization data to every report.

```kotlin
OneManage.bugReporter.submitBug(
    title = "Checkout button overlapping",
    description = "On small screens, the checkout button blocks the total price.",
    severity = "HIGH", // LOW, NORMAL, HIGH, CRITICAL
    steps = "1. Open Cart\n2. Rotate to landscape",
    submittedBy = "qa-tester-bob",
    metadata = mapOf(
        "screen_route" to "/cart",
        "cart_items" to "4"
    )
)
```

---

## 🎛 3. Remote Configuration

Manage feature flags and dynamic values remotely without pushing app updates. The local cache ensures instantaneous reads, while the SDK syncs with the server in the background.

**Accessing Values:**
```kotlin
val isPromoActive = OneManage.configManager.getBoolean("promo_banner_active", default = false)
val maxRetries = OneManage.configManager.getInt("network_retries", default = 3)
```

**Reactive UI (Compose):**
If you want your UI to automatically update the moment a remote config is changed:
```kotlin
val configs by OneManage.configManager.configs.collectAsState()

if (configs["promo_banner_active"]?.toBooleanStrictOrNull() == true) {
    PromoBanner()
}
```

**Forcing a manual fetch:**
```kotlin
OneManage.configManager.fetchRemoteConfig()
```

---

## 🛠 Advanced SDK Configuration

The `OneManageConfig` builder allows internal control over the networking environment, primarily used by your internal team for pre-production testing.

```kotlin
OneManageConfig(
    apiKey = "...",
    appId = "...",
    enableLogging = true, // Enables Ktor HTTP body logging
    
    // [Internal] Override the API endpoint URL for local backend testing
    environment = Environment.DEV, 
    
    // [Internal] Override the ingestion schema version 
    schemaVersion = "2.0-beta"
)
```
