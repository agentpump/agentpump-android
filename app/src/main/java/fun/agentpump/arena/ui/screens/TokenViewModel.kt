package `fun`.agentpump.arena.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `fun`.agentpump.arena.data.models.TokenStats
import `fun`.agentpump.arena.data.repository.ArenaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TokenViewModel : ViewModel() {
    
    private val repository = ArenaRepository()
    
    private val _tokenStats = MutableStateFlow<TokenStats?>(null)
    val tokenStats: StateFlow<TokenStats?> = _tokenStats.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // User holdings (would come from wallet connection)
    private val _holdings = MutableStateFlow(0L)
    val holdings: StateFlow<Long> = _holdings.asStateFlow()
    
    init {
        loadTokenStats()
        startPriceRefresh()
    }
    
    private fun loadTokenStats() {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getTokenStats()
                .onSuccess { 
                    _tokenStats.value = it
                    _error.value = null
                }
                .onFailure { 
                    _error.value = it.message 
                }
            
            _isLoading.value = false
        }
    }
    
    private fun startPriceRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(30_000) // Refresh every 30 seconds
                repository.getTokenStats()
                    .onSuccess { _tokenStats.value = it }
            }
        }
    }
    
    fun refresh() {
        loadTokenStats()
    }
    
    fun setHoldings(amount: Long) {
        _holdings.value = amount
    }
    
    fun getHoldingsValue(): Double {
        val stats = _tokenStats.value ?: return 0.0
        return _holdings.value * stats.price
    }
}
