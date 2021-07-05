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
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.samples.apps.iosched.ui.agenda.AgendaScreen
import com.google.samples.apps.iosched.ui.codelabs.CodelabsScreen
import com.google.samples.apps.iosched.ui.schedule.ScheduleScreen
import com.google.samples.apps.iosched.ui.settings.SettingsScreen
import com.google.samples.apps.iosched.ui.theme.DeepSkyBlue
import com.google.samples.apps.iosched.ui.theme.IOTheme
import com.google.samples.apps.iosched.ui.theme.White

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun MainScreen(
    state: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController()
) {
    IOTheme {
        Scaffold(
            scaffoldState = state,
            bottomBar = {
                BottomBar(navController = navController)
            }
        ) {
            BottomBarMain(navController = navController)
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
private fun BottomBarMain(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Schedule.route) {
        composable(Screen.Schedule.route) {
            ScheduleScreen()
        }
        composable(Screen.Agenda.route) {
            AgendaScreen()
        }

        composable(Screen.Codelabs.route) {
            CodelabsScreen()
        }

//        composable(Screen.Maps.route) {
//            Maps()
//        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

    }
}


@Composable
fun BottomBar(navController: NavController) {

    val items = listOf(
        Screen.Schedule,
        Screen.Agenda,
        Screen.Codelabs,
        Screen.Settings,
    )

    BottomNavigation(
        elevation = 5.dp,
        backgroundColor = White,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.map {
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.title
                    )
                },
                label = {
                    Text(
                        text = it.title
                    )
                },
                selected = currentRoute == it.route,
                selectedContentColor = DeepSkyBlue,
                unselectedContentColor = Color.Gray,
                onClick = {
                    navController.navigate(it.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}