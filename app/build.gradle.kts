plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.veltix.praktikum6"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.veltix.praktikum6"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.androidx.activity.ktx)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.material)

        // Retrofit dan Gson
        implementation(libs.retrofit)
        implementation(libs.retrofit.gson)

        // OkHttp Logging
        implementation(libs.okhttp.logging)

        // Coil untuk menampilkan poster film
        implementation(libs.coil)
        implementation(libs.coil.okhttp)

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }
