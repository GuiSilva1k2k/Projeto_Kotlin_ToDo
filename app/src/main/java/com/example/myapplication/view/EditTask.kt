package com.example.myapplication.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.component.CustomButton
import com.example.myapplication.component.CustomTextField
import com.example.myapplication.model.Priority
import com.example.myapplication.model.TaskModel
import com.example.myapplication.repository.TaskRepository
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTask(
    navController: NavController,
    task: TaskModel
) {
    var taskTitle by remember { mutableStateOf(task.title ?: "") }
    var taskDescription by remember { mutableStateOf(task.description ?: "") }
    var taskLocation by remember { mutableStateOf(task.location ?: "") }
    var taskPriority by remember { mutableIntStateOf(task.priority ?: Priority.NO_PRIORITY.value) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar tarefa",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PurpleGrey40)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Título
            CustomTextField(
                value = taskTitle,
                label = "Título da tarefa",
                onValueChange = { taskTitle = it },
                maxLines = 1,
                keyboardType = KeyboardType.Text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 20.dp, 20.dp, 0.dp)
            )

            // Descrição
            CustomTextField(
                value = taskDescription,
                label = "Descrição da tarefa",
                onValueChange = { taskDescription = it },
                maxLines = 5,
                keyboardType = KeyboardType.Text,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(20.dp, 20.dp, 20.dp, 0.dp)
            )

            // Local
            CustomTextField(
                value = taskLocation,
                label = "Local",
                onValueChange = { taskLocation = it },
                maxLines = 1,
                keyboardType = KeyboardType.Text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 20.dp, 20.dp, 0.dp)
            )

            // Prioridade
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
            ) {
                Text(text = "Prioridade", fontSize = 18.sp)

                RadioButton(
                    selected = taskPriority == Priority.LOW.value,
                    onClick = { taskPriority = Priority.LOW.value },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = GreenRadioButtonSelected,
                        unselectedColor = GreenRadioButtonUnselected
                    )
                )
                RadioButton(
                    selected = taskPriority == Priority.MEDIUM.value,
                    onClick = { taskPriority = Priority.MEDIUM.value },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = YellowRadioButtonSelected,
                        unselectedColor = YellowRadioButtonUnselected
                    )
                )
                RadioButton(
                    selected = taskPriority == Priority.HIGH.value,
                    onClick = { taskPriority = Priority.HIGH.value },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = RedRadioButtonSelected,
                        unselectedColor = RedRadioButtonUnselected
                    )
                )
            }

            // Botão salvar alterações
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomButton(
                    onClick = {
                        val repository = TaskRepository()
                        repository.updateTask(
                            docId = task.id!!,
                            updates = mapOf(
                                "title" to taskTitle,
                                "description" to taskDescription,
                                "location" to taskLocation,
                                "priority" to taskPriority
                            )
                        ) { success ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Alterações salvas!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Erro ao salvar alterações",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    label = "Salvar alterações",
                    color = Color.Blue,
                    modifier = Modifier
                        .height(80.dp)
                        .padding(10.dp)
                )
            }
        }
    }
}
