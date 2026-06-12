package mg.jn.seralink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mg.jn.seralink.data.TokenDataStore
import mg.jn.seralink.model.Message
import mg.jn.seralink.model.SendMessageRequest
import mg.jn.seralink.network.RetrofitClient

sealed class MessageState {
    object Loading : MessageState()
    data class Success(val messages: List<Message>) : MessageState()
    data class Error(val message: String) : MessageState()
}

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = TokenDataStore(application)
    private val api = RetrofitClient.api

    private val _messageState = MutableStateFlow<MessageState>(MessageState.Loading)
    val messageState: StateFlow<MessageState> = _messageState

    private val _sendState = MutableStateFlow<String?>(null)
    val sendState: StateFlow<String?> = _sendState

    fun loadMessages(contractId: Int) {
        viewModelScope.launch {
            _messageState.value = MessageState.Loading
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.getMessages(token, contractId)
                if (response.isSuccessful) {
                    _messageState.value = MessageState.Success(response.body()!!)
                } else {
                    _messageState.value = MessageState.Error("Erreur chargement messages")
                }
            } catch (e: Exception) {
                _messageState.value = MessageState.Error("Erreur : ${e.message}")
            }
        }
    }

    fun sendMessage(contractId: Int, content: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${dataStore.token.first()}"
                val response = api.sendMessage(token, contractId, SendMessageRequest(content))
                if (response.isSuccessful) {
                    loadMessages(contractId)
                } else {
                    _sendState.value = "Erreur envoi message"
                }
            } catch (e: Exception) {
                _sendState.value = "Erreur : ${e.message}"
            }
        }
    }

    fun resetSend() { _sendState.value = null }
}