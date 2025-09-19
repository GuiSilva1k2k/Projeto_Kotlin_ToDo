package com.example.intro.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.component.CustomButton
import com.example.myapplication.component.CustomTextField
import com.example.myapplication.model.Priority
import com.example.myapplication.model.TaskModel
import com.example.myapplication.repository.TaskRepository
import com.example.myapplication.ui.theme.GreenRadioButtonSelected
import com.example.myapplication.ui.theme.GreenRadioButtonUnselected
import com.example.myapplication.ui.theme.PurpleGrey40
import com.example.myapplication.ui.theme.RedRadioButtonSelected
import com.example.myapplication.ui.theme.RedRadioButtonUnselected
import com.example.myapplication.ui.theme.YellowRadioButtonSelected
import com.example.myapplication.ui.theme.YellowRadioButtonUnselected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun onClickSaveButton(scope: CoroutineScope, context: Context, task: TaskModel, navController: NavController) {
    val taskRepository = TaskRepository()
    var taskIsValid = true

    scope.launch(Dispatchers.IO) {
        taskIsValid = task.title!!.isNotEmpty() && task.description!!.isNotEmpty()
        if (taskIsValid) {
            taskRepository.saveTask(task)
        }
    }

    scope.launch(Dispatchers.Main) {
        if (taskIsValid) {
            Toast.makeText(context, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } else {
            if (task.title!!.isEmpty()) {
                Toast.makeText(context, "Título é obrigatório!", Toast.LENGTH_SHORT).show()
            } else if (task.description!!.isEmpty()) {
                Toast.makeText(context, "Descrição é obrigatório!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveTask(navController: NavController) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var noPriority by remember { mutableStateOf(false) }
    var lowPriority by remember { mutableStateOf(false) }
    var mediumPriority by remember { mutableStateOf(false) }
    var highPriority by remember { mutableStateOf(false) }

    var taskLocation by remember { mutableStateOf("") }

    var taskPriority by remember { mutableIntStateOf( Priority.NO_PRIORITY.value ) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Salvar tarefa",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = topAppBarColors(containerColor = PurpleGrey40)
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Prioridade", fontSize = 18.sp)
                RadioButton(
                    selected = lowPriority,
                    onClick = {
                        taskPriority = Priority.LOW.value
                        highPriority = false
                        mediumPriority = false
                        lowPriority = true
                    },
                    colors = RadioButtonColors(
                        selectedColor = GreenRadioButtonSelected,
                        unselectedColor = GreenRadioButtonUnselected,
                        disabledSelectedColor = Color.Unspecified,
                        disabledUnselectedColor = Color.Unspecified,
                    )
                )
                RadioButton(
                    selected = mediumPriority,
                    onClick = {
                        taskPriority = Priority.MEDIUM.value
                        highPriority = false
                        mediumPriority = true
                        lowPriority = false
                    },
                    colors = RadioButtonColors(
                        selectedColor = YellowRadioButtonSelected,
                        unselectedColor = YellowRadioButtonUnselected,
                        disabledSelectedColor = Color.Unspecified,
                        disabledUnselectedColor = Color.Unspecified,
                    )
                )
                RadioButton(
                    selected = highPriority,
                    onClick = {
                        taskPriority = Priority.HIGH.value
                        highPriority = true
                        mediumPriority = false
                        lowPriority = false
                    },
                    colors = RadioButtonColors(
                        selectedColor = RedRadioButtonSelected,
                        unselectedColor = RedRadioButtonUnselected,
                        disabledSelectedColor = Color.Unspecified,
                        disabledUnselectedColor = Color.Unspecified,
                    )
                )
            }

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                CustomButton(
                    onClick = {navController.navigate("taskList")},
                    label = "Cancelar",
                    color = Color.Red,
                    modifier = Modifier
                        .height(80.dp)
                        .padding(10.dp)
                )
                CustomButton(
                    onClick = {
                        onClickSaveButton(
                            scope,
                            context,
                            TaskModel(title = taskTitle, description =  taskDescription, priority =  taskPriority, location = taskLocation),
                            navController
                        )
                    },
                    label = "Salvar",
                    color = Color.Blue,
                    modifier = Modifier
                        .height(80.dp)
                        .padding(10.dp)
                )
            }

        }
    }

}

@Preview
@Composable
fun SaveTaskPreview() {
    val navController = rememberNavController()

    SaveTask(navController)

}