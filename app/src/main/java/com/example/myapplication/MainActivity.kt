package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.intro.view.SaveTask
import com.example.myapplication.model.TaskModel
import com.example.myapplication.repository.TaskRepository
import com.example.myapplication.ui.theme.IntroTheme
import com.example.myapplication.view.EditTask
import com.example.myapplication.view.TaskList

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object PreferencesKeys{
    val IS_DARK_MODE = booleanPreferencesKey("isDarkMode")
    val CONTANDO = intPreferencesKey("contando")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkModeFlow = remember {
                PreferencesDataStore(this).getPreferences(PreferencesKeys.IS_DARK_MODE)
            }

            val isDarkMode by isDarkModeFlow.collectAsState(initial = false)

            IntroTheme(darkTheme = isDarkMode ?: false) {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "taskList") {
                    composable("taskList") { TaskList(navController) }
                    composable("saveTask") { SaveTask(navController) }
                    composable("editTask/{taskId}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("taskId")
                        val repository = TaskRepository()
                        val task = remember { mutableStateOf<TaskModel?>(null) }

                        LaunchedEffect(id) {
                            repository.getTaskById(id!!) { fetchedTask ->
                                if (fetchedTask != null) {
                                    task.value = fetchedTask
                                } else {
                                    Log.e("UI", "Tarefa não encontrada para id: $id")
                                }
                            }
                        }

                        task.value?.let {
                            EditTask(navController, it)
                        }
                    }
                }
            }
        }
    }
}