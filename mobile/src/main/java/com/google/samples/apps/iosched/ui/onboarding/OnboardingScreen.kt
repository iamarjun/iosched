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

package com.google.samples.apps.iosched.ui.onboarding

import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.samples.apps.iosched.shared.util.TimeUtils
import com.google.samples.apps.iosched.ui.MainActivity
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun OnboardingScreen(
    navController: NavHostController,
    viewModel: OnboardingViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    Scaffold(
        scaffoldState = scaffoldState
    ) {
        val modifier = Modifier.padding(it)
        val context = LocalContext.current
        val pages = remember {
            screens()
        }
        Timber.d("OnboardingScreen: ${pages.size}")

        LaunchedEffect(key1 = viewModel.navigationActions) {
            viewModel.navigationActions.collect { action ->
                if (action == OnboardingNavigationAction.NavigateToMainScreen) {
                    navController.navigate(com.google.samples.apps.iosched.ui.Screen.LaunchScreen.Main.route)
                }
            }
        }

        val pagerState = rememberPagerState(
            pageCount = pages.size,
        )
        Column(
            modifier = modifier
                .padding(32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            HorizontalPager(
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = pagerState
            ) { page ->
                Timber.d("OnboardingScreen Current Page: $page")
                when (pages[page]) {
                    Screen.OnboardingSignInScreen -> OnboardingSignInScreen()
                    Screen.WelcomeDuringConferenceScreen -> WelcomeDuringConferenceScreen()
                    Screen.WelcomePostConferenceScreen -> WelcomePostConferenceScreen()
                    Screen.WelcomePreConferenceScreen -> WelcomePreConferenceScreen()
                }
            }


            Button(
                modifier = modifier.padding(16.dp),
                onClick = {
                    viewModel.getStartedClick()
                },
            ) {
                Text(text = "Get Started")
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
            )
        }
    }

}


private fun screens(): Array<Screen> {
    return if (!TimeUtils.conferenceHasStarted()) {
        // Before the conference
        arrayOf(
            Screen.WelcomePreConferenceScreen,
            Screen.OnboardingSignInScreen
        )
    } else if (TimeUtils.conferenceHasStarted() && !TimeUtils.conferenceHasEnded()) {
        // During the conference
        arrayOf(
            Screen.WelcomeDuringConferenceScreen,
            Screen.OnboardingSignInScreen
        )
    } else {
        // Post the conference
        arrayOf(
            Screen.WelcomePostConferenceScreen
        )
    }
}

sealed class Screen {
    object WelcomePreConferenceScreen : Screen()
    object WelcomeDuringConferenceScreen : Screen()
    object WelcomePostConferenceScreen : Screen()
    object OnboardingSignInScreen : Screen()
}