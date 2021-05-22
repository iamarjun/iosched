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
    id("java-platform")
    id("maven-publish")
}

// val appcompat = "1.1.0"
// val activity = "1.2.0-rc01"
// val activityCompose = "1.3.0-alpha07"
// val cardview = "1.0.0"
// val archTesting = "2.0.0"
// val arcore = "1.7.0"
// val benchmark = "1.0.0"
// val browser = "1.0.0"
// val compose = Versions.COMPOSE
// val constraintLayout = "1.1.3"
// val core = "1.3.2"
// val coroutines = "1.4.2"
// val coroutinesTest = "1.3.4"
// val crashlytics = "17.2.2"
// val dataStore = "1.0.0-beta01"
// val drawerLayout = "1.1.0-rc01"
// val espresso = "3.1.1"
// val firebaseAnalytics = "17.4.0"
// val firebaseAuth = "19.3.1"
// val firebaseConfig = "19.1.4"
// val firebaseFirestore = "21.4.3"
// val firebaseFunctions = "19.0.2"
// val firebaseMessaging = "20.1.6"
// val firebaseUi = "4.0.0"
// val flexbox = "1.1.0"
// val fragment = "1.3.0"
// val glide = "4.9.0"
// val googlePlayServicesMapsKtx = "3.0.0"
// val googlePlayServicesVision = "17.0.2"
// val gson = "2.8.6"
// val hamcrest = "1.3"
// val hilt = Versions.HILT_AGP
// val junit = "4.13"
// val junitExt = "1.1.1"
// val lifecycle = "2.4.0-alpha01"
// val lottie = "3.0.0"
// val material = "1.4.0-beta01"
// val mockito = "3.3.1"
// val mockitoKotlin = "1.5.0"
// val okhttp = "3.10.0"
// val okio = "1.14.0"
// val pageIndicator = "1.3.0"
// val playCore = "1.6.5"
// val room = "2.2.5"
// val rules = "1.1.1"
// val runner = "1.2.0"
// val slidingpanelayout = "1.2.0-alpha01"
// val threetenabp = "1.0.5"
// val timber = "4.7.1"
// val viewpager2 = "1.0.0"
// val viewModelCompose = "1.0.0-alpha04"

