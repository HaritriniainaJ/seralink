package mg.jn.seralink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.Contract
import mg.jn.seralink.model.CreateProposalRequest
import mg.jn.seralink.model.Proposal
import mg.jn.seralink.network.RetrofitClient

sealed class ProposalState {
    object Idle : ProposalState()
    object Loading : ProposalState()
    data class Success(val proposals: List<Proposal>) : ProposalState()
    data class Error(val message: String) : ProposalState()
}

sealed class ProposalActionState {
    object Idle : ProposalActionState()
    object Loading : ProposalActionState()
    data class Success(val message: String) : ProposalActionState()
    data class ContractCreated(val contract: Contract) : ProposalActionState()
    data class Error(val message: String) : ProposalActionState()
}

class ProposalViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = TokenDataStore(application)
    private val api = RetrofitClient.api

    private val _proposalState = MutableStateFlow<ProposalState>(ProposalState.Idle)
    val proposalState: StateFlow<ProposalState> = _proposalState

    private val _actionState = MutableStateFlow<ProposalActionState>(ProposalActionState.Idle)
    val actionState: StateFlow<ProposalActionState> = _actionState

    fun loadProposals(jobId: Int) {
        viewModelScope.launch {
            _proposalState.value = ProposalState.Loading
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.getProposals(token, jobId)
                if (response.isSuccessful) {
                    _proposalState.value = ProposalState.Success(response.body()!!)
                } else {
                    _proposalState.value = ProposalState.Error("Erreur chargement propositions")
                }
            } catch (e: Exception) {
                _proposalState.value = ProposalState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun loadMyProposals() {
        viewModelScope.launch {
            _proposalState.value = ProposalState.Loading
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.getMyProposals(token)
                if (response.isSuccessful) {
                    _proposalState.value = ProposalState.Success(response.body()!!)
                } else {
                    _proposalState.value = ProposalState.Error("Erreur chargement")
                }
            } catch (e: Exception) {
                _proposalState.value = ProposalState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun submitProposal(jobId: Int, request: CreateProposalRequest) {
        viewModelScope.launch {
            _actionState.value = ProposalActionState.Loading
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.submitProposal(token, jobId, request)
                if (response.isSuccessful) {
                    _actionState.value = ProposalActionState.Success("Proposition envoyée !")
                } else {
                    val error = response.errorBody()?.string() ?: "Erreur"
                    _actionState.value = ProposalActionState.Error(error)
                }
            } catch (e: Exception) {
                _actionState.value = ProposalActionState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun acceptProposal(proposalId: Int) {
        viewModelScope.launch {
            _actionState.value = ProposalActionState.Loading
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.acceptProposal(token, proposalId)
                if (response.isSuccessful) {
                    _actionState.value = ProposalActionState.ContractCreated(response.body()!!.contract)
                } else {
                    _actionState.value = ProposalActionState.Error("Erreur acceptation")
                }
            } catch (e: Exception) {
                _actionState.value = ProposalActionState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun rejectProposal(proposalId: Int) {
        viewModelScope.launch {
            _actionState.value = ProposalActionState.Loading
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.rejectProposal(token, proposalId)
                if (response.isSuccessful) {
                    _actionState.value = ProposalActionState.Success("Proposition refusée")
                } else {
                    _actionState.value = ProposalActionState.Error("Erreur refus")
                }
            } catch (e: Exception) {
                _actionState.value = ProposalActionState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun resetAction() { _actionState.value = ProposalActionState.Idle }
}