package com.example.gains.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gains.features.settings.NutritionSettingsViewModel
import com.example.gains.ui.common.EditIcon
import com.example.gains.ui.common.InputField
import com.example.gains.ui.common.NavBackIcon
import com.example.gains.ui.common.NavBar

@Composable
fun NutritionSettingsScreen(popBackStack: () -> Unit) {
    NavBar(
        title = "Nutrition Settings",
        scrollContent = { paddingValues: PaddingValues ->
            NutritionSettingsContent(
                paddingValues
            )
        },
        optionalNavigationComponent = {
            NavBackIcon(popBackStack)
        }
    )
}

@Composable
fun NutritionSettingsContent(paddingValues: PaddingValues) {
    val viewModel: NutritionSettingsViewModel = hiltViewModel()
    var editProtein by remember { mutableStateOf(false) }
    var proteinTarget by remember { mutableIntStateOf(viewModel.getProteinGoal()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (editProtein) {
                InputField(
                    label = "New Protein Target",
                    value = proteinTarget.toString(),
                    onValueChange = {
                        proteinTarget = it.toIntOrNull() ?: 0
                    },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                    onClick = {
                        viewModel.setProteinGoal(proteinTarget)
                        editProtein = false
                    },
                    enabled = true,
                ) {
                    Text("Set Target")
                }
            } else {
                Text("Protein Target is set to ${proteinTarget}g")
                EditIcon({ editProtein = true })
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 10.dp),
        ) {
            Text("View/Remove Custom Sources")
            // dropdown icon that loads the list of sources with delete option
        }
    }
}