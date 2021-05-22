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
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.COMPILE_SDK
    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "CONFERENCE_TIMEZONE",
            project.properties["conference_timezone"] as String
        )
        buildConfigField(
            "String",
            "CONFERENCE_DAY1_START",
            project.properties["conference_day1_start"] as String
        )
        buildConfigField(
            "String",
            "CONFERENCE_DAY1_END",
            project.properties["conference_day1_end"] as String
        )
        buildConfigField(
            "String",
            "CONFERENCE_DAY2_START",
            project.properties["conference_day2_start"] as String
        )
        buildConfigField(
            "String",
            "CONFERENCE_DAY2_END",
            project.properties["conference_day2_end"] as String
        )
        buildConfigField(
            "String",
            "CONFERENCE_DAY3_START",
            project.properties["conference_day3_start"] as String
        )
        buildConfigField(
            "String",
            "CONFERENCE_DAY3_END",
            project.properties["conference_day3_end"] as String
        )

        buildConfigField(
            "String",
            "CONFERENCE_DAY1_AFTERHOURS_START",
            project.properties["conference_day1_afterhours_start"] as String
        )
        buildConfigField(
            "String",
            "CONFERENCE_DAY2_CONCERT_START",
            project.properties["conference_day2_concert_start"] as String
        )

        buildConfigField(
            "String",
            "BOOTSTRAP_CONF_DATA_FILENAME",
            project.properties["bootstrap_conference_data_filename"] as String
        )

        buildConfigField(
            "String",
            "CONFERENCE_WIFI_OFFERING_START",
            project.properties["conference_wifi_offering_start"] as String
        )

        consumerProguardFiles("consumer-proguard-rules.pro")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }

    buildTypes {
        getByName("release") {
            buildConfigField(
                "String",
                "REGISTRATION_ENDPOINT_URL",
                "\"https://events-d07ac.appspot.com/_ah/api/registration/v1/register\""
            )
            buildConfigField(
                "String",
                "CONFERENCE_DATA_URL",
                "\"https://firebasestorage.googleapis.com/v0/b/io2019-festivus-prod/o/sessions.json?alt=media&token=89140adf-e228-45a5-9ae3-8ed01547166a\""
            )
        }
        getByName("debug") {
            buildConfigField(
                "String",
                "REGISTRATION_ENDPOINT_URL",
                "\"https://events-dev-62d2e.appspot.com/_ah/api/registration/v1/register\""
            )
            buildConfigField(
                "String",
                "CONFERENCE_DATA_URL",
                "\"https://firebasestorage.googleapis.com/v0/b/io2019-festivus/o/sessions.json?alt=media&token=019af2ec-9fd1-408e-9b86-891e4f66e674\""
            )
        }
        maybeCreate("staging")
        getByName("staging") {


            // Specifies a sorted list of fallback build types that the
            // plugin should try to use when a dependency does not include a
            // "staging" build type.
            // Used with :test-shared, which doesn't have a staging variant.
            setMatchingFallbacks(listOf("debug"))
        }
    }

    lint {
        disable("InvalidPackage", "MissingTranslation")
        // Version changes are beyond our control, so don't warn. The IDE will still mark these.
        disable("GradleDependency")
        // Timber needs to update to new Lint API
        disable("ObsoleteLintCustomCheck")
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

    // Some libs (such as androidx.core:core-ktx 1.2.0 and newer) require Java 8

    // To avoid the compile error: "Cannot inline bytecode built with JVM target 1.8
    // into bytecode that is being built with JVM target 1.6"
    kotlinOptions {
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "1.8"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    api(project(":model"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation(project(":test-shared"))
    testImplementation(project(":androidTest-shared"))

    // AppCompat
    implementation("androidx.appcompat:appcompat:1.3.0")

    // Architecture Components
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0-alpha01")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0-alpha01")
    implementation("androidx.room:room-ktx:2.3.0")
    implementation("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")

    // Maps
    api("com.google.maps.android:maps-utils-ktx:3.0.1") {
        exclude(group = "com.google.android.gms")
    }
    api("com.google.maps.android:maps-ktx:3.0.1")

    // Utils
    api("com.jakewharton.timber:timber:4.7.1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("androidx.core:core-ktx:1.3.2")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.0")

    // Coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.0")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.35.1")
    kapt("com.google.dagger:hilt-android-compiler:2.35.1")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0-beta01")

    // Firebase
    api("com.google.firebase:firebase-auth-ktx:21.0.1")
    api("com.google.firebase:firebase-config-ktx:21.0.0")
    api("com.google.firebase:firebase-analytics-ktx:19.0.0")
    api("com.google.firebase:firebase-firestore-ktx:23.0.0")
    api("com.google.firebase:firebase-functions-ktx:20.0.0") {
        exclude(group = "com.google.firebase", module = "firebase-iid")
    }
    api("com.google.firebase:firebase-messaging:22.0.0") {
        exclude(group = "com.google.firebase", module = "firebase-iid")
    }

    // Has to be replaced to avoid compile / runtime conflicts between okhttp and firestore
    api("com.squareup.okio:okio:2.10.0")

    // ThreeTenBP for the shared module only. Date and time API for Java.
    testImplementation("org.threeten:threetenbp:1.5.1")
    compileOnly("org.threeten:threetenbp:1.5.1:no-tzdb")

    // Unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    testImplementation("org.mockito:mockito-core:3.10.0")
    testImplementation("com.nhaarman:mockito-kotlin:1.6.0")

    // unit tests livedata
    testImplementation("androidx.arch.core:core-testing:2.1.0")
}

apply(plugin = "com.google.gms.google-services")
