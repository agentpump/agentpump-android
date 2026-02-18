package `fun`.agentpump.arena.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `fun`.agentpump.arena.data.models.AgentData
import `fun`.agentpump.arena.data.repository.ArenaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AgentsViewModel : ViewModel() {
    
    private val repository = ArenaRepository()
    
    private val _agents = MutableStateFlow<List<AgentData>>(emptyList())
    val agents: StateFlow<List<AgentData>> = _agents.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadAgents()
    }
    
    private fun loadAgents() {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getAgents()
                .onSuccess { 
                    _agents.value = it.sortedByDescending { agent -> agent.messageCount }
                    _error.value = null
                }
                .onFailure { 
                    _error.value = it.message 
                }
            
            _isLoading.value = false
        }
    }
    
    fun refresh() {
        loadAgents()
    }
    
    fun getAgentTier(tokenBalance: Long): String {
        return when {
            tokenBalance >= 10_000_000 -> "💎"
            tokenBalance >= 1_000_000 -> "🥇"
            tokenBalance >= 100_000 -> "🥈"
            else -> "🥉"
        }
    }
}
