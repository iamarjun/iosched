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

package com.google.samples.apps.iosched.ui.schedule

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsPadding
import com.google.samples.apps.iosched.R
import com.google.samples.apps.iosched.model.Tag
import com.google.samples.apps.iosched.model.userdata.UserSession
import com.google.samples.apps.iosched.shared.util.TimeUtils
import com.google.samples.apps.iosched.ui.MainActivityViewModel
import com.google.samples.apps.iosched.ui.Screen
import com.google.samples.apps.iosched.ui.theme.Black
import com.google.samples.apps.iosched.ui.theme.Teal
import com.google.samples.apps.iosched.ui.theme.Transparent
import com.google.samples.apps.iosched.ui.theme.White
import kotlinx.coroutines.flow.collect
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

private val timeFormatter = DateTimeFormatter.ofPattern("h:mm")
private val meridiemFormatter = DateTimeFormatter.ofPattern("a")

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ScheduleScreen(
    navController: NavHostController,
    viewModel: ScheduleViewModel = hiltViewModel(),
    mainViewModel: MainActivityViewModel = hiltViewModel(),
    scheduleTwoPaneViewModel: ScheduleTwoPaneViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val scheduleUiData by viewModel.scheduleUiData.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val days by viewModel.indicators.collectAsState()

    LaunchedEffect(key1 = scheduleTwoPaneViewModel.selectSessionEvents) {
        scheduleTwoPaneViewModel.selectSessionEvents.collect { sessionId ->
            navController.navigate("${Screen.BottomNavScreen.ScheduleDetail.route}/$sessionId")
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    LazyRow {
                        items(days) { item ->
                            Surface(
                                color = if (item.checked) Teal else Transparent,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = stringResource(
                                        id = TimeUtils.getShortLabelResForDay(
                                            item.day
                                        )
                                    ),
                                    modifier = Modifier
                                        .padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        ),
                                    style = TextStyle(
                                        color = if (item.checked) White else Black,
                                    )
                                )
                            }

                        }
                    }
                },
                actions = {

                    IconButton(onClick = { mainViewModel.onProfileClicked() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_default_profile_avatar),
                            contentDescription = "Profile",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
                backgroundColor = White,
                contentColor = Black,
                elevation = 8.dp,
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) {

        val modifier = Modifier.padding(it)

        if (loading)
            CircularProgressIndicator()
        else
            Schedules(
                modifier = modifier,
                schedules = scheduleUiData.list!!,
                zoneId = scheduleUiData.timeZoneId!!,
                onDayChange = viewModel::onDayChange,
                onStarClick = scheduleTwoPaneViewModel::onStarClicked,
                openEventDetail = scheduleTwoPaneViewModel::openEventDetail
            )
    }

}


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
private fun Schedules(
    modifier: Modifier,
    schedules: List<UserSession>,
    zoneId: ZoneId,
    onDayChange: (ZonedDateTime) -> Unit,
    onStarClick: (UserSession) -> Unit,
    openEventDetail: (String) -> Unit,
    state: LazyListState = rememberLazyListState()
) {

    LazyColumn(
        state = state,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        val isConferenceTimeZone = TimeUtils.isConferenceTimeZone(zoneId = zoneId)

        val days = schedules.groupBy {
            TimeUtils.getLabelResForTime(it.session.startTime)
        }

        days.forEach { (day, sessions) ->
            item {
                ScheduleHeader(
                    modifier = modifier,
                    day = day,
                    onDraw = onDayChange
                )
            }

            val userSession = sessions.groupBy {
                TimeUtils.zonedTime(it.session.startTime, zoneId).truncatedTo(ChronoUnit.MINUTES)
            }

            userSession.forEach { (startTime, list) ->

                stickyHeader {
                    Column(
                        modifier = modifier.width(84.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = timeFormatter.format(startTime),
                            style = MaterialTheme.typography.h6.copy(
                                color = MaterialTheme.colors.primary,
                            )
                        )
                        Text(
                            text = meridiemFormatter.format(startTime)
                                .uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.body2.copy(
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                }

                items(list) { session ->
                    Schedule(
                        modifier = modifier,
                        userSession = session,
                        zoneId = zoneId,
                        onStarClick = onStarClick,
                        openEventDetail = openEventDetail,
                    )
                }

            }
        }
    }

}

@Composable
private fun ScheduleHeader(
    modifier: Modifier,
    day: ZonedDateTime,
    onDraw: (ZonedDateTime) -> Unit
) {
    Text(
        modifier = modifier
            .padding(top = 32.dp, bottom = 16.dp)
            .fillMaxWidth(),
        text = TimeUtils.dateString(day),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h6
    )
    onDraw(day)
}


@ExperimentalComposeUiApi
@Composable
private fun Schedule(
    modifier: Modifier,
    userSession: UserSession,
    zoneId: ZoneId?,
    onStarClick: (UserSession) -> Unit,
    openEventDetail: (String) -> Unit,
) {
    Row(
        modifier = modifier.clickable { openEventDetail(userSession.session.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .padding(start = 84.dp, end = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = userSession.session.title,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.W600)
            )
            Row(
                modifier = modifier
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_livestreamed),
                    contentDescription = null,
                    tint = Color.Gray,
                )
                Spacer(modifier = modifier.width(10.dp))
                Text(
                    text = sessionDateTimeLocation(
                        userSession.session.startTime,
                        zoneId,
                        false,
                        userSession.session.room
                    ),
                    style = MaterialTheme.typography.subtitle2.copy(
                        color = Color.Gray
                    )
                )
            }
            Row(
                modifier = modifier.horizontalScroll(rememberScrollState(), true)
            ) {
                userSession.session.displayTags.forEach {
                    Tag(
                        modifier = modifier,
                        tag = it
                    )
                }
            }
        }
        IconButton(
            modifier = modifier,
            onClick = {
                onStarClick(userSession)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star_border),
                contentDescription = null,
                tint = Color.Gray,
                modifier = modifier.padding(end = 16.dp)
            )
        }
    }

}

@Composable
private fun Tag(
    modifier: Modifier,
    tag: Tag
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .wrapContentSize(Alignment.Center)
                .clip(CircleShape)
                .size(10.dp)
                .background(Color(tag.color))
        )
        Text(
            text = tag.displayName,
            style = MaterialTheme.typography.subtitle2.copy(
                color = Color.Gray
            )
        )
        Spacer(modifier = modifier.width(10.dp))
    }
}