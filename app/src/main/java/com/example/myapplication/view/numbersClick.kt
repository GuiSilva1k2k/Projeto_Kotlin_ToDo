package com.example.myapplication.view

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.PreferencesDataStore
import com.example.myapplication.PreferencesKeys
import com.example.myapplication.ui.theme.PurpleGrey40
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Numbers(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { PreferencesDataStore(context) }

    val contadorFlow = remember { prefs.getPreferences(PreferencesKeys.CONTANDO) }
    val savedCount by contadorFlow.collectAsState(initial = 0)

    var contador by remember { mutableStateOf(savedCount ?: 0) }

    LaunchedEffect(savedCount) {
        if (savedCount != null) contador = savedCount!!
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Contador de cliques",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PurpleGrey40)
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Cliques: $contador", fontSize = 22.sp)

            Button(
                onClick = {
                    contador++
                    scope.launch {
                        prefs.setPreferences(PreferencesKeys.CONTANDO, contador)
                    }
                },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.8f)
                    .height(60.dp)
            ) {
                Text("Clique aqui")
            }
        }
    }
}
