package mg.jn.seralink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.CreateJobRequest
import mg.jn.seralink.model.JobListing
import mg.jn.seralink.network.RetrofitClient

sealed class JobState {
    object Loading : JobState()
    data class Success(val jobs: List<JobListing>) : JobState()
    data class Error(val message: String) : JobState()
}

sealed class JobDetailState {
    object Loading : JobDetailState()
    data class Success(val job: JobListing) : JobDetailState()
    data class Error(val message: String) : JobDetailState()
}

class JobViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = TokenDataStore(application)
    private val api = RetrofitClient.api

    private val _jobState = MutableStateFlow<JobState>(JobState.Loading)
    val jobState: StateFlow<JobState> = _jobState

    private val _jobDetailState = MutableStateFlow<JobDetailState>(JobDetailState.Loading)
    val jobDetailState: StateFlow<JobDetailState> = _jobDetailState

    private val _actionState = MutableStateFlow<String?>(null)
    val actionState: StateFlow<String?> = _actionState

    fun loadJobs(category: String? = null, search: String? = null) {
        viewModelScope.launch {
            android.util.Log.d("SeraLink", "loadJobs() appelé")
            _jobState.value = JobState.Loading
            try {
                android.util.Log.d("SeraLink", "Appel API getJobs...")
                val response = api.getJobs(category = category, search = search)
                android.util.Log.d("SeraLink", "Réponse: ${response.code()} - ${response.isSuccessful}")
                if (response.isSuccessful) {
                    val jobs = response.body()!!.data
                    android.util.Log.d("SeraLink", "Missions reçues: ${jobs.size}")
                    _jobState.value = JobState.Success(jobs)
                } else {
                    android.util.Log.e("SeraLink", "Erreur API: ${response.code()}")
                    _jobState.value = JobState.Error("Erreur chargement missions")
                }
            } catch (e: Exception) {
                android.util.Log.e("SeraLink", "Exception: ${e.message}")
                _jobState.value = JobState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun loadJobDetail(jobId: Int) {
        viewModelScope.launch {
            _jobDetailState.value = JobDetailState.Loading
            try {
                val response = api.getJob(jobId)
                if (response.isSuccessful) {
                    _jobDetailState.value = JobDetailState.Success(response.body()!!)
                } else {
                    _jobDetailState.value = JobDetailState.Error("Mission introuvable")
                }
            } catch (e: Exception) {
                _jobDetailState.value = JobDetailState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun createJob(request: CreateJobRequest) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.createJob(token, request)
                if (response.isSuccessful) {
                    _actionState.value = "Mission créée avec succès"
                    loadJobs()
                } else {
                    _actionState.value = "Erreur création mission"
                }
            } catch (e: Exception) {
                _actionState.value = "Erreur : ${e.message}"
            }
        }
    }

    fun deleteJob(jobId: Int) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.deleteJob(token, jobId)
                if (response.isSuccessful) {
                    _actionState.value = "Mission supprimée"
                    loadJobs()
                } else {
                    _actionState.value = "Erreur suppression"
                }
            } catch (e: Exception) {
                _actionState.value = "Erreur : ${e.message}"
            }
        }
    }

    fun resetAction() { _actionState.value = null }
}