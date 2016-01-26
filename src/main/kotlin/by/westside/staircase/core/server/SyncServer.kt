package by.westside.staircase.core.server

import by.westside.staircase.core.http.request.RequestType
import by.westside.staircase.core.http.response.HttpResponse
import by.westside.staircase.core.http.response.ResponseStatus
import java.net.ServerSocket
import java.util.*

/**
 * Created by d.pranchuk on 1/20/16.c
 */
class SyncServer(val port: Int = 80, val threadsCount: Int) : Runnable {

    val serverSocket = ServerSocket(port)
    private val listeners = ArrayList<ServerListener>()

    override fun run() {
        start()
    }

    fun registerListener(serverListener: ServerListener) {
        listeners.add(serverListener)
    }

    fun getListener(path: String, requestType: RequestType): ServerListener {
        return listeners.find { it.path.equals(path) && it.requestType.equals(requestType) }
                ?: ServerListener("", RequestType.GET, { request ->
            HttpResponse(request.httpVersion, ResponseStatus.NOT_FOUND, "<h1>NOT FOUND</h1>") }
        )
    }

    private fun start() {
        println("$port port is listening by staircase")
        (1..threadsCount).forEach {
            Thread(ServerThread(it, this)).start()
        }
    }
}
