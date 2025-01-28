package com.example.gains.ui.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gains.features.home.HomeViewModel
import com.example.gains.ui.theme.GainsTheme
import com.example.gains.ui.theme.high
import com.example.gains.ui.theme.highPressed
import com.example.gains.ui.theme.low
import com.example.gains.ui.theme.lowPressed
import com.example.gains.ui.theme.medium
import com.example.gains.ui.theme.mediumPressed
import com.example.gains.ui.theme.percentText
import java.text.NumberFormat

@Composable
fun HomeScreen(
    navigateToNutrition: () -> Unit,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val proteinTotal by viewModel.proteinTotal.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProteinGoal(proteinTotal.toInt(), 130, navigateToNutrition)
        TextColumn()
        MainButton("Log Protein") { navigateToNutrition() }
        MainButton("Track Strength/Mobility") { buttonClick("Clicked Track Strength/Mobility") }
        MainButton("Workouts") { buttonClick("Clicked Workouts") }
    }
}

fun buttonClick(text: String) {
    Log.d(ContentValues.TAG, "buttonClick: $text")
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
fun ProteinGoal(current: Int, goal: Int, navigateToNutrition: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val percentComplete = (current / goal.toFloat()) * 100

    val color = if (percentComplete >= 100F) {
        if (isPressed) highPressed
        else high
    } else if (percentComplete > 50F) {
        if (isPressed) mediumPressed
        else medium
    } else if (isPressed) lowPressed
        else low

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .requiredSize(170.dp) // Enforces the size to not stretch
            .padding(16.dp)
            .background(color = color, shape = CircleShape)
            .clickable(
                onClick = {},
                interactionSource = interactionSource,
                indication = null // ripple()
            ),
    ) {
        TextButton(
            onClick = {
                navigateToNutrition()
            },
        ) {
            Text(
                text = "$current / $goal",
                color = percentText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
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
                ProteinGoal(88, 130, {})
                TextColumn()
                MainButton("Log Protein") { buttonClick("Clicked Log Protein") }
                MainButton("Track Strength/Mobility") { buttonClick("Clicked Track Strength/Mobility") }
                MainButton("Workouts") { buttonClick("Clicked Workouts") }
            }
        }
    }
}