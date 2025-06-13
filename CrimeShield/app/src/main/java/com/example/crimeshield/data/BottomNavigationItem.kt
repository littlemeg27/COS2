package com.example.crimeshield.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.CrisisAlert
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
)

val items = listOf(
    BottomNavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home, false),
    BottomNavigationItem("Map", Icons.Filled.LocationOn, Icons.Outlined.LocationOn, true),
    BottomNavigationItem("Create", Icons.Filled.AddCircle, Icons.Outlined.AddCircle, false),
    BottomNavigationItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, false),
    BottomNavigationItem("Sent Reports", Icons.Filled.Checklist, Icons.Outlined.Checklist, false),
    BottomNavigationItem("News", Icons.Filled.Newspaper, Icons.Outlined.Newspaper, true),
    BottomNavigationItem("Missing", Icons.Filled.CrisisAlert, Icons.Outlined.CrisisAlert, false),
    BottomNavigationItem("Sex Offenders", Icons.Filled.Search, Icons.Outlined.Search, false)
)
