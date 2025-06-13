package com.example.crimeshield.data

import androidx.annotation.StringRes
import com.example.crimeshield.R

sealed class Screen(val route: String, @StringRes val resourceId: Int)
{
    data object Home : Screen("home", R.string.homeScreen)
    data object Map : Screen("map", R.string.mapScreen)
    data object Create : Screen("create", R.string.createScreen)
    data object Settings : Screen("settings", R.string.settingsScreen)
    data object SentReports : Screen("sentreports", R.string.sentReportsScreen)
    data object News : Screen("news", R.string.newsScreen)
    data object Missing : Screen("missing", R.string.missingScreen)
    data object SexOffenders : Screen("sexoffenders", R.string.sexOffendersScreen)
}