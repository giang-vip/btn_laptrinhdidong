plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp) // Thêm dòng này
    id("org.jetbrains.kotlin.plugin.compose")
//    id("kotlin-kapt") // Quay lại dùng kapt
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.app_dich_quet_van_ban"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.app_dich_quet_van_ban"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    buildFeatures {
        buildConfig = true
    }

}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.foundation)
    //    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Jetpack Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

// HILT - THÊM CỤM NÀY
    implementation(libs.hilt.android)
//    kapt(libs.hilt.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose) // Rất quan trọng để dùng ViewModel trong NavGraph

    // ROOM (Giữ nguyên của bạn)
    val roomVersion = "2.7.0-alpha11"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
//    kapt("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")


    // ViewModel & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Các thứ khác (giữ nguyên)
    implementation("com.google.mlkit:translate:17.0.1")
    implementation("com.google.mlkit:text-recognition:16.0.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // Thư viện hỗ trợ hiển thị Preview
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Thư viện Google ML Kit - Nhận diện văn bản (OCR)
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")

    // Thư viện CameraX - Để mở Camera và chụp ảnh
    val camerax_version = "1.3.1"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    // thư viện hỗ tr crop ảnh
    implementation("com.vanniktech:android-image-cropper:4.6.0")
    // thư viện tóm tắt văn ba bằng gemini
    implementation("com.google.ai.client.generativeai:generativeai:0.7.0")
    implementation("com.aallam.openai:openai-client:3.7.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    // Thêm dòng này để sửa lỗi ListenableFuture và addListener
    implementation("com.google.guava:guava:31.0.1-android")

    // Đảm bảo bạn đã có dòng này (nếu chưa có thì thêm vào)
    implementation("androidx.concurrent:concurrent-futures-ktx:1.1.0")

    // đc file excel
//    implementation("org.apache.poi:poi:5.2.3")
//    implementation("org.apache.poi:poi-ooxml:5.2.3")
}