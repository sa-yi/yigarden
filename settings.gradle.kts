pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        flatDir {
            dirs("libs")  // 添加 flatDir 仓库
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://a8c-libs.s3.amazonaws.com/android") }
    }
}

rootProject.name = "V次元"

include(":app")