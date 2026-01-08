package com.simats.quitsmoking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.quitsmoking.model.ContactInfo
import com.simats.quitsmoking.model.HelpTopic
import com.simats.quitsmoking.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HelpSupportViewModel : ViewModel() {

    private val _topics = MutableStateFlow<List<HelpTopic>>(emptyList())
    val topics: StateFlow<List<HelpTopic>> = _topics

    private val _contact = MutableStateFlow<ContactInfo?>(null)
    val contact: StateFlow<ContactInfo?> = _contact

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadHelpData() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = RetrofitClient.api.getHelpSupport()
                if (response.isSuccessful && response.body()?.status == true) {
                    _topics.value = response.body()!!.topics
                    _contact.value = response.body()!!.contact
                } else {
                    _error.value = "Failed to load help data"
                }
            } catch (e: Exception) {
                _error.value = "Network error"
            } finally {
                _loading.value = false
            }
        }
    }
}
