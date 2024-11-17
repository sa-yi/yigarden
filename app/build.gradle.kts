plugins {
    id("com.android.application")

}


android {
    namespace = "com.sayi.yi_garden"
    compileSdk = 34


    repositories {

    }

    defaultConfig {
        applicationId = "com.sayi.yi_garden"
        compileSdk = 34
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isDebuggable = false
            // 启用代码压缩、优化及混淆
            isMinifyEnabled = true
            // 启用资源压缩，需配合 minifyEnabled=true 使用
            isShrinkResources=true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    sourceSets {
        getByName("main") {
            res.srcDirs(
                "src/main/res/layout/activity",
                "src/main/res/layout/fragmentlayout",
                "src/main/res/layout/fragments",
                "src/main/res/layout/common",
                "src/main/res/layout/items",
                "src/main/res"
            )
        }
    }

}

dependencies {
    //implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22")


    implementation("com.google.android.material:material:1.12.0")

    implementation("com.android.volley:volley:1.2.1")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.navigation:navigation-fragment:2.8.3")
    implementation("androidx.navigation:navigation-ui:2.8.3")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.appcompat:appcompat:1.7.0")

    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-session:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")


    // Retrofit for network requests
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")



    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.alibaba:fastjson:1.2.47")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("androidx.preference:preference:1.2.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    api("org.wordpress:aztec:v1.6.2")

    implementation("org.wordpress.aztec:glide-loader:1080-2a2a1d5e515185d2df97442c0bb45aa5f46b8471")

}