dependencies {
    constraints {
//        api("${"androidx.activity:activity-compose:1.3.0-alpha07"}:$activityCompose")
//        api("${"androidx.activity:activity-ktx:1.2.0-rc01"}:$activity")
//        api("${"androidx.appcompat:appcompat:1.1.0"}:$appcompat")
//        api("${"androidx.cardview:cardview:1.0.0"}:$cardview")
//        api("${"androidx.arch.core:core-testing:2.0.0"}:$archTesting")
//        api("${"com.google.ar:core:1.7.0"}:$arcore")
//        api("${"androidx.benchmark:benchmark-junit4:1.0.0"}:$benchmark")
//        api("${"androidx.browser:browser:1.0.0"}:$browser")
//        api("${"androidx.compose.animation:animation:1.0.0-beta07"}:$compose")
//        api("${"androidx.compose.material:material:1.0.0-beta07"}:$compose")
//        api("${"androidx.compose.runtime:runtime:1.0.0-beta07"}:$compose")
//        api("${"androidx.compose.ui:ui-test-junit4:1.0.0-beta07"}:$compose")
//        api("${"com.google.android.material:compose-theme-adapter:1.0.0-beta07"}:$compose")
//        api("${"androidx.compose.ui:ui-tooling:1.0.0-beta07"}:$compose")
//        api("${"androidx.constraintlayout:constraintlayout:1.1.3"}:$constraintLayout")
//        api("${"androidx.core:core-ktx:1.3.2"}:$core")
//        api("${"org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2"}:$coroutines")
//        api("${"org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2"}:$coroutines")
//        api("${"com.google.firebase:firebase-crashlytics:17.2.2"}:$crashlytics")
//        api("${"androidx.datastore:datastore-preferences:1.0.0-beta01"}:$dataStore")
//        api("${"androidx.drawerlayout:drawerlayout:1.1.0-rc01"}:$drawerLayout")
//        api("${"androidx.test.espresso:espresso-core:3.1.1"}:$espresso")
//        api("${"androidx.test.espresso:espresso-contrib:3.1.1"}:$espresso")
//        api("${"com.google.firebase:firebase-auth-ktx:19.3.1"}:$firebaseAuth")
//        api("${"com.google.firebase:firebase-config-ktx:19.1.4"}:$firebaseConfig")
//        api("${"com.google.firebase:firebase-analytics-ktx:17.4.0"}:$firebaseAnalytics")
//        api("${"com.google.firebase:firebase-firestore-ktx:21.4.3"}:$firebaseFirestore")
//        api("${"com.google.firebase:firebase-functions-ktx:19.0.2"}:$firebaseFunctions")
//        api("${"com.google.firebase:firebase-messaging:20.1.6"}:$firebaseMessaging")
//        api("${"com.firebaseui:firebase-ui-auth:4.0.0"}:$firebaseUi")
//        api("${"com.google.android:flexbox:1.1.0"}:$flexbox")
//        api("${"androidx.fragment:fragment-ktx:1.3.0"}:$fragment")
//        api("${"androidx.fragment:fragment-testing:1.3.0"}:$fragment")
//        api("${"com.github.bumptech.glide:glide:4.9.0"}:$glide")
//        api("${"com.github.bumptech.glide:compiler:4.9.0"}:$glide")
//        api("${"com.google.maps.android:maps-utils-ktx:3.0.0"}:$googlePlayServicesMapsKtx")
//        api("${"com.google.maps.android:maps-ktx:3.0.0"}:$googlePlayServicesMapsKtx")
//        api("${"com.google.android.gms:play-services-vision:17.0.2"}:$googlePlayServicesVision")
//        api("${"com.google.code.gson:gson:2.8.6"}:$gson")
//        api("${"org.hamcrest:hamcrest-library:1.3"}:$hamcrest")
//        api("${"com.google.dagger:hilt-android:2.35.1"}:$hilt")
//        api("${"com.google.dagger:hilt-android-compiler:2.35.1"}:$hilt")
//        api("${"com.google.dagger:hilt-android-testing:2.35.1"}:$hilt")
//        api("${"junit:junit:4.13"}:$junit")
//        api("${"androidx.test.ext:junit:1.1.1"}:$junitExt")
//        api("${"org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.32"}:${Versions.KOTLIN}")
//        api("${"androidx.lifecycle:lifecycle-compiler:2.4.0-alpha01"}:$lifecycle")
//        api("${"androidx.lifecycle:lifecycle-livedata-ktx:2.4.0-alpha01"}:$lifecycle")
//        api("${"androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01"}:$lifecycle")
//        api("${"androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0-alpha01"}:$lifecycle")
//        api("${"com.airbnb.android:lottie:3.0.0"}:$lottie")
//        api("${"com.google.android.material:material:1.4.0-beta01"}:$material")
//        api("${"com.google.android.material:compose-theme-adapter:1.0.0-beta07"}:$compose")
//        api("${"org.mockito:mockito-core:3.3.1"}:$mockito")
//        api("${"com.nhaarman:mockito-kotlin:1.5.0"}:$mockitoKotlin")
//        api("${"androidx.navigation:navigation-fragment-ktx:2.3.4"}:${Versions.NAVIGATION}")
//        api("${"androidx.navigation:navigation-ui-ktx:2.3.4"}:${Versions.NAVIGATION}")
//        api("${"androidx.room:room-ktx:2.2.5"}:$room")
//        api("${"androidx.room:room-runtime:2.2.5"}:$room")
//        api("${"androidx.room:room-compiler:2.2.5"}:$room")
//        api("${"com.squareup.okhttp3:okhttp:3.10.0"}:$okhttp")
//        api("${"com.squareup.okhttp3:logging-interceptor:3.10.0"}:$okhttp")
//        api("${"com.squareup.okio:okio:1.14.0"}:$okio")
//        api("${"com.pacioianu.david:ink-page-indicator:1.3.0"}:$pageIndicator")
//        api("${"androidx.test:rules:1.1.1"}:$rules")
//        api("${"androidx.test:runner:1.1.1"}:$runner")
//        api("${"androidx.slidingpanelayout:slidingpanelayout:1.2.0-alpha01"}:$slidingpanelayout")
//        api("${"com.jakewharton.threetenabp:threetenabp:1.0.5"}:$threetenabp")
//        api("${"org.threeten:threetenbp:1.0.5"}:${Versions.THREETENBP}")
//        api("${"com.jakewharton.timber:timber:4.7.1"}:$timber")
//        api("${Libs.VIEWPAGER2}:$viewpager2")
//        api("${"androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha04"}:$viewModelCompose")
    }
}

publishing {
    publications {
        create<MavenPublication>("myPlatform") {
            from(components["javaPlatform"])
        }
    }
}
