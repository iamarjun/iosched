/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.COMPILE_SDK
    defaultConfig {
        applicationId = "com.google.samples.apps.iosched"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.versionCodeMobile
        versionName = Versions.versionName
        testInstrumentationRunner = "com.google.samples.apps.iosched.tests.CustomTestRunner"

        buildConfigField(
            "com.google.android.gms.maps.model.LatLng",
            "MAP_VIEWPORT_BOUND_NE",
            "new com.google.android.gms.maps.model.LatLng(${project.properties["map_viewport_bound_ne"]})"
        )
        buildConfigField(
            "com.google.android.gms.maps.model.LatLng",
            "MAP_VIEWPORT_BOUND_SW",
            "new com.google.android.gms.maps.model.LatLng(${project.properties["map_viewport_bound_sw"]})"
        )

        buildConfigField(
            "float",
            "MAP_CAMERA_FOCUS_ZOOM",
            project.properties["map_camera_focus_zoom"] as String
        )

        resValue(
            "dimen",
            "map_camera_bearing",
            project.properties["map_default_camera_bearing"] as String
        )
        resValue(
            "dimen",
            "map_camera_target_lat",
            project.properties["map_default_camera_target_lat"] as String
        )
        resValue(
            "dimen",
            "map_camera_target_lng",
            project.properties["map_default_camera_target_lng"] as String
        )
        resValue(
            "dimen",
            "map_camera_tilt",
            project.properties["map_default_camera_tilt"] as String
        )
        resValue(
            "dimen",
            "map_camera_zoom",
            project.properties["map_default_camera_zoom"] as String
        )
        resValue(
            "dimen",
            "map_viewport_min_zoom",
            project.properties["map_viewport_min_zoom"] as String
        )
        resValue(
            "dimen",
            "map_viewport_max_zoom",
            project.properties["map_viewport_max_zoom"] as String
        )

        manifestPlaceholders["crashlyticsEnabled"] = true

        vectorDrawables.useSupportLibrary = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            // TODO: b/120517460 shrinkResource can't be used with dynamic-feature at this moment.
            //       Need to ensure the app size has not increased
            manifestPlaceholders["crashlyticsEnabled"] = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue(
                "string",
                "google_maps_key",
                "AIzaSyD5jqwKMm1SeoYsW25vxCXfTlhDBeZ4H5c"
            )

            buildConfigField(
                "String",
                "MAP_TILE_URL_BASE",
                "\"https://storage.googleapis.com/io2019-festivus-prod/images/maptiles\""
            )
        }
        getByName("debug") {
            versionNameSuffix = "-debug"
            manifestPlaceholders["crashlyticsEnabled"] = false
            resValue(
                "string",
                "google_maps_key",
                "AIzaSyAhJx57ikQH9rYc8IT8W3d2As5cGHMBvuo"
            )

            buildConfigField(
                "String",
                "MAP_TILE_URL_BASE",
                "\"https://storage.googleapis.com/io2019-festivus/images/maptiles\""
            )
        }
        maybeCreate("staging")
        getByName("staging") {

            versionNameSuffix = "-staging"

            // Specifies a sorted list of fallback build types that the
            // plugin should try to use when a dependency does not include a
            // "staging" build type.
            // Used with :test-shared, which doesn't have a staging variant.
            matchingFallbacks.add("debug")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.0-rc01"
    }

    signingConfigs {
        // We need to sign debug builds with a debug key to make firebase auth happy
        getByName("debug") {
            storeFile = file("/Users/arjunmanoj/StudioProjects/iosched/debug.keystore")
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storePassword = "android"
        }
    }

    // debug and release variants share the same source dir
    sourceSets {
        getByName("debug") {
            java.srcDir("src/debugRelease/java")
        }
        getByName("release") {
            java.srcDir("src/debugRelease/java")
        }
    }

    lint {
        // Eliminates UnusedResources false positives for resources used in DataBinding layouts
        isCheckGeneratedSources = true
        // Running lint over the debug variant is enough
        isCheckReleaseBuilds = false
        // See lint.xml for rules configuration

        // TODO: Remove when upgrading lifecycle from `2.4.0-alpha01`.
        // Fix: https://android-review.googlesource.com/c/platform/frameworks/support/+/1697465
        // Bug: https://issuetracker.google.com/184830263
        disable("NullSafeMutableLiveData")
    }

    testBuildType = "staging"

    // Required for AR because it includes a library built with Java 8
    // To avoid the compile error: "Cannot inline bytecode built with JVM target 1.8
    // into bytecode that is being built with JVM target 1.6"
    kotlinOptions {
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "11"
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packagingOptions {
        resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
        }
    }
}

