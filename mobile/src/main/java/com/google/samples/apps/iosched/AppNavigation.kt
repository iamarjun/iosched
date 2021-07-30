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

package com.google.samples.apps.iosched

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.samples.apps.iosched.model.SessionId
import com.google.samples.apps.iosched.model.SpeakerId
import com.google.samples.apps.iosched.ui.LauncherScreen
import com.google.samples.apps.iosched.ui.MainScreen
import com.google.samples.apps.iosched.ui.agenda.AgendaScreen
import com.google.samples.apps.iosched.ui.codelabs.CodelabsScreen
import com.google.samples.apps.iosched.ui.onboarding.*
import com.google.samples.apps.iosched.ui.schedule.ScheduleScreen
import com.google.samples.apps.iosched.ui.sessiondetail.SessionDetailScreen
import com.google.samples.apps.iosched.ui.settings.SettingsScreen
import com.google.samples.apps.iosched.ui.speaker.SpeakerScreen

sealed class Screen(val route: String) {
    object Launcher : Screen("launcherroot")
    object Main : Screen("mainroot")

    sealed class BottomNavScreen(route: String, val icon: Int, val title: String) : Screen(route) {
        object Schedule : BottomNavScreen("scheduleroot", R.drawable.ic_nav_schedule, "Schedule")
        object Agenda : BottomNavScreen("agendaroot", R.drawable.ic_nav_agenda, "Agenda")
        object CodeLabs : BottomNavScreen("codelabsroot", R.drawable.ic_nav_codelabs, "CodeLabs")
        object Maps : BottomNavScreen("mapsroot", R.drawable.ic_nav_map, "Maps")
        object Settings : BottomNavScreen("settingsroot", R.drawable.ic_nav_settings, "Settings")
    }
}

sealed class LeafScreen(val route: String) {
    object Launcher : LeafScreen("launcher")
    object Main : LeafScreen("main")
    object OnBoarding : LeafScreen("onboarding")

    object Schedule : LeafScreen("schedule")

    object SessionDetail : LeafScreen("session_detail/{sessionId}") {
        fun createRoute(sessionId: SessionId) = "session_detail/$sessionId"
    }

    object SpeakerDetail : LeafScreen("speaker/{speaker_id}") {
        fun createRoute(speakerId: SpeakerId) = "speaker/$speakerId"
    }

    object Agenda : LeafScreen("agenda")

    object CodeLabs : LeafScreen("codelabs")

    object Maps : LeafScreen("maps")

    object Settings : LeafScreen("settings")

    object OnboardingSignIn : LeafScreen("onboarding_sign_in")
    object WelcomeDuringConference : LeafScreen("welcome_during_conference")
    object WelcomePostConference : LeafScreen("welcome_post_conference")
    object WelcomePreConferenceS : LeafScreen("welcome_pre_conferences")
}

val bottomNavItems = listOf(
    Screen.BottomNavScreen.Schedule,
    Screen.BottomNavScreen.Agenda,
    Screen.BottomNavScreen.CodeLabs,
    Screen.BottomNavScreen.Settings,
)
val bottomNavItemsRoutes = listOf(
    Screen.BottomNavScreen.Schedule.route,
    Screen.BottomNavScreen.Agenda.route,
    Screen.BottomNavScreen.CodeLabs.route,
    Screen.BottomNavScreen.Settings.route,
)


