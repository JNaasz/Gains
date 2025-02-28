package com.example.gains.ui.screens.settings

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gains.database.ProteinSource
import com.example.gains.ui.common.EditIcon

@Composable
fun CustomProteinItem(
    item: ProteinSource,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(item.name)
            Text("${item.proteinPerServing}g protein per ${item.servingSize}${item.servingUnit}")
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete Icon"
        )
        EditIcon { Log.d("TAG", "NutritionSettingsContent: edit Edamame") }
    }
}
