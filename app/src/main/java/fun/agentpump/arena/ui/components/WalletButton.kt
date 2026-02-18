package `fun`.agentpump.arena.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `fun`.agentpump.arena.ui.theme.*

@Composable
fun WalletButton(
    isConnected: Boolean,
    walletAddress: String?,
    isConnecting: Boolean,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isConnected && walletAddress != null) {
        // Connected state
        OutlinedButton(
            onClick = onDisconnectClick,
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = NeonGreen
            )
        ) {
            Icon(
                Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = shortenAddress(walletAddress),
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    } else {
        // Disconnected state
        Button(
            onClick = onConnectClick,
            modifier = modifier,
            enabled = !isConnecting,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonGreen,
                contentColor = Color.Black
            )
        ) {
            if (isConnecting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Connecting...", fontWeight = FontWeight.Bold)
            } else {
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Connect Wallet", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WalletSelectionDialog(
    onDismiss: () -> Unit,
    onSelectPhantom: () -> Unit,
    onSelectSolflare: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkCard,
        title = {
            Text(
                "Connect Wallet",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Choose your Solana wallet:",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                
                WalletOption(
                    name = "Phantom",
                    icon = "👻",
                    onClick = {
                        onSelectPhantom()
                        onDismiss()
                    }
                )
                
                WalletOption(
                    name = "Solflare",
                    icon = "☀️",
                    onClick = {
                        onSelectSolflare()
                        onDismiss()
                    }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        }
    )
}

@Composable
private fun WalletOption(
    name: String,
    icon: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                name,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}

private fun shortenAddress(address: String): String {
    return if (address.length > 10) {
        "${address.take(4)}...${address.takeLast(4)}"
    } else {
        address
    }
}
