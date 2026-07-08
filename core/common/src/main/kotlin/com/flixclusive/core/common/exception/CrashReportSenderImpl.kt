package com.flixclusive.core.common.exception

import android.content.Context
import com.flixclusive.core.common.BuildConfig
import com.flixclusive.core.common.R
import com.flixclusive.core.common.dispatchers.AppDispatchers
import com.flixclusive.core.util.android.showToast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

private const val GITHUB_API_BASE = "https://api.github.com"
private const val REPO_OWNER = "buddhistblueberry"
private const val REPO_NAME = "Flixclusive"

internal class CrashReportSenderImpl
    @Inject
    constructor(
        private val client: OkHttpClient,
        private val dispatchers: AppDispatchers,
        @ApplicationContext private val context: Context,
    ) : CrashReportSender {
        override suspend fun send(errorLog: String) {
            withContext(dispatchers.io) {
                val token = BuildConfig.GITHUB_TOKEN
                if (token.isBlank()) {
                    // No token in CI builds — silently skip to avoid toast spam
                    return@withContext
                }

                // ... rest of the function remains

                val title = "Crash Report: ${errorLog.lines().firstOrNull()?.take(80) ?: "Unknown"}"
                val body = """
                    ## Crash Report
                    
                    ```
                    $errorLog
                    ```
                    
                    _Auto-reported by Flixclusive Crash Reporter_
                """.trimIndent()

                val json = JSONObject()
                    .put("title", title)
                    .put("body", body)

                val requestBody = json.toString()
                    .toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("$GITHUB_API_BASE/repos/$REPO_OWNER/$REPO_NAME/issues")
                    .header("Authorization", "Bearer $token")
                    .header("Accept", "application/vnd.github.v3+json")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    withContext(dispatchers.main) {
                        val errorMessage = context.getString(R.string.failed_to_send_crash_report)
                        context.showToast(errorMessage)
                    }
                }
            }
        }
    }
