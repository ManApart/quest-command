package system.connection

import core.history.GameLogger
import core.history.TerminalPrinter
import core.history.displayGlobal
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import system.connection.WebClient.doPolling
import kotlin.coroutines.*
import kotlin.js.Promise


actual object WebClient {
    private val client by lazy { buildWebClient() }
    actual var doPolling = false
    actual var host = "http://localhost"
    actual var port = "8080"
    actual var latestResponse = 0
    actual var latestSubResponse = 0
    actual var playerName = "Player"
    actual var latestInfo = ServerInfo()

    private fun buildWebClient(): HttpClient {
        return HttpClient(Js) { install(ContentNegotiation) { json() } }
    }


    actual fun createServerConnectionIfPossible(host: String, port: String, playerName: String, callback: (ServerInfo) -> Unit) {
        getServerInfo(host, port) { info ->
            latestInfo = info
            if (latestInfo.validServer) {
                this.host = host
                this.port = port
                this.playerName = playerName
                if (latestInfo.playerNames.none { it.lowercase() == playerName.lowercase() }) {
                    GlobalScope.launch {
                        latestInfo = createPlayer(playerName)
                        callback(latestInfo)
                    }
                } else {
                    callback(latestInfo)
                }
            } else {
                callback(latestInfo)
            }
        }
    }

    actual fun getServerInfo(host: String, port: String, callback: (ServerInfo) -> Unit) {
        latestInfo = ServerInfo()
        GlobalScope.launch {
            try {
                println("Launching")
                latestInfo = client.get("$host:$port/info").body()
                println("Got info: $latestInfo")
            } catch (e: Exception) {
                println("Caught exception $e")
                ServerInfo()
            }
            println("returning $latestInfo")
            callback(latestInfo)
        }
    }

    private suspend fun createPlayer(name: String): ServerInfo {
        return try {
            client.post("$host:$port/$name").body()
        } catch (e: Exception) {
            ServerInfo()
        }
    }

    actual fun sendCommand(line: String, callback: (List<String>) -> Unit) {
        GlobalScope.launch {
            val responses = try {
                val response: ServerResponse = client.post("$host:$port/$playerName/command") {
                    parameter("start", latestResponse)
                    parameter("startSub", latestSubResponse)
                    setBody(line)
                }.body()

                this@WebClient.latestResponse = response.latestResponse
                this@WebClient.latestSubResponse = response.latestSubResponse
                response.history
            } catch (e: Exception) {
                listOf("Unable to hit server.")
            }
            callback(responses)
        }
    }

    actual fun pollForUpdates() {
        doPolling = true
        GlobalScope.launch {
            while (doPolling) {
                if (latestInfo.validServer) {
                    withContext(Dispatchers.Default) {
                        val updates = getServerUpdates()
                        if (updates.isNotEmpty() && !updates.first().startsWith("No history for")) {
                            updates.forEach { displayGlobal(it) }
                            GameLogger.endCurrent()
                            TerminalPrinter.print()
                        }
                        delay(1000)
                    }
                }
            }
        }
    }

    actual fun getServerHistory(callback: (List<String>) -> Unit) {
        GlobalScope.launch {
            callback(getServerUpdates())
        }
    }

    private suspend fun getServerUpdates(): List<String> {
        return try {
            val response: ServerResponse = client.get("$host:$port/$playerName/history") {
                parameter("start", latestResponse)
                parameter("startSub", latestSubResponse)
            }.body()

            this@WebClient.latestResponse = response.latestResponse
            this@WebClient.latestSubResponse = response.latestSubResponse
            response.history
        } catch (e: Exception) {
            listOf("Unable to hit server")
        }
    }
}

//suspend fun <T> Promise<T>.await(): T = suspendCoroutine<T> { cont ->
//    then { cont.resume(it) }
//}
//
//fun <T> async2(block: suspend () -> T) = Promise<T> { resolve, reject ->
//    block.startCoroutine(object : Continuation<T> {
//        override val context: CoroutineContext = EmptyCoroutineContext
//
//        override fun resumeWith(result: Result<T>) {
//            resolve(result.getOrNull()!!)
//        }
//    })
//}

//fun launch(block: suspend () -> Unit): Unit {
//    block.startCoroutine(object : Continuation<Unit> {
//        override val context = EmptyCoroutineContext
//        override fun resumeWith(result: Result<Unit>) {}
//    })
//}