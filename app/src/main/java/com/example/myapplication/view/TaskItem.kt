package com.example.myapplication.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.model.TaskModel
import com.example.myapplication.repository.TaskRepository
import com.example.myapplication.ui.theme.GreenRadioButtonSelected
import com.example.myapplication.ui.theme.RedRadioButtonSelected
import com.example.myapplication.ui.theme.YellowRadioButtonSelected

fun getPriorityText(level: Int): Map<String, Any> {
    return when(level) {
        1 -> mapOf("priorityText" to "Baixa", "color" to GreenRadioButtonSelected)
        2 -> mapOf("priorityText" to "Média", "color" to YellowRadioButtonSelected)
        3 -> mapOf("priorityText" to "Alta", "color" to RedRadioButtonSelected)
        else -> mapOf("priorityText" to "Sem prioridade", "color" to Color.Gray)
    }
}

@Composable
fun TaskItem(
    task: TaskModel,
    navController: NavController
) {
    val priority = task.priority ?: 0
    val priorityInfo = getPriorityText(priority)
    Card(
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.Unspecified,
            disabledContentColor = Color.Unspecified,
            disabledContainerColor = Color.Unspecified
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                task.id?.let { navController.navigate("editTask/$it") }
            },
    ) {
        ConstraintLayout (
            modifier = Modifier.padding(20.dp)
        ){
            val (txtTitle, txtDescription, txtPriority,
                cardPriorityColor, btnDelete) = createRefs()

            Text(
                text = task.title!!,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.constrainAs(txtTitle){
                    top.linkTo(
                        anchor = parent.top,
                        margin = 10.dp
                    )
                    start.linkTo(
                        anchor = parent.start,
                        margin = 10.dp
                    )
                }
            )
            Text(
                text = task.description!!,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.constrainAs(txtDescription){
                    top.linkTo(
                        anchor = txtTitle.bottom,
                        margin = 10.dp
                    )
                    start.linkTo(
                        anchor = parent.start,
                        margin = 20.dp
                    )
                }
            )
            Text(
                text = "Prioridade baixa",
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.constrainAs(txtPriority){
                    top.linkTo(
                        anchor = txtDescription.bottom,
                        margin = 10.dp
                    )
                    start.linkTo(
                        anchor = parent.start,
                        margin = 10.dp
                    )
                }
            )
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = getPriorityText(task.priority!!)["color"] as Color
                ),
                modifier = Modifier.size(30.dp).constrainAs(cardPriorityColor) {
                    start.linkTo(
                        txtPriority.end,
                        margin = 15.dp
                    )
                    top.linkTo(
                        anchor = txtDescription.bottom,
                        margin = 5.dp
                    )
                }
            ) {}

            IconButton(
                onClick = {
                    val repository = TaskRepository()

                    task.id?.let { id ->
                        repository.deleteTask(task.id!!) { success ->
                            if (success) Log.d("UI", "Tarefa removida com sucesso")
                            else Log.e("UI", "Falha ao remover tarefa")
                        }
                    }
                },
                modifier = Modifier.constrainAs(btnDelete) {
                    start.linkTo(cardPriorityColor.end)
                    top.linkTo(cardPriorityColor.top)
                }
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                    contentDescription = "Excluir tarefa"
                )
            }
            Text(
                text = priorityInfo["priorityText"] as String,
                modifier = Modifier.constrainAs(txtPriority) {
                    top.linkTo(anchor = txtDescription.bottom, margin = 15.dp)
                    start.linkTo(anchor = parent.start, margin = 10.dp)
                }
            )
        }
    }
}


