package mg.jn.seralink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.ClientDashboard
import mg.jn.seralink.model.FreelanceDashboard
import mg.jn.seralink.network.RetrofitClient

sealed class DashboardState {
    object Loading : DashboardState()
    data class FreelanceSuccess(val data: FreelanceDashboard) : DashboardState()
    data class ClientSuccess(val data: ClientDashboard) : DashboardState()
    data class Error(val message: String) : DashboardState()
}

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = TokenDataStore(application)
    private val api = RetrofitClient.api

    private val _dashboardState = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val dashboardState: StateFlow<DashboardState> = _dashboardState

    fun loadFreelanceDashboard() {
        viewModelScope.launch {
            _dashboardState.value = DashboardState.Loading
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.getDashboardFreelance(token)
                if (response.isSuccessful) {
                    _dashboardState.value = DashboardState.FreelanceSuccess(response.body()!!)
                } else {
                    _dashboardState.value = DashboardState.Error("Erreur dashboard")
                }
            } catch (e: Exception) {
                _dashboardState.value = DashboardState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun loadClientDashboard() {
        viewModelScope.launch {
            _dashboardState.value = DashboardState.Loading
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.getDashboardClient(token)
                android.util.Log.d("SeraLink", "Dashboard code: ${response.code()}")
                if (response.isSuccessful) {
                    val data = response.body()!!
                    android.util.Log.d("SeraLink", "Proposals count: ${data.recentProposals.size}")
                    android.util.Log.d("SeraLink", "Jobs count: ${data.jobs.size}")
                    _dashboardState.value = DashboardState.ClientSuccess(data)
                } else {
                    android.util.Log.e("SeraLink", "Erreur dashboard: ${response.code()}")
                    _dashboardState.value = DashboardState.Error("Erreur dashboard")
                }
            } catch (e: Exception) {
                android.util.Log.e("SeraLink", "Exception dashboard: ${e.message}")
                _dashboardState.value = DashboardState.Error("Erreur : ${e.message}")
            }
        }
    }
}