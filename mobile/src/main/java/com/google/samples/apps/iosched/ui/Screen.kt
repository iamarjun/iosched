/*
 * Copyright 2021 Google LLC
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

package com.google.samples.apps.iosched.ui

import androidx.annotation.DrawableRes
import com.google.samples.apps.iosched.R

sealed class Screen(
    val route: String,
) {

    sealed class LaunchScreen(route: String) : Screen(route) {

        object Launch : LaunchScreen(
            route = "launch",
        )

        object Main : LaunchScreen(
            route = "main",
        )

        object Onboarding : LaunchScreen(
            route = "onboarding",
        )
    }

    sealed class BottomNavScreen(
        route: String,
        val title: String,
        @DrawableRes val icon: Int
    ) : Screen(route) {

        object Schedule : BottomNavScreen(
            route = "schedule",
            title = "Schedule",
            icon = R.drawable.ic_nav_schedule
        )

        object ScheduleDetail : BottomNavScreen(
            route = "schedule_detail",
            title = "Schedule Detail",
            icon = R.drawable.ic_nav_schedule
        )

        object Agenda : BottomNavScreen(
            route = "agenda",
            title = "Agenda",
            icon = R.drawable.ic_nav_agenda
        )

        object Codelabs : BottomNavScreen(
            route = "codelabs",
            title = "Codelabs",
            icon = R.drawable.ic_nav_codelabs
        )

        object Maps : BottomNavScreen(
            route = "maps",
            title = "Maps",
            icon = R.drawable.ic_nav_map
        )

        object Settings : BottomNavScreen(
            route = "settings",
            title = "Settings",
            icon = R.drawable.ic_nav_settings
        )
    }
}

