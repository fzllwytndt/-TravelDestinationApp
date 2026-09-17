plugins {
    alias(libs.plugins.android.application)
    // 1. Tambahkan plugin KSP di sini
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.tugas2_loginregister"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.tugas2_loginregister"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
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
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)

    // Networking & Parsing
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // MVVM & Coroutines
    implementation(libs.androidx.activity.ktx)
    implementation(libs.coroutines.android)

    //Tambahkan implementasi Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler) // Gunakan ksp untuk compiler-nya

    //Tambahkan implementasi Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)


    //swiperefresh
    implementation(libs.androidx.swiperefreshlayout)

    // Image Loader
    implementation(libs.glide)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
}