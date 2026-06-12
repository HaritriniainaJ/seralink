package mg.jn.seralink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mg.jn.seralink.data.TokenDataStore

class ContractViewModelFactory(private val dataStore: TokenDataStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ContractViewModel(dataStore) as T
    }
}