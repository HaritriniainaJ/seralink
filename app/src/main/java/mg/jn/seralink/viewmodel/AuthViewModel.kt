package mg.jn.seralink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.LoginRequest
import mg.jn.seralink.model.RegisterRequest
import mg.jn.seralink.network.RetrofitClient

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val role: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = TokenDataStore(application)
    private val api = RetrofitClient.api

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String, role: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = api.login(LoginRequest(email, password, role))
                if (response.isSuccessful) {
                    val body = response.body()!!
                    dataStore.saveToken(
                        token = body.token,
                        role = body.user.role,
                        name = body.user.name,
                        id = body.user.id
                    )
                    _authState.value = AuthState.Success(body.user.role)
                } else {
                    _authState.value = AuthState.Error("Email ou mot de passe incorrect")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erreur de connexion : ${e.message}")
            }
        }
    }

    fun register(name: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = api.register(RegisterRequest(name, email, password, password, role))
                if (response.isSuccessful) {
                    val body = response.body()!!
                    dataStore.saveToken(
                        token = body.token,
                        role = body.user.role,
                        name = body.user.name,
                        id = body.user.id
                    )
                    _authState.value = AuthState.Success(body.user.role)
                } else {
                    _authState.value = AuthState.Error("Erreur lors de l'inscription")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erreur de connexion : ${e.message}")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}