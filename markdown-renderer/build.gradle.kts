import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            
            // JetBrains Markdown parser
            implementation("org.jetbrains:markdown:0.7.3")

            // Ktor Client for fetching images/badges
            implementation(libs.ktor.client.core)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
    }
}
