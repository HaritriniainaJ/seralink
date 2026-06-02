package mg.jn.seralink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublierMissionScreen(navController: NavController) {

    var titre by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var categorie by remember { mutableStateOf("") }
    var delai by remember { mutableStateOf("") }
    var competences by remember { mutableStateOf("") }
    var expandedCategorie by remember { mutableStateOf(false) }
    var expandedDelai by remember { mutableStateOf(false) }

    val categories = listOf(
        "Développement Mobile",
        "Développement Web",
        "Design UI/UX",
        "Rédaction / SEO",
        "Marketing Digital",
        "Data / IA",
        "Autre"
    )

    val delais = listOf(
        "Moins d'une semaine",
        "1 à 2 semaines",
        "1 mois",
        "2 à 3 mois",
        "Plus de 3 mois"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Publier une mission", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
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

            // Section titre
            SectionCard(title = "Informations générales") {
                OutlinedTextField(
                    value = titre,
                    onValueChange = { titre = it },
                    label = { Text("Titre de la mission *") },
                    placeholder = { Text("Ex: Développeur Android Kotlin") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Dropdown Catégorie
                ExposedDropdownMenuBox(
                    expanded = expandedCategorie,
                    onExpandedChange = { expandedCategorie = !expandedCategorie }
                ) {
                    OutlinedTextField(
                        value = categorie,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Catégorie *") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategorie)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategorie,
                        onDismissRequest = { expandedCategorie = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    categorie = cat
                                    expandedCategorie = false
                                }
                            )
                        }
                    }
                }
            }

            // Section description
            SectionCard(title = "Description") {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description détaillée *") },
                    placeholder = { Text("Décrivez votre mission, les livrables attendus, le contexte...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 6
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = competences,
                    onValueChange = { competences = it },
                    label = { Text("Compétences requises") },
                    placeholder = { Text("Ex: Kotlin, Jetpack Compose, API REST...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Section budget & délai
            SectionCard(title = "Budget & Délai") {
                OutlinedTextField(
                    value = budget,
                    onValueChange = { budget = it.filter { c -> c.isDigit() } },
                    label = { Text("Budget (Ar) *") },
                    placeholder = { Text("Ex: 500000") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    leadingIcon = {
                        Text("Ar", fontSize = 13.sp, color = Color(0xFF888888),
                            modifier = Modifier.padding(start = 8.dp))
                    },
                    suffix = { Text("Ariary") }
                )

                if (budget.isNotEmpty()) {
                    val budgetInt = budget.toLongOrNull() ?: 0L
                    Text(
                        "≈ ${"%,d".format(budgetInt).replace(',', ' ')} Ar",
                        fontSize = 12.sp,
                        color = GreenPrimary,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dropdown Délai
                ExposedDropdownMenuBox(
                    expanded = expandedDelai,
                    onExpandedChange = { expandedDelai = !expandedDelai }
                ) {
                    OutlinedTextField(
                        value = delai,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Délai estimé *") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDelai)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDelai,
                        onDismissRequest = { expandedDelai = false }
                    ) {
                        delais.forEach { d ->
                            DropdownMenuItem(
                                text = { Text(d) },
                                onClick = {
                                    delai = d
                                    expandedDelai = false
                                }
                            )
                        }
                    }
                }
            }

            // Récap budget
            if (budget.isNotEmpty() && titre.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenLight)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null,
                            tint = GreenPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Récapitulatif", fontWeight = FontWeight.Bold,
                                fontSize = 13.sp, color = GreenPrimary)
                            Text(titre, fontSize = 12.sp, color = Color(0xFF333333))
                            if (budget.isNotEmpty()) {
                                Text("Budget : ${budget} Ar", fontSize = 12.sp, color = Color(0xFF555555))
                            }
                        }
                    }
                }
            }

            // Bouton publier
            Button(
                onClick = {
                    // TODO: appel API Laravel pour publier la mission
                    navController.popBackStack()
                },
                enabled = titre.isNotEmpty() && description.isNotEmpty()
                        && budget.isNotEmpty() && categorie.isNotEmpty() && delai.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Icon(Icons.Default.Send, contentDescription = null,
                    modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Publier la mission", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}