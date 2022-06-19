package system.connection

import core.history.GameLogger
import core.history.TerminalPrinter
import core.history.displayGlobal
import core.history.displayToMe
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

object WebClient {
    private val client by lazy { buildWebClient() }
    var host = "http://localhost"
    var port = "8080"
    var playerName = "Player"
    var connected = false
    var activeSession: DefaultClientWebSocketSession? = null

    private fun buildWebClient(): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
            install(ContentNegotiation) { json() }
        }
    }

    fun getInfo() = "$playerName on $host:$port"

    fun connectToServer(host: String, port: String, playerName: String) {
        this.host = host
        this.port = port
        this.playerName = playerName
        //Don't block main thread
        GlobalScope.launch {
            runBlocking {
                try {
                    client.webSocket(method = HttpMethod.Get, host, port.toIntOrNull(), path = "/$playerName/command") {
                        activeSession = this
//                        activeSession?.timeoutMillis = 1000 * 1000
                        connected = true
                        println("Connected. Server info: ${getInfo()}")
                        val messageOutputRoutine = launch { receiveServerUpdates() }
                        val keepAlive = launch { keepAlive() }

                        keepAlive.join() // Wait for completion; either "exit" or error
                        messageOutputRoutine.cancelAndJoin()
                    }
                } catch (e: Exception) {
                    println("Failed to connect to server at $host:$port: ${e.message ?: e.stackTrace.firstOrNull().toString()}")
                }
                println("Websocket completed")
            }
//            client.close()
        }
    }

    fun sendCommand(line: String) {
        if (activeSession == null) connectToServer(host, port, playerName)

        if (activeSession != null) {
            runBlocking { activeSession?.send(line) }
        } else {
            println("Could not connect to server!")
        }
    }

    private suspend fun keepAlive() {
        while (connected) delay(100)
    }

    private suspend fun DefaultClientWebSocketSession.receiveServerUpdates() {
        while (connected) {
            try {
//            for (message in incoming) {
                println("Got Message")
                val updates = receiveDeserialized<List<String>>()
                println("Got Got updates")
                if (updates.isNotEmpty() && !updates.first().startsWith("No history for")) {
                    updates.forEach { displayGlobal(it) }
                    GameLogger.endCurrent()
                    TerminalPrinter.print()
                }
                println("Processed update")
//            }
            } catch (e: Exception) {
                println("Error while receiving: " + e.localizedMessage)
            }
        }
    }

}