plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

}


android {
    namespace = "com.sayi.vdim"
    compileSdk = 35
    packagingOptions {
        resources {
            excludes += setOf("META-INF/DEPENDENCIES")
        }
    }

    repositories {

    }

    defaultConfig {
        applicationId = "com.sayi.vdim"
        compileSdk = 35
        minSdk = 28
        targetSdk = 35
        versionCode = 9
        versionName = "0.6.0-alpha"

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
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
    }
    android.applicationVariants.all {
        // 编译类型
        val buildType = this.buildType.name
        val versionName = android.defaultConfig.versionName
        val versionCode = android.defaultConfig.versionCode
        outputs.all {
            // 判断是否是输出 apk 类型
            if (this is com.android.build.gradle
                .internal.api.ApkVariantOutputImpl
            ) {
                this.outputFileName = "V次元" +
                        "_${versionName}_${versionCode}_${buildType}.apk"
                val suffix = if (buildType == "debug") "-debug" else ""
                this.versionNameOverride = "$versionName$suffix"
            }
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    sourceSets {
        getByName("main") {
            res.srcDirs(
                "src/main/res/activity",
                "src/main/res/fragmentlayout",
                "src/main/res/fragments",
                "src/main/res/common",
                "src/main/res/items",
                "src/main/res/legacy"
            )
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    /*implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22"){
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
    }*/


    implementation("com.google.android.material:material:1.12.0")

    implementation("com.android.volley:volley:1.2.1")




    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.appcompat:appcompat:1.7.0")


    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-ktx:1.9.3")

    implementation("androidx.media3:media3-exoplayer:1.5.0")
    implementation("androidx.media3:media3-session:1.5.0")
    implementation("androidx.media3:media3-ui:1.5.0")

    implementation("org.apache.httpcomponents.client5:httpclient5:5.4.1")



    // Retrofit for network requests
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")



    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.alibaba:fastjson:1.2.47")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    api("org.wordpress:aztec:v1.6.2")

    implementation("org.wordpress.aztec:glide-loader:1080-2a2a1d5e515185d2df97442c0bb45aa5f46b8471")


    implementation("org.apache.tika:tika-core:2.9.0")

    implementation("org.apache.tika:tika-parsers:2.9.0") {
        exclude(group = "org.osgi")
        exclude(group = "javax.xml.stream")
        exclude(group = "aQute.bnd")
    }

    implementation("com.github.ihsanbal:LoggingInterceptor:3.1.0") {//httplogger
        exclude(group = "org.json", module = "json")
    }
    implementation("com.github.amggg:YXing:V2.0.1")

}
