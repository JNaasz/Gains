package com.example.gains

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gains.ui.theme.GainsTheme
import com.example.gains.ui.theme.medium

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GainsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // Color(0xFF6650a4)
                )
                {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ProteinGoal(88, 130)
                        Steps(84444, 10000)
                    }
                }
            }
        }
    }
}

@Composable
fun ProteinGoal(current: Int, goal: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .requiredSize(100.dp) // Enforces the size to not stretch
            .background(color = medium, shape = CircleShape)
    ) {
        Text(
            text = "$current / $goal",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp
        )
    }
}

@Composable
fun Steps(current: Int, goal: Int) {
    val remaining: Int = current - goal
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Steps: $current")
        Text(text = "$remaining remaining of $goal")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GainsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        )
        {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProteinGoal(88, 130)
                Steps(84444, 10000)
            }
        }
    }
}