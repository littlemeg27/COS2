package com.example.crimeshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.crimeshield.ui.theme.CrimeShieldTheme

class MainActivity : ComponentActivity()
{

    private lateinit var binding : MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        setContent
        {
            CrimeShieldTheme
            {
                binding.bottomNavigationView.setOnItemSelectedListener
                {

                    when(it.itemId)
                    {
                        R.id.home -> replaceFragment(HomeFragment())
                        R.id.map -> replaceFragment(MapFragment())
                        R.id.create -> replaceFragment(CreateFragment())
                        R.id.settings -> replaceFragment(SettingsFragment())

                        else ->
                        {

                        }
                    }
                    true
                }

            }
            private fun replaceFragment(fragment : Fragment)
            {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_layout,fragment)
                fragmentTransaction.commit()
            }
        }
    }
}






@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CrimeShieldTheme {
        Greeting("Android")
    }
}