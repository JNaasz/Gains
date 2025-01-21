package com.example.gains

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import java.text.NumberFormat

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
                        TextColumn()
                        MainButton("Log Protein") { buttonClick("Clicked Log Protein") }
                        MainButton("Track Strength/Mobility") { buttonClick("Clicked Track Strength/Mobility") }
                        MainButton("Workouts") { buttonClick("Clicked Workouts") }
                    }
                }
            }
        }
    }
}

fun buttonClick(text: String) {
    Log.d(TAG, "buttonClick: $text")
}

fun getStepText(): Array<String> {
    val current = 8444
    val target = 10000
    val remaining: Int = target - current
    val text1 = "Steps: ${NumberFormat.getNumberInstance().format(current)}"
    val text2 = "${
        NumberFormat.getNumberInstance().format(remaining)
    } remaining of ${NumberFormat.getNumberInstance().format(target)}"
    return arrayOf(text1, text2)
}

fun getActivitiesText(): Array<String> {
    val text1 = "Today's Activities:"
    val text2 = "4m Run"
    val text3 = "45min Strength"
    val text4 = "15min Mobility"
    return arrayOf(text1, text2, text3, text4)
}

@Composable
fun ProteinGoal(current: Int, goal: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .requiredSize(150.dp) // Enforces the size to not stretch
            .padding(16.dp)
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
fun TextColumn() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)) {
        TextChunk(getStepText())
        TextChunk(getActivitiesText())
    }
}

@Composable
fun TextChunk(textArr: Array<String>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        textArr.forEach { text ->
            Text(text = text)
        }
    }
}

@Composable
fun MainButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(text)
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
                TextColumn()
                MainButton("Log Protein") { buttonClick("Clicked Log Protein") }
                MainButton("Track Strength/Mobility") { buttonClick("Clicked Track Strength/Mobility") }
                MainButton("Workouts") { buttonClick("Clicked Workouts") }
            }
        }
    }
}