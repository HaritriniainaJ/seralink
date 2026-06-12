package mg.jn.seralink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.PublicUserResponse
import mg.jn.seralink.model.UserResponse
import mg.jn.seralink.network.RetrofitClient

sealed class ProfilState {
    object Idle : ProfilState()
    object Loading : ProfilState()
    data class Success(val user: UserResponse) : ProfilState()
    data class Error(val message: String) : ProfilState()
}

class ProfilViewModel(private val dataStore: TokenDataStore) : ViewModel() {

    private val _profilState = MutableStateFlow<ProfilState>(ProfilState.Idle)
    val profilState: StateFlow<ProfilState> = _profilState

    private val _publicProfile = MutableStateFlow<PublicUserResponse?>(null)
    val publicProfile: StateFlow<PublicUserResponse?> = _publicProfile

    private val _isLoadingPublic = MutableStateFlow(false)
    val isLoadingPublic: StateFlow<Boolean> = _isLoadingPublic

    fun loadMyProfile() {
        viewModelScope.launch {
            _profilState.value = ProfilState.Loading
            try {
                val token = dataStore.getToken() ?: return@launch
                val response = RetrofitClient.api.getProfile("Bearer $token")
                if (response.isSuccessful) {
                    _profilState.value = ProfilState.Success(response.body()!!)
                } else {
                    _profilState.value = ProfilState.Error("Erreur ${response.code()}")
                }
            } catch (e: Exception) {
                _profilState.value = ProfilState.Error("Connexion impossible : ${e.message}")
            }
        }
    }

    fun loadPublicProfile(userId: Int) {
        viewModelScope.launch {
            _isLoadingPublic.value = true
            try {
                val token = dataStore.getToken() ?: return@launch
                val response = RetrofitClient.api.getUser("Bearer $token", userId)
                if (response.isSuccessful) {
                    _publicProfile.value = response.body()
                }
            } catch (e: Exception) {
                // silencieux
            } finally {
                _isLoadingPublic.value = false
            }
        }
    }
}