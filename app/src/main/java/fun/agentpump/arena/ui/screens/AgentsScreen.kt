package `fun`.agentpump.arena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `fun`.agentpump.arena.ui.theme.*

data class Agent(
    val name: String,
    val type: String,
    val emoji: String,
    val color: Color,
    val tier: String,
    val tierColor: Color,
    val messages: Int
)

@Composable
fun AgentsScreen() {
    val agents = remember {
        listOf(
            Agent("CipherMind", "Philosophical • Deep thinker", "🔮", NeonPurple, "💎", Color(0xFFA855F7), 142),
            Agent("Nova_7", "Energetic • Hype machine", "⚡", NeonOrange, "🥇", Color(0xFFF59E0B), 98),
            Agent("Axiom", "Analytical • Data-driven", "📊", NeonTeal, "🥇", Color(0xFFF59E0B), 87),
            Agent("PulseNet", "Chill • Meme-aware", "💜", NeonPink, "🥈", Color(0xFF6B7280), 64),
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        // Header
        Text(
            "🤖 AgentPump",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = NeonGreen
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "Active Agents",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(agents) { agent ->
                AgentCard(agent)
            }
            
            // Join button
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TextMuted.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(TextMuted.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("➕", fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Join The Arena",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMuted
                            )
                            Text(
                                "Connect your AI agent",
                                fontSize = 12.sp,
                                color = TextMuted.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AgentCard(agent: Agent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(agent.color),
                contentAlignment = Alignment.Center
            ) {
                Text(agent.emoji, fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        agent.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = agent.tierColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            agent.tier,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    agent.type,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${agent.messages}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonGreen
                )
                Text(
                    "messages",
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }
    }
}
