package com.minebot.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiClient {
    private const val BASE_URL = "https://afkbotb.fly.dev"

    suspend fun getHealth(): String = withContext(Dispatchers.IO) {
        val url = URL("$BASE_URL/health")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10000
            readTimeout = 10000
        }

        try {
            val input = if (connection.responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream ?: connection.inputStream
            }

            BufferedReader(InputStreamReader(input)).use { reader ->
                reader.readText()
            }
        } finally {
            connection.disconnect()
        }
    }
}