kapt {
    arguments {
        arg("dagger.hilt.shareTestComponents", "true")
    }
}

dependencies {

    implementation(project(":shared"))
    implementation(project(":ar"))
    testImplementation(project(":test-shared"))
    testImplementation(project(":androidTest-shared"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.core:core-ktx:1.3.2")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    // UI
    implementation("androidx.activity:activity-ktx:1.3.0-rc01")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.fragment:fragment-ktx:1.3.5")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.browser:browser:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")
    implementation("com.google.android.material:material:1.4.0-rc01")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.airbnb.android:lottie:3.7.0")
    implementation("com.pacioianu.david:ink-page-indicator:1.3.0")
    implementation("androidx.slidingpanelayout:slidingpanelayout:1.2.0-alpha03")

    // Architecture Components
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.4.0-alpha02")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
//    implementation("androidx.navigation:navigation-fragment-ktx:2.4.0-alpha04")
//    implementation("androidx.navigation:navigation-ui-ktx:2.4.0-alpha04")
    implementation("androidx.room:room-ktx:2.3.0")
    implementation("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
    testImplementation("androidx.room:room-ktx:2.3.0")
    testImplementation("androidx.room:room-runtime:2.3.0")

    // Compose
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha04")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0-alpha02")
    implementation("androidx.activity:activity-compose:1.3.0-rc01")
    implementation("androidx.compose.animation:animation:1.0.0-rc01")
    implementation("androidx.compose.material:material:1.0.0-rc01")
    implementation("androidx.compose.runtime:runtime:1.0.0-rc01")
    implementation("com.google.android.material:compose-theme-adapter:1.0.0-rc01")
    implementation("androidx.compose.ui:ui-tooling:1.0.0-rc01")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
    implementation("com.google.android.material:compose-theme-adapter:1.0.0-rc01")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.0-rc01")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.37")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.37")
    kapt("com.google.dagger:hilt-android-compiler:2.37")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.37")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0-rc01")
    androidTestImplementation("androidx.datastore:datastore-preferences:1.0.0-rc01")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")

    // Fabric and Firebase
    implementation(platform("com.google.firebase:firebase-bom:28.0.1"))
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("com.google.firebase:firebase-crashlytics:18.1.0")

    //Accompanist
    implementation("com.google.accompanist:accompanist-coil:0.13.0")
    implementation("com.google.accompanist:accompanist-insets:0.13.0")
    implementation("com.google.accompanist:accompanist-placeholder:0.13.0")
    implementation("com.google.accompanist:accompanist-placeholder-material:0.13.0")
    implementation("com.google.accompanist:accompanist-pager:0.13.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.13.0")


    // Kotlin
    //implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.0")

    // Instrumentation tests
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.fragment:fragment-ktx:1.3.5")
    debugImplementation("androidx.fragment:fragment-testing:1.3.5")
    add("stagingImplementation", "androidx.fragment:fragment-testing:1.3.5")

    // Local unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.11.1")
    testImplementation("com.nhaarman:mockito-kotlin:1.6.0")
    testImplementation("org.hamcrest:hamcrest-library:2.2")

    // Solve conflicts with gson. DataBinding is using an old version.
    implementation("com.google.code.gson:gson:2.8.7")

    implementation("com.google.ar:core:1.25.0")
}

apply(plugin = "com.google.gms.google-services")
