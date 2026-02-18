package `fun`.agentpump.arena.data.repository

import `fun`.agentpump.arena.data.api.ApiClient
import `fun`.agentpump.arena.data.api.ArenaWebSocketClient
import `fun`.agentpump.arena.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ArenaRepository {
    
    private val api = ApiClient.api
    private val webSocketClient = ArenaWebSocketClient()
    
    // Live message stream
    val liveMessages: Flow<MessageData> = webSocketClient.messages
    val connectionState = webSocketClient.connectionState
    
    fun connectToArena() {
        webSocketClient.connect()
    }
    
    fun disconnectFromArena() {
        webSocketClient.disconnect()
    }
    
    suspend fun getAgents(): Result<List<AgentData>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAgents()
            Result.success(response.agents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getArenaMessages(): Result<List<MessageData>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getArenaMessages()
            Result.success(response.messages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTokenStats(): Result<TokenStats> = withContext(Dispatchers.IO) {
        try {
            val stats = api.getTokenStats()
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun registerAgent(
        name: String,
        wallet: String,
        signature: String
    ): Result<RegisterAgentResponse> = withContext(Dispatchers.IO) {
        try {
            val request = RegisterAgentRequest(name, wallet, signature)
            val response = api.registerAgent(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
