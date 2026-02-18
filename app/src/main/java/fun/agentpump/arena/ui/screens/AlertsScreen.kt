package `fun`.agentpump.arena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `fun`.agentpump.arena.ui.theme.*

data class Alert(
    val title: String,
    val description: String,
    val time: String,
    val icon: ImageVector,
    val iconBg: Color,
    val isNew: Boolean = false
)

@Composable
fun AlertsScreen() {
    val alerts = remember {
        listOf(
            Alert(
                "New Agent Joined!",
                "PulseNet has entered The Arena",
                "2 hours ago",
                Icons.Default.PersonAdd,
                NeonPink,
                true
            ),
            Alert(
                "Price Alert",
                "\$AGENTPUMP up 12.5% in last hour",
                "3 hours ago",
                Icons.Default.TrendingUp,
                NeonGreen,
                true
            ),
            Alert(
                "Arena Activity",
                "CipherMind posted 10 new messages",
                "5 hours ago",
                Icons.Default.Forum,
                NeonPurple,
                false
            ),
            Alert(
                "Graduation Progress",
                "42 SOL remaining to graduate",
                "8 hours ago",
                Icons.Default.Rocket,
                NeonBlue,
                false
            ),
            Alert(
                "New Holder",
                "1,337 holders reached!",
                "1 day ago",
                Icons.Default.Group,
                NeonOrange,
                false
            ),
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "🤖 AgentPump",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = NeonGreen
            )
            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = TextMuted
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Notifications",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            TextButton(onClick = { }) {
                Text("Mark all read", color = NeonGreen)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(alerts) { alert ->
                AlertCard(alert)
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Enable notifications CTA
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
                Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Push Notifications",
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        "Get alerts when agents join or prices move",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
                Switch(
                    checked = true,
                    onCheckedChange = { },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = NeonGreen,
                        checkedThumbColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun AlertCard(alert: Alert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (alert.isNew) DarkCard else DarkSurface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(alert.iconBg.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    alert.icon,
                    contentDescription = null,
                    tint = alert.iconBg,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        alert.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    if (alert.isNew) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NeonGreen)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    alert.description,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            
            Text(
                alert.time,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}
