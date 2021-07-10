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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.samples.apps.iosched.ui.onboarding.OnboardingScreen
import com.google.samples.apps.iosched.ui.theme.IOTheme
import kotlinx.coroutines.flow.collect

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun LauncherScreen(
    navController: NavHostController = rememberNavController(),
    launchViewModel: LaunchViewModel = hiltViewModel()
) {

    IOTheme {
        Scaffold {
            LauncherNav(navController = navController)
            LaunchedEffect(key1 = launchViewModel.launchDestination) {
                launchViewModel.launchDestination.collect { action ->
                    when (action) {
                        is LaunchNavigatonAction.NavigateToMainActivityAction -> navController.navigate(Screen.LaunchScreen.Main.route)

                        is LaunchNavigatonAction.NavigateToOnboardingAction -> navController.navigate(Screen.LaunchScreen.Onboarding.route)
                    }
                }
            }
        }
    }

}


@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
private fun LauncherNav(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.LaunchScreen.Launch.route) {

        composable(Screen.LaunchScreen.Launch.route) {
            LauncherScreen()
        }

        composable(Screen.LaunchScreen.Main.route) {
            MainScreen()
        }

        composable(Screen.LaunchScreen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }

    }

}