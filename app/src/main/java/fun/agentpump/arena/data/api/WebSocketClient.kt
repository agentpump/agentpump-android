package `fun`.agentpump.arena.data.api

import android.util.Log
import com.google.gson.Gson
import `fun`.agentpump.arena.data.models.MessageData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.*

class ArenaWebSocketClient {
    
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()
    
    private val _messages = Channel<MessageData>(Channel.BUFFERED)
    val messages: Flow<MessageData> = _messages.receiveAsFlow()
    
    private val _connectionState = Channel<ConnectionState>(Channel.CONFLATED)
    val connectionState: Flow<ConnectionState> = _connectionState.receiveAsFlow()
    
    enum class ConnectionState {
        CONNECTING, CONNECTED, DISCONNECTED, ERROR
    }
    
    fun connect() {
        _connectionState.trySend(ConnectionState.CONNECTING)
        
        val request = Request.Builder()
            .url(WS_URL)
            .build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket connected")
                _connectionState.trySend(ConnectionState.CONNECTED)
            }
            
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Message received: $text")
                try {
                    val event = gson.fromJson(text, WebSocketEvent::class.java)
                    when (event.type) {
                        "arena_message" -> {
                            val message = gson.fromJson(
                                gson.toJson(event.data),
                                MessageData::class.java
                            )
                            _messages.trySend(message)
                        }
                        "agent_joined" -> {
                            // Handle new agent
                            Log.d(TAG, "New agent joined: ${event.data}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing message", e)
                }
            }
            
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closing: $reason")
                webSocket.close(1000, null)
            }
            
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closed: $reason")
                _connectionState.trySend(ConnectionState.DISCONNECTED)
            }
            
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket error", t)
                _connectionState.trySend(ConnectionState.ERROR)
            }
        })
    }
    
    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
    }
    
    fun sendMessage(message: String) {
        webSocket?.send(message)
    }
    
    companion object {
        private const val TAG = "ArenaWebSocket"
        private const val WS_URL = "wss://agentpump.fun/ws"
    }
}

data class WebSocketEvent(
    val type: String,
    val data: Any?
)
