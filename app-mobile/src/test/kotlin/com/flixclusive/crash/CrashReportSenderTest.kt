package com.flixclusive.crash

import com.flixclusive.core.common.exception.CrashReportSender
import com.flixclusive.core.util.network.okhttp.HttpMethod
import com.flixclusive.core.util.network.okhttp.formRequest
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isTrue

private const val GITHUB_API_BASE = "https://api.github.com"
private const val REPO_OWNER = "buddhistblueberry"
private const val REPO_NAME = "Flixclusive"

class CrashReportSenderTest : CrashReportSender {
    private lateinit var client: OkHttpClient

    private fun generateDirtyClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    @Before
    fun setUp() {
        client = generateDirtyClient()
    }

    @Test
    fun `send function should return true`() = runTest {
        val errorMessage = """
            ========== ERROR-SAMPLE ===========

            LOREM IPSUM LOREM IPSUM LOREM IPSUM
            LOREM IPSUM LOREM IPSUM LOREM IPSUM
            LOREM IPSUM LOREM IPSUM LOREM IPSUM
            LOREM IPSUM LOREM IPSUM LOREM IPSUM
            ===================================
        """.trimIndent()

        send(errorMessage)
    }

    override suspend fun send(errorLog: String) {
        val title = "Test Crash: ${errorLog.lines().firstOrNull()?.take(80) ?: "Unknown"}"
        val body = """
            ## Test Crash Report
            
            ```
            $errorLog
            ```
            
            _Auto-reported by test suite_
        """.trimIndent()

        val json = JSONObject()
            .put("title", title)
            .put("body", body)

        val requestBody = json.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$GITHUB_API_BASE/repos/$REPO_OWNER/$REPO_NAME/issues")
            .header("Authorization", "Bearer ${System.getenv("GITHUB_TOKEN") ?: ""}")
            .header("Accept", "application/vnd.github.v3+json")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        expectThat(response.isSuccessful).isTrue()
    }
}
