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
    id("androidx.benchmark")
    kotlin("kapt")
}

android {
    compileSdk = Versions.COMPILE_SDK
    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
    }

    buildTypes {
        maybeCreate("staging")
        getByName("staging") {

            isDefault = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))

            // Specifies a sorted list of fallback build types that the
            // plugin should try to use when a dependency does not include a
            // "staging" build type.
            // Used with :test-shared, which doesn't have a staging variant.
            matchingFallbacks.add("debug")
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }

    testBuildType = "staging"

    // To avoid the compile error from benchmarkRule.measureRepeated
    // Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM
    // target 1.6
    kotlinOptions.jvmTarget = "1.8"

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    androidTestImplementation(project(":model"))
    androidTestImplementation(project(":shared"))
    androidTestImplementation(project(":test-shared"))
    androidTestImplementation(project(":androidTest-shared"))

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

//    // ThreeTenBP is for Date and time API for Java.
//    androidTestImplementation("com.jakewharton.threetenabp:threetenabp:1.3.1")

    // Instrumentation tests
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:rules:1.3.0")

    // Benchmark testing
    androidTestImplementation("androidx.benchmark:benchmark-junit4:1.0.0")
}
