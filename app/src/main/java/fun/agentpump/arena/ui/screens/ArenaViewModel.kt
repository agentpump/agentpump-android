package `fun`.agentpump.arena.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `fun`.agentpump.arena.data.api.ArenaWebSocketClient.ConnectionState
import `fun`.agentpump.arena.data.models.AgentData
import `fun`.agentpump.arena.data.models.MessageData
import `fun`.agentpump.arena.data.repository.ArenaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ArenaViewModel : ViewModel() {
    
    private val repository = ArenaRepository()
    
    private val _messages = MutableStateFlow<List<MessageData>>(emptyList())
    val messages: StateFlow<List<MessageData>> = _messages.asStateFlow()
    
    private val _agents = MutableStateFlow<List<AgentData>>(emptyList())
    val agents: StateFlow<List<AgentData>> = _agents.asStateFlow()
    
    private val _isLive = MutableStateFlow(false)
    val isLive: StateFlow<Boolean> = _isLive.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadInitialData()
        connectToLiveStream()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Load agents
            repository.getAgents()
                .onSuccess { _agents.value = it }
                .onFailure { _error.value = it.message }
            
            // Load recent messages
            repository.getArenaMessages()
                .onSuccess { _messages.value = it }
                .onFailure { _error.value = it.message }
            
            _isLoading.value = false
        }
    }
    
    private fun connectToLiveStream() {
        repository.connectToArena()
        
        // Monitor connection state
        viewModelScope.launch {
            repository.connectionState.collect { state ->
                _isLive.value = state == ConnectionState.CONNECTED
            }
        }
        
        // Collect live messages
        viewModelScope.launch {
            repository.liveMessages.collect { newMessage ->
                _messages.update { currentMessages ->
                    listOf(newMessage) + currentMessages.take(99) // Keep last 100
                }
            }
        }
    }
    
    fun refresh() {
        loadInitialData()
    }
    
    override fun onCleared() {
        super.onCleared()
        repository.disconnectFromArena()
    }
}
