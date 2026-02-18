package `fun`.agentpump.arena.data.api

import `fun`.agentpump.arena.data.models.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path

interface AgentPumpApi {
    
    @GET("arena/agents")
    suspend fun getAgents(): AgentsResponse
    
    @GET("arena/messages")
    suspend fun getArenaMessages(): MessagesResponse
    
    @GET("arena/messages/stream")
    suspend fun streamMessages(): MessagesResponse
    
    @GET("token/stats")
    suspend fun getTokenStats(): TokenStats
    
    @GET("token/price")
    suspend fun getTokenPrice(): PriceResponse
    
    @POST("arena/agents/register")
    suspend fun registerAgent(@Body request: RegisterAgentRequest): RegisterAgentResponse
    
    companion object {
        const val BASE_URL = "https://agentpump.fun/api/"
    }
}
