package mg.jn.seralink.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mg.jn.seralink.model.JobListing

val sampleJobs = listOf(
    JobListing(1, "Développeur Android", "App mobile e-commerce", "Mobile", 500000, 1500000, "fixed", "open", "2025-08-01", null),
    JobListing(2, "Designer UI/UX", "Refonte d'une application", "Design", 300000, 800000, "fixed", "open", "2025-07-15", null),
    JobListing(3, "Développeur Laravel", "API REST pour startup", "Backend", 400000, 1200000, "fixed", "open", "2025-09-01", null),
    JobListing(4, "Rédacteur web", "Articles SEO pour blog", "Rédaction", 50000, 150000, "hourly", "open", "2025-07-30", null),
    JobListing(5, "Data Analyst", "Analyse des ventes mensuelles", "Data", 600000, 1000000, "fixed", "open", "2025-08-15", null),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("SeraLink", fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("freelance_profile/1")
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Profil")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {},
                    label = { Text("Missions") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.MY_CONTRACTS) },
                    icon = {},
                    label = { Text("Contrats") }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(sampleJobs) { job ->
                JobCard(job = job, onClick = {
                    navController.navigate("job_detail/${job.id}")
                })
            }
        }
    }
}

@Composable
fun JobCard(job: JobListing, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = job.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = job.category,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${job.budgetMin / 1000}k - ${job.budgetMax / 1000}k Ar",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                job.deadline?.let {
                    Text(
                        text = "Délai : $it",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}