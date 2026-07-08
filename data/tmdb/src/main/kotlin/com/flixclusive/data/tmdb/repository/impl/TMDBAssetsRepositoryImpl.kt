package com.flixclusive.data.tmdb.repository.impl

import com.flixclusive.core.common.dispatchers.AppDispatchers
import com.flixclusive.core.network.retrofit.TMDBApiService
import com.flixclusive.core.network.util.Resource
import com.flixclusive.core.network.util.Resource.Failure.Companion.toNetworkException
import com.flixclusive.data.tmdb.repository.TMDBAssetsRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TMDBAssetsRepositoryImpl @Inject constructor(
    private val tmdbApiService: TMDBApiService,
    private val appDispatchers: AppDispatchers,
) : TMDBAssetsRepository {

    override suspend fun getLogo(
        mediaType: String,
        id: Int,
    ): Resource<String> {
        return withContext(appDispatchers.io) {
            try {
                val response = tmdbApiService.getImages(
                    mediaType = mediaType,
                    id = id,
                )

                val logo = response.logos?.firstOrNull()?.filePath?.replace("svg", "png")
                    ?: return@withContext Resource.Failure(Exception("No logo found"))

                Resource.Success(logo)
            } catch (e: Exception) {
                e.toNetworkException()
            }
        }
    }

    override suspend fun getPosterWithoutLogo(
        mediaType: String,
        id: Int,
    ): Resource<String> {
        return withContext(appDispatchers.io) {
            try {
                val response = tmdbApiService.getImages(
                    mediaType = mediaType,
                    id = id,
                    includeImageLanguage = null,
                )

                val poster = response.posters?.firstOrNull()?.filePath
                    ?: return@withContext Resource.Failure(Exception("No poster found"))
                Resource.Success(poster)
            } catch (e: Exception) {
                e.toNetworkException()
            }
        }
    }
}
