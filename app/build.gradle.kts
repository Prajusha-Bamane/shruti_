plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
  
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage.ktx)
  implementation(libs.firebase.firestore)
  implementation(libs.androidx.ui.desktop)
  testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
  implementation("com.google.android.gms:play-services-maps:18.1.0")

    implementation ("com.google.code.gson:gson:2.10")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-analytics-ktx")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    implementation ("com.squareup.picasso:picasso:2.8")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")

    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")


    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.6.1")

  implementation(libs.google.firebase.bom)

  implementation ("com.google.firebase:firebase-firestore-ktx")

  implementation(platform("com.google.firebase:firebase-bom:32.2.3")) // Firebase BOM
  implementation("com.google.firebase:firebase-messaging-ktx")

}