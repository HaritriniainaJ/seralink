package mg.jn.seralink.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.ReviewRequest
import mg.jn.seralink.network.RetrofitClient

class ReviewViewModel(private val dataStore: TokenDataStore) : ViewModel() {
    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Success(val msg: String) : State()
        data class Error(val msg: String) : State()
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    fun submit(contractId: Int, rating: Int, comment: String?) {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val token = dataStore.getToken() ?: return@launch
                val response = RetrofitClient.api.submitReview(
                    "Bearer $token", contractId, ReviewRequest(rating, comment)
                )
                if (response.isSuccessful) {
                    _state.value = State.Success("Avis envoyé avec succès !")
                } else {
                    val err = response.errorBody()?.string() ?: "Erreur ${response.code()}"
                    _state.value = State.Error(err)
                }
            } catch (e: Exception) {
                _state.value = State.Error(e.message ?: "Erreur réseau")
            }
        }
    }

    fun reset() { _state.value = State.Idle }
}

class ReviewViewModelFactory(private val ds: TokenDataStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(c: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ReviewViewModel(ds) as T
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(navController: NavController, contractId: Int) {
    val context = LocalContext.current
    val dataStore = TokenDataStore(context)
    val viewModel: ReviewViewModel = viewModel(factory = ReviewViewModelFactory(dataStore))
    val state by viewModel.state.collectAsState()

    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    when (val s = state) {
        is ReviewViewModel.State.Success -> {
            AlertDialog(
                onDismissRequest = { viewModel.reset(); navController.popBackStack() },
                icon = { Icon(Icons.Default.Star, contentDescription = null,
                    tint = Color(0xFFFFC107), modifier = Modifier.size(48.dp)) },
                title = { Text("Merci !", fontWeight = FontWeight.Bold) },
                text = { Text(s.msg) },
                confirmButton = {
                    Button(
                        onClick = { viewModel.reset(); navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) { Text("OK") }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
        is ReviewViewModel.State.Error -> {
            AlertDialog(
                onDismissRequest = { viewModel.reset() },
                title = { Text("Erreur", fontWeight = FontWeight.Bold) },
                text = { Text(s.msg) },
                confirmButton = {
                    Button(onClick = { viewModel.reset() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        shape = RoundedCornerShape(10.dp)) { Text("OK") }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
        else -> {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laisser un avis", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text("Comment s'est passée la collaboration ?",
                fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
                color = Color(0xFF333333))

            Spacer(modifier = Modifier.height(24.dp))

            // Étoiles
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(5) { index ->
                    IconButton(onClick = { rating = index + 1 }) {
                        Icon(
                            imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            Text(
                when (rating) {
                    1 -> "Très insatisfait"
                    2 -> "Insatisfait"
                    3 -> "Correct"
                    4 -> "Satisfait"
                    5 -> "Excellent !"
                    else -> "Sélectionne une note"
                },
                fontSize = 14.sp,
                color = if (rating > 0) Color(0xFFFFC107) else Color(0xFF999999),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Commentaire (optionnel)") },
                placeholder = { Text("Décris ton expérience...") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (rating > 0) viewModel.submit(contractId, rating, comment.ifBlank { null })
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rating > 0) GreenPrimary else Color(0xFFCCCCCC)
                ),
                enabled = rating > 0 && state !is ReviewViewModel.State.Loading
            ) {
                if (state is ReviewViewModel.State.Loading) {
                    CircularProgressIndicator(color = Color.White,
                        modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Envoyer l'avis", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}