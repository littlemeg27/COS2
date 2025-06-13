package com.example.crimeshield.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.crimeshield.R
import com.example.crimeshield.data.items
import com.google.maps.android.compose.*

@Composable
fun HomeScreen(navController: NavController)
{
    val selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentDestination?.hierarchy?.any { it.route == item.title.lowercase() } == true) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.title
                                )
                            }
                        },
                        label = { Text(text = item.title) },
                        alwaysShowLabel = false,
                        selected = currentDestination?.hierarchy?.any { it.route == item.title.lowercase() } == true,
                        onClick = {
                            navController.navigate(item.title.lowercase()) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "CRIME SHIELD",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.crimeshieldicon),
                contentDescription = "Icon",
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = "Create a Report!",
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            Button(
                modifier = Modifier
                    .height(70.dp)
                    .width(190.dp)
                    .padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                onClick = { navController.navigate("create") }
            ) {
                Text(text = "Create a Report!")
            }
            var uiSettings by remember { mutableStateOf(MapUiSettings()) }
            var properties by remember { mutableStateOf(MapProperties(mapType = MapType.SATELLITE)) }

            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, bottom = 80.dp),
                properties = properties,
                uiSettings = uiSettings
            )
        }
    }
}