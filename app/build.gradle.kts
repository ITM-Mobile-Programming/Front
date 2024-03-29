plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.hwido.pieceofdayfront"
    compileSdk = 34


    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.hwido.pieceofdayfront"
        minSdk = 27
        targetSdk = 33
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
//        jvmTarget = "1.8"
        jvmTarget = "17"
    }

    buildFeatures{
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("androidx.media3:media3-common:1.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //dexter
    implementation ("com.karumi:dexter:6.2.3")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Google Play 서비스 Auth 라이브러리
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Retrofit 및 Gson 컨버터
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Kotlin 코루틴 코어 및 안드로이드 라이브러리
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("androidx.security:security-crypto-ktx:1.1.0-alpha06")


    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // fragment commit
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //location library
    implementation ("com.google.android.gms:play-services-location:21.0.1")


//    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("androidx.activity:activity-ktx:1.4.0")



}