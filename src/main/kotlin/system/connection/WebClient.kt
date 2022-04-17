package system.connection

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

//TODO - how do we poll for updates?
object WebClient {
    var host = "localhost"
    var port = "8080"
    private val client by lazy { HttpClient.newBuilder().build() }


    fun getServerInfo(): String {
        return "$host:$port"
    }

    fun isValidServer(host: String, port: String): Boolean {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$host:$port"))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return false
    }

    fun sendCommand(playerId: Int, line: String): List<String> {
        return listOf()
    }
}