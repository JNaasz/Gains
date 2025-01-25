package com.example.gains.ui.screens.logNutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gains.features.nutrition.LogNutritionViewModel

@Composable
fun ConfirmDialog(
    viewModel: LogNutritionViewModel,
    popBackStack: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            viewModel.onDismissDialog()
            popBackStack()
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Would you like to save this custom item to food selections?",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            viewModel.onDismissDialog()
                            popBackStack()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("No")
                    }
                    TextButton(
                        onClick = {
                            viewModel.storeCustomItem()
                            popBackStack()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Yes")
                    }
                }
            }
        }
    }
}