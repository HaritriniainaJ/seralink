package mg.jn.seralink.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.viewmodel.PublierMissionState
import mg.jn.seralink.viewmodel.PublierMissionViewModel
import mg.jn.seralink.viewmodel.PublierMissionViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublierMissionScreen(navController: NavController) {
    val context = LocalContext.current
    val dataStore = TokenDataStore(context)
    val viewModel: PublierMissionViewModel = viewModel(
        factory = PublierMissionViewModelFactory(dataStore)
    )

    val state by viewModel.state.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var budgetMin by remember { mutableStateOf("") }
    var budgetMax by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }

    val categories = listOf(
        "Développement Web", "Développement Mobile", "Design UI/UX",
        "Rédaction", "Marketing Digital", "Comptabilité", "Autre"
    )
    var categoryExpanded by remember { mutableStateOf(false) }

    val formValid = title.isNotBlank() && description.isNotBlank() &&
            category.isNotBlank() && budgetMin.isNotBlank() &&
            budgetMax.isNotBlank() && deadline.isNotBlank()

    val isLoading = state is PublierMissionState.Loading

    if (state is PublierMissionState.Success) {
        AlertDialog(
            onDismissRequest = { viewModel.resetState(); navController.popBackStack() },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = null,
                    tint = GreenPrimary, modifier = Modifier.size(48.dp))
            },
            title = { Text("Mission publiée !", fontWeight = FontWeight.Bold) },
            text = { Text("Votre mission est en ligne. Les freelances peuvent maintenant postuler.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.resetState(); navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("OK") }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (state is PublierMissionState.Error) {
        AlertDialog(
            onDismissRequest = { viewModel.resetState() },
            icon = {
                Icon(Icons.Default.ErrorOutline, contentDescription = null,
                    tint = Color(0xFFE53935), modifier = Modifier.size(48.dp))
            },
            title = { Text("Erreur", fontWeight = FontWeight.Bold) },
            text = { Text((state as PublierMissionState.Error).message) },
            confirmButton = {
                Button(onClick = { viewModel.resetState() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(10.dp)) { Text("OK") }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publier une mission", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = Color.White) {
                Button(
                    onClick = {
                        if (formValid) {
                            viewModel.publierMission(
                                titre = title,
                                description = description,
                                categorie = category,
                                budgetMin = budgetMin.toIntOrNull() ?: 0,
                                budgetMax = budgetMax.toIntOrNull() ?: 0,
                                delai = deadline
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (formValid) GreenPrimary else Color(0xFFBBBBBB)
                    ),
                    enabled = formValid && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White,
                            modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.Publish, contentDescription = null,
                            modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Publier la mission", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        },
        containerColor = Color(0xFFF8F8F8)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    Text("Informations de la mission", fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, color = Color(0xFF1A1A1A))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Titre de la mission") },
                        placeholder = { Text("Ex: Développeur Android Kotlin") },
                        leadingIcon = { Icon(Icons.Default.Work, contentDescription = null, tint = GreenPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = GreenPrimary)
                    )

                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Catégorie") },
                            leadingIcon = { Icon(Icons.Default.Category, contentDescription = null, tint = GreenPrimary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedBorderColor = GreenPrimary)
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = { category = cat; categoryExpanded = false }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        placeholder = { Text("Décrivez la mission en détail...") },
                        modifier = Modifier.fillMaxWidth().height(130.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = GreenPrimary)
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    Text("Budget & Délai", fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, color = Color(0xFF1A1A1A))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = budgetMin,
                            onValueChange = { budgetMin = it.filter { c -> c.isDigit() } },
                            label = { Text("Budget min (Ar)") },
                            leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null, tint = GreenPrimary) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedBorderColor = GreenPrimary)
                        )
                        OutlinedTextField(
                            value = budgetMax,
                            onValueChange = { budgetMax = it.filter { c -> c.isDigit() } },
                            label = { Text("Budget max (Ar)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedBorderColor = GreenPrimary)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Type :", fontSize = 13.sp, color = Color(0xFF555555))
                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = { Text("Fixe") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GreenPrimary,
                                selectedLabelColor = Color.White)
                        )
                    }

                    OutlinedTextField(
                        value = deadline,
                        onValueChange = { deadline = it },
                        label = { Text("Date limite") },
                        placeholder = { Text("Ex: 2025-09-01") },
                        leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GreenPrimary) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedBorderColor = GreenPrimary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}