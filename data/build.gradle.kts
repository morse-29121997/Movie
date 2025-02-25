plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.morse.mylibrary"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
        buildConfigField("String", "TOKEN", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMmFlMDIxMGQxMDc4NjNmZDFkODliMWUyZWUxZjI2YSIsIm5iZiI6MTUyOTEwNDYxOS4xOTYsInN1YiI6IjViMjQ0OGViMGUwYTI2NjcyOTAwMDMzZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.3iT4csoL3Umkk8ZP-BasRm6N_1XMlT92q0HEuvdNRwU\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {
    api(project(":domain"))
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.core)
    api(libs.retrofit)
    api(libs.okhttp)
    api(libs.okhttp.logging.interceptor)
    api(libs.gson.converter)
    api(libs.gson)
    api(libs.room.runtime)
    api(libs.room.ktx)
    api(libs.androidx.paging.runtime)
    api(libs.androidx.paging.common)
    ksp(libs.room.compiler)
    api(libs.hilt.android)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}