@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun AppNavigation(navController: NavController) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = LeafScreen.Main.route
    ) {

        composable(route = LeafScreen.Main.route) {
            MainScreen()
        }
        composable(route = LeafScreen.OnBoarding.route) {
            OnboardingScreen(
                openMainScreen = {
                    navController.navigate(LeafScreen.Main.route) {
                        popUpTo(LeafScreen.Main.route) {
                            inclusive = true
                        }
                    }
                },
                onSignInClick = {}
            )
        }
        composable(route = LeafScreen.OnboardingSignIn.route) {
            OnboardingSignInScreen(
                onSignInClick = {

                }
            )
        }
        composable(route = LeafScreen.WelcomeDuringConference.route) {
            WelcomeDuringConferenceScreen(
                onSignInClick = {}
            )
        }
        composable(route = LeafScreen.WelcomePostConference.route) {
            WelcomePostConferenceScreen()
        }
        composable(route = LeafScreen.WelcomePreConferenceS.route) {
            WelcomePreConferenceScreen(
                onSignInClick = {}
            )
        }

    }
}

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun BottomNavigations(navController: NavController, modifier: Modifier) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = LeafScreen.Schedule.route,
        modifier = modifier
    ) {

        composable(route = LeafScreen.Schedule.route) {
            ScheduleScreen(
                openSessionDetailScreen = {
                    navController.navigate(LeafScreen.SessionDetail.createRoute(it))
                }
            )
        }

        ScheduleTopLevel(navController)
        AgendaTopLevel(navController)
        CodeLabsTopLevel(navController)
        SettingsTopLevel(navController)
    }
}

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
private fun NavGraphBuilder.LauncherTopLevel(
    navController: NavController,
) {
    navigation(
        route = Screen.Launcher.route,
        startDestination = LeafScreen.Launcher.route
    ) {
        composable(route = LeafScreen.Launcher.route) {
            LauncherScreen(
                openMainScreen = {
                    navController.navigate(
                        LeafScreen.Main.route
                    )
                },
                openOnBoardingScreen = {
                    navController.navigate(
                        LeafScreen.OnBoarding.route
                    )
                }
            )
        }

//        composable(route = LeafScreen.Main.route) {
//            MainScreen(navController = navController)
//        }

    }
}

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
fun NavGraphBuilder.ScheduleTopLevel(
    navController: NavController,
) {
    navigation(
        route = Screen.BottomNavScreen.Schedule.route,
        startDestination = LeafScreen.Schedule.route
    ) {

        composable(route = LeafScreen.Schedule.route) {
            ScheduleScreen(
                openSessionDetailScreen = {
                    navController.navigate(LeafScreen.SessionDetail.createRoute(it))
                }
            )
        }

        composable(route = LeafScreen.SessionDetail.route) {
            val sessionId = it.arguments?.get("sessionId") as SessionId
            SessionDetailScreen(
                sessionId = sessionId,
                onBackPress = {
                    navController.popBackStack()
                },
                openSpeakerDetailScreen = { speakerId ->
                    navController.navigate(LeafScreen.SpeakerDetail.createRoute(speakerId))
                },
                openFeedbackDialog = { /*TODO*/ },
                openSignInDialog = { /*TODO*/ },
                openSwapReservationDialog = { /*TODO*/ },
                openYoutubeUrl = { /*TODO*/ },
                openRemoveReservationDialog = { /*TODO*/ },
                openNotificationsPreferenceDialog = {/*TODO*/ },
            )
        }

        composable(route = LeafScreen.SpeakerDetail.route) {
            val speakerId = it.arguments?.get("speaker_id") as SpeakerId
            SpeakerScreen(
                speakerId = speakerId,
                onBackPress = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
fun NavGraphBuilder.AgendaTopLevel(
    navController: NavController,
) {
    navigation(
        route = Screen.BottomNavScreen.Agenda.route,
        startDestination = LeafScreen.Agenda.route
    ) {

        composable(route = LeafScreen.Agenda.route) {
            AgendaScreen(navController = navController)
        }
    }
}

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
fun NavGraphBuilder.CodeLabsTopLevel(
    navController: NavController,
) {
    navigation(
        route = Screen.BottomNavScreen.CodeLabs.route,
        startDestination = LeafScreen.CodeLabs.route
    ) {

        composable(route = LeafScreen.CodeLabs.route) {
            CodelabsScreen(navController = navController)
        }
    }
}

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
fun NavGraphBuilder.SettingsTopLevel(
    navController: NavController,
) {
    navigation(
        route = Screen.BottomNavScreen.Settings.route,
        startDestination = LeafScreen.Settings.route
    ) {

        composable(route = LeafScreen.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}

