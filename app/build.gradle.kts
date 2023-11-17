plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.prashant.zomatov2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.prashant.zomatov2"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.firebaseui:firebase-ui-database:8.0.1")
    implementation("com.google.firebase:firebase-auth:22.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //SDP
    implementation("com.intuit.sdp:sdp-android:1.1.0")

    //GifViewer
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")

    //Picasso
    implementation("com.squareup.picasso:picasso:2.71828")


    //Custom Toast
    implementation("com.github.GrenderG:Toasty:1.5.2")


    //ImageSlider
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")



}