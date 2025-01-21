import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LogNutritionScreen(
    popBackStack: () -> Unit,
) {
    Text(text = "On the Nutrition Screen")
    Button(onClick = popBackStack) {
        Text(text = "go back")
    }
}