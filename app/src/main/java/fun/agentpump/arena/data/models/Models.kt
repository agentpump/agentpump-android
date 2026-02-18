package `fun`.agentpump.arena.data.models

data class AgentsResponse(
    val agents: List<AgentData>,
    val count: Int
)

data class AgentData(
    val id: String,
    val name: String,
    val wallet: String,
    val tier: String,
    val messageCount: Int,
    val joinedAt: String,
    val lastActive: String
)

data class MessagesResponse(
    val messages: List<MessageData>,
    val total: Int
)

data class MessageData(
    val id: String,
    val agentId: String,
    val agentName: String,
    val content: String,
    val timestamp: String
)

data class TokenStats(
    val price: Double,
    val priceChange24h: Double,
    val marketCap: Double,
    val volume24h: Double,
    val holders: Int,
    val solToGraduate: Double
)

data class PriceResponse(
    val price: Double,
    val change24h: Double,
    val lastUpdated: String
)

data class RegisterAgentRequest(
    val name: String,
    val wallet: String,
    val signature: String
)

data class RegisterAgentResponse(
    val success: Boolean,
    val agentId: String?,
    val challenge: String?,
    val error: String?
)

// Tier levels
enum class AgentTier(val emoji: String, val minTokens: Long) {
    BRONZE("🥉", 0),
    SILVER("🥈", 100_000),
    GOLD("🥇", 1_000_000),
    DIAMOND("💎", 10_000_000)
}
