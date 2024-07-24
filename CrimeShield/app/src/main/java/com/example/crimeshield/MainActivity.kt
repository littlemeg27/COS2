package com.example.crimeshield

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.crimeshield.ui.theme.CrimeShieldTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
)

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent {
            CrimeShieldTheme{

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background)
                {
                    GreetingPreview()

                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "home")
                    {
                        composable("home") { HomeScreen(navController) }
                        composable("details") { MapScreen(navController) }
                        composable("create") { CreateScreen(navController) }
                        composable("settings") { SettingsScreen(navController) }
                    }
                }
            }
        }
    }
}

/*@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {

    //Information for UI
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Map",
            selectedIcon = Icons.Filled.LocationOn,
            unselectedIcon = Icons.Outlined.LocationOn,
            hasNews = true
        ),
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.AddCircle,
            unselectedIcon = Icons.Outlined.AddCircle,
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            hasNews = false
        )
    )

    var selectedItemIndex by rememberSaveable()
    {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    //Information for UI


        Scaffold(
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        val isSelected = item.title.lowercase() == navBackStackEntry?.destination?.route

                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                navController.navigate(item.title.lowercase())
                                {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = {
                                Text(text = item.title)
                            },
                            alwaysShowLabel = false,
                            icon =
                            {
                                Icon(
                                    imageVector = if(isSelected)
                                    {
                                        item.selectedIcon
                                    }
                                    else item.unselectedIcon,
                                    contentDescription = item.title
                                )

                                BadgedBox(
                                    badge =
                                    {
                                        if (item.hasNews)
                                        {
                                            Badge {
                                                Badge()
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex)
                                        {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ) {
            padding ->
            NavHost(navController, startDestination = "home")
            {
                composable("home")
                {
                    HomeScreen(text = "")
                }
                composable("map")
                {
                    MapScreen(text = "")
                }
                composable("create")
                {
                    CreateScreen(text = "")
                }
                composable("settings")
                {
                    SettingsScreen(text = "")
                }
            }
        }
    }
}
//Home Screen
*/

/*
@Composable
fun HomeScreen(
    text: String,
) {
    val homeNavController = rememberNavController()
    NavHost(homeNavController, startDestination = "home1")
    {

    }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Text(text = text)
            Spacer(Modifier.height(16.dp))
        }
}


@Composable
fun MapScreen(
    text: String,
)
{
    val mapNavController = rememberNavController()
    NavHost(mapNavController, startDestination = "map1")
    {

    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )   {
        Text(text = text)
        Spacer(Modifier.height(16.dp))
        }
}

@Composable
fun CreateScreen(
    text: String,
)
{
    val createNavController = rememberNavController()
    NavHost(createNavController, startDestination = "create1")
    {

    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )   {
        Text(text = text)
        Spacer(Modifier.height(16.dp))
        }
}

@Composable
fun SettingsScreen(
    text: String,
)
{
    val settingsNavController = rememberNavController()
    NavHost(settingsNavController, startDestination = "settings1")
    {

    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )   {
        Text(text = text)
        Spacer(Modifier.height(16.dp))
        }
}*/

@Composable
fun HomeScreen(navController: NavController)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Greeting("World")
        Button(onClick = { navController.navigate("details") })
        {
            Text(text = "Go to Details")
        }
        Text(
            text = "CRIME SHIELD",
            fontSize = 30.sp
        )
        Image(
            painter = painterResource(id = R.drawable.crimeshieldicon),
            contentDescription = "Icon"
        )
        Text(
            text = "Create a Report!",
            fontSize = 15.sp
        )
        Button(
            modifier = Modifier
                .height(50.dp)
                .width(190.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            onClick = { }
        )
        {
            Text(text = "Create a Report!")
        }
        Image(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "Map"
      )}
}

@Composable
fun MapScreen(navController: NavController)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Map Screen")
        Button(onClick = { navController.navigate("home") })
        {
            Text(text = "Back to Home")
        }
    }
}

@Composable
fun CreateScreen(navController: NavController)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Create Screen")
        Button(onClick = { navController.navigate("home") })
        {
            Text(text = "Back to Home")
        }
    }
}

@Composable
fun SettingsScreen(navController: NavController)
{
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Settings Screen")
        Button(onClick = { navController.navigate("home") })
        {
            Text(text = "Back to Home")
        }
    }
}


//To show the App
@Preview(showBackground = true)
@Composable
fun GreetingPreview()
{
    CrimeShieldTheme {
        GreetingPreview("Android")
    }
}
