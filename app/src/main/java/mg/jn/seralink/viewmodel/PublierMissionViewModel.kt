package mg.jn.seralink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.CreateJobRequest
import mg.jn.seralink.model.JobListing
import mg.jn.seralink.network.RetrofitClient

sealed class PublierMissionState {
    object Idle : PublierMissionState()
    object Loading : PublierMissionState()
    data class Success(val job: JobListing) : PublierMissionState()
    data class Error(val message: String) : PublierMissionState()
}

class PublierMissionViewModel(private val dataStore: TokenDataStore) : ViewModel() {

    private val _state = MutableStateFlow<PublierMissionState>(PublierMissionState.Idle)
    val state: StateFlow<PublierMissionState> = _state

    fun publierMission(
        titre: String,
        description: String,
        categorie: String,
        budgetMin: Int,
        budgetMax: Int,
        delai: String
    ) {
        viewModelScope.launch {
            _state.value = PublierMissionState.Loading
            try {
                val token = dataStore.getToken() ?: run {
                    _state.value = PublierMissionState.Error("Non connecté")
                    return@launch
                }
                val request = CreateJobRequest(
                    title = titre,
                    description = description,
                    category = categorie,
                    budgetMin = budgetMin,
                    budgetMax = budgetMax,
                    budgetType = "fixed",
                    deadline = delai
                )
                val response = RetrofitClient.api.createJob("Bearer $token", request)
                if (response.isSuccessful) {
                    _state.value = PublierMissionState.Success(response.body()!!)
                } else {
                    _state.value = PublierMissionState.Error("Erreur ${response.code()}")
                }
            } catch (e: Exception) {
                _state.value = PublierMissionState.Error("Connexion impossible : ${e.message}")
            }
        }
    }

    fun resetState() {
        _state.value = PublierMissionState.Idle
    }
}