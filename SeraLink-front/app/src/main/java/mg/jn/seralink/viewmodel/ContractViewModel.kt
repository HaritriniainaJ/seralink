package mg.jn.seralink.viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.Contract
import mg.jn.seralink.network.RetrofitClient
import java.io.File

sealed class ContractActionState {
    object Idle : ContractActionState()
    object Loading : ContractActionState()
    data class Success(val message: String, val contract: Contract) : ContractActionState()
    data class PaymentReady(val clientSecret: String, val contract: Contract) : ContractActionState()
    data class Error(val message: String) : ContractActionState()
}

class ContractViewModel(private val dataStore: TokenDataStore) : ViewModel() {

    private val _contracts = MutableStateFlow<List<Contract>>(emptyList())
    val contracts: StateFlow<List<Contract>> = _contracts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _actionState = MutableStateFlow<ContractActionState>(ContractActionState.Idle)
    val actionState: StateFlow<ContractActionState> = _actionState

    fun loadContracts() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val token = dataStore.getToken() ?: run {
                    android.util.Log.e("ContractVM", "TOKEN NULL")
                    return@launch
                }
                android.util.Log.d("ContractVM", "token: $token")
                val response = RetrofitClient.api.getContracts("Bearer $token")
                android.util.Log.d("ContractVM", "code: ${response.code()} body size: ${response.body()?.size}")
                if (response.isSuccessful) {
                    _contracts.value = response.body() ?: emptyList()
                } else {
                    android.util.Log.e("ContractVM", "error: ${response.errorBody()?.string()}")
                    _errorMessage.value = "Erreur ${response.code()}"
                }
            } catch (e: Exception) {
                android.util.Log.e("ContractVM", "exception: ${e.message}")
                _errorMessage.value = "Connexion impossible : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signContract(contractId: Int) {
        viewModelScope.launch {
            _actionState.value = ContractActionState.Loading
            try {
                val token = dataStore.getToken() ?: return@launch
                val response = RetrofitClient.api.signContract("Bearer $token", contractId)
                if (response.isSuccessful) {
                    _actionState.value = ContractActionState.Success(
                        message = "Contrat signé électroniquement ✓",
                        contract = response.body()!!.contract
                    )
                    loadContracts()
                } else {
                    _actionState.value = ContractActionState.Error("Erreur ${response.code()}")
                }
            } catch (e: Exception) {
                _actionState.value = ContractActionState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun downloadAndOpenPdf(context: Context, contractId: Int) {
        viewModelScope.launch {
            try {
                val token = dataStore.getToken() ?: return@launch
                val client = okhttp3.OkHttpClient.Builder()
                    .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .build()
                val request = okhttp3.Request.Builder()
                    .url("${RetrofitClient.BASE_URL}contracts/$contractId/pdf")
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                val response = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    client.newCall(request).execute()
                }
                if (response.isSuccessful) {
                    val body = response.body ?: return@launch
                    val fileName = "contrat-seralink-$contractId.pdf"
                    val file = java.io.File(context.getExternalFilesDir(null), fileName)
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                        file.outputStream().use { body.byteStream().copyTo(it) }
                    }
                    val uri = androidx.core.content.FileProvider.getUriForFile(
                        context, "${context.packageName}.provider", file
                    )
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "application/pdf")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                } else {
                    _actionState.value = ContractActionState.Error("Erreur PDF : ${response.code}")
                }
            } catch (e: Exception) {
                _actionState.value = ContractActionState.Error("Erreur téléchargement : ${e.message}")
            }
        }
    }
    fun payContract(contractId: Int) {
        viewModelScope.launch {
            _actionState.value = ContractActionState.Loading
            try {
                val token = dataStore.getToken() ?: return@launch
                val response = RetrofitClient.api.payContract("Bearer $token", contractId)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    _actionState.value = ContractActionState.PaymentReady(
                        clientSecret = body.clientSecret,
                        contract = body.contract
                    )
                    loadContracts()
                } else {
                    _actionState.value = ContractActionState.Error("Erreur paiement ${response.code()}")
                }
            } catch (e: Exception) {
                _actionState.value = ContractActionState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun completeContract(contractId: Int) {
        viewModelScope.launch {
            _actionState.value = ContractActionState.Loading
            try {
                val token = dataStore.getToken() ?: return@launch
                val response = RetrofitClient.api.completeContract("Bearer $token", contractId)
                if (response.isSuccessful) {
                    _actionState.value = ContractActionState.Success(
                        message = "Travail marqué comme terminé",
                        contract = response.body()!!.contract
                    )
                    loadContracts()
                } else {
                    _actionState.value = ContractActionState.Error("Erreur ${response.code()}")
                }
            } catch (e: Exception) {
                _actionState.value = ContractActionState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun releaseContract(contractId: Int) {
        viewModelScope.launch {
            _actionState.value = ContractActionState.Loading
            try {
                val token = dataStore.getToken() ?: return@launch
                val response = RetrofitClient.api.releaseContract("Bearer $token", contractId)
                if (response.isSuccessful) {
                    _actionState.value = ContractActionState.Success(
                        message = "Paiement libéré au freelance !",
                        contract = response.body()!!.contract
                    )
                    loadContracts()
                } else {
                    _actionState.value = ContractActionState.Error("Erreur ${response.code()}")
                }
            } catch (e: Exception) {
                _actionState.value = ContractActionState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun disputeContract(contractId: Int) {
        viewModelScope.launch {
            _actionState.value = ContractActionState.Loading
            try {
                val token = dataStore.getToken() ?: return@launch
                val response = RetrofitClient.api.disputeContract("Bearer $token", contractId)
                if (response.isSuccessful) {
                    _actionState.value = ContractActionState.Success(
                        message = "Litige ouvert",
                        contract = response.body()!!.contract
                    )
                    loadContracts()
                } else {
                    _actionState.value = ContractActionState.Error("Erreur ${response.code()}")
                }
            } catch (e: Exception) {
                _actionState.value = ContractActionState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun resetAction() {
        _actionState.value = ContractActionState.Idle
    }
}