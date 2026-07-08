package com.flixclusive.domain.catalog.usecase.impl

import com.flixclusive.core.network.util.Resource
import com.flixclusive.core.util.exception.safeCall
import com.flixclusive.data.tmdb.repository.TMDBDiscoverCatalogRepository
import com.flixclusive.data.tmdb.repository.TMDBFilmSearchItemsRepository
import com.flixclusive.data.tmdb.repository.TMDBMetadataRepository
import com.flixclusive.domain.catalog.R
import com.flixclusive.domain.catalog.usecase.GetHomeHeaderUseCase
import com.flixclusive.model.film.Film
import com.flixclusive.model.film.FilmSearchItem
import javax.inject.Inject
import kotlin.random.Random

private const val MAX_RETRIES = 50
private const val MIN_VOTE_COUNT = 50
private typealias CatalogUrl = String
private typealias FilmId = String

internal class GetHomeHeaderUseCaseImpl
    @Inject
    constructor(
        private val tmdbMetadataRepository: TMDBMetadataRepository,
        private val tmdbFilmSearchItemsRepository: TMDBFilmSearchItemsRepository,
        private val tmdbDiscoverCatalogRepository: TMDBDiscoverCatalogRepository,
    ) : GetHomeHeaderUseCase {
        override suspend fun invoke(): Resource<Film> {
            val catalogs = tmdbDiscoverCatalogRepository.getMovies() + tmdbDiscoverCatalogRepository.getTv()

            val traversedFilms = mutableSetOf<FilmId>()
            val traversedCatalogs = mutableSetOf<CatalogUrl>()

            var lastError: Exception? = null
            for (i in 0 until MAX_RETRIES) {
                try {
                    val randomIndex = Random.nextInt(catalogs.size)
                    val catalog = catalogs[randomIndex]

                    if (catalog.url in traversedCatalogs) {
                        continue
                    }

                    traversedCatalogs.add(catalog.url)

                    val filmSearchItems = tmdbFilmSearchItemsRepository
                        .get(
                            url = catalog.url,
                            page = 1,
                        ).data
                        ?.results ?: emptyList()

                    filmSearchItems.shuffled().forEach { headerItem ->
                        if (headerItem.identifier in traversedFilms) {
                            return@forEach
                        }

                        traversedFilms.add(headerItem.identifier)

                        if (headerItem.isNotPopular) {
                            return@forEach
                        }

                        // Use header item directly (FilmSearchItem implements Film)
                        return Resource.Success(headerItem)
                    }
                } catch (e: Exception) {
                    lastError = e
                }
            }

            return lastError?.let { Resource.Failure(it) }
                ?: Resource.Failure(R.string.failure_looking_for_header_item)
        }

        private val Film.isNotPopular: Boolean
            get() =
                safeCall {
                    (this as? FilmSearchItem)?.run {
                        isFromTmdb && voteCount < MIN_VOTE_COUNT
                    } == true
                } == true
    }
