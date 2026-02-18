package `fun`.agentpump.arena.wallet

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.URLEncoder

/**
 * Solana wallet integration via deep linking
 * Supports Phantom, Solflare, and other Solana mobile wallets
 */
class SolanaWalletManager(private val context: Context) {
    
    private val _connectedWallet = MutableStateFlow<WalletConnection?>(null)
    val connectedWallet: StateFlow<WalletConnection?> = _connectedWallet.asStateFlow()
    
    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting.asStateFlow()
    
    data class WalletConnection(
        val publicKey: String,
        val walletName: String,
        val session: String? = null
    )
    
    /**
     * Connect to Phantom wallet via deep link
     */
    fun connectPhantom() {
        _isConnecting.value = true
        
        val params = buildString {
            append("app_url=${encode(APP_URL)}")
            append("&dapp_encryption_public_key=${encode(getDappPublicKey())}")
            append("&redirect_link=${encode(REDIRECT_URI)}")
            append("&cluster=mainnet-beta")
        }
        
        val uri = Uri.parse("phantom://v1/connect?$params")
        launchWalletIntent(uri, "com.phantom")
    }
    
    /**
     * Connect to Solflare wallet
     */
    fun connectSolflare() {
        _isConnecting.value = true
        
        val params = buildString {
            append("app_url=${encode(APP_URL)}")
            append("&redirect_link=${encode(REDIRECT_URI)}")
        }
        
        val uri = Uri.parse("solflare://connect?$params")
        launchWalletIntent(uri, "com.solflare.mobile")
    }
    
    /**
     * Handle callback from wallet app
     */
    fun handleCallback(uri: Uri): Boolean {
        val path = uri.path ?: return false
        
        return when {
            path.contains("onConnect") -> {
                val publicKey = uri.getQueryParameter("public_key")
                val session = uri.getQueryParameter("session")
                
                if (publicKey != null) {
                    _connectedWallet.value = WalletConnection(
                        publicKey = publicKey,
                        walletName = "Phantom",
                        session = session
                    )
                    _isConnecting.value = false
                    true
                } else false
            }
            path.contains("onDisconnect") -> {
                disconnect()
                true
            }
            else -> false
        }
    }
    
    /**
     * Sign a transaction (for buying/selling tokens)
     */
    fun signTransaction(transaction: ByteArray) {
        val wallet = _connectedWallet.value ?: return
        
        val encoded = Base64.encodeToString(transaction, Base64.NO_WRAP)
        val params = buildString {
            append("dapp_encryption_public_key=${encode(getDappPublicKey())}")
            append("&nonce=${encode(generateNonce())}")
            append("&redirect_link=${encode(REDIRECT_URI)}")
            append("&payload=${encode(encoded)}")
        }
        
        val uri = Uri.parse("phantom://v1/signTransaction?$params")
        launchWalletIntent(uri, "com.phantom")
    }
    
    /**
     * Sign a message (for agent registration verification)
     */
    fun signMessage(message: String) {
        val wallet = _connectedWallet.value ?: return
        
        val encoded = Base64.encodeToString(message.toByteArray(), Base64.NO_WRAP)
        val params = buildString {
            append("dapp_encryption_public_key=${encode(getDappPublicKey())}")
            append("&nonce=${encode(generateNonce())}")
            append("&redirect_link=${encode(REDIRECT_URI)}")
            append("&payload=${encode(encoded)}")
        }
        
        val uri = Uri.parse("phantom://v1/signMessage?$params")
        launchWalletIntent(uri, "com.phantom")
    }
    
    fun disconnect() {
        _connectedWallet.value = null
        _isConnecting.value = false
    }
    
    fun isConnected(): Boolean = _connectedWallet.value != null
    
    fun getPublicKey(): String? = _connectedWallet.value?.publicKey
    
    private fun launchWalletIntent(uri: Uri, packageName: String) {
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        // Check if wallet app is installed
        val walletIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage(packageName)
        }
        
        if (walletIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(walletIntent)
        } else {
            // Fallback to browser/play store
            context.startActivity(intent)
        }
    }
    
    private fun encode(value: String): String = 
        URLEncoder.encode(value, "UTF-8")
    
    private fun getDappPublicKey(): String {
        // In production, generate and store a keypair
        return "AgentPumpDappKey123" 
    }
    
    private fun generateNonce(): String {
        return java.util.UUID.randomUUID().toString()
    }
    
    companion object {
        private const val APP_URL = "https://agentpump.fun"
        private const val REDIRECT_URI = "agentpump://callback"
    }
}
