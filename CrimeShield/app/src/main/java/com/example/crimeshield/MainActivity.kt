package com.example.crimeshield

import android.media.Image
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.crimeshield.ui.theme.CrimeShieldTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrimeShieldTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Immutable
data class PixelAlignment (
    val offsetX: Float,
    val offsetY: Float
) : Alignment {

    override fun align(size: IntSize, space: IntSize, layoutDirection: LayoutDirection): IntOffset {
        val centerX = (space.width - size.width).toFloat() / 2f
        val centerY = (space.height - size.height).toFloat() / 2f

        val x = centerX + offsetX
        val y = centerY + offsetY

        return IntOffset(x.roundToInt(), y.roundToInt())
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Text(
            text = "CRIME SHIELD",
            fontSize = 30.sp)
        Image(painter = painterResource(id = R.drawable.crimeshieldicon),
            contentDescription = "Icon")
        Text(
            text = "Create a Report!",
            fontSize = 15.sp)
        Button(
            modifier = Modifier.height(50.dp).width(190.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            onClick = { }
        )
        {
            Text(text = "Create a Report!")
        }
        Image(painter = painterResource(id = R.drawable.map),
            contentDescription = "Map")

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CrimeShieldTheme {
        Greeting("Android")
    }
}