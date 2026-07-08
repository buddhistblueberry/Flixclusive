package com.flixclusive.domain.catalog.usecase.impl

import com.flixclusive.core.network.util.Resource
import com.flixclusive.core.util.exception.safeCall
import com.flixclusive.data.tmdb.repository.TMDBDiscoverCatalogRepository
import com.flixclusive.data.tmdb.repository.TMDBFilmSearchItemsRepository
import com.flixclusive.data.tmdb.repository.TMDBMetadataRepository
import com.flixclusive.domain.catalog.R
import com.flixclusive.domain.catalog.usecase.GetHomeHeaderUseCase
import com.flixclusive.model.film.Film
import com.flixclusive.model.film.FilmMetadata
import com.flixclusive.model.film.FilmSearchItem
import com.flixclusive.model.film.Movie
import com.flixclusive.model.film.TvShow
import com.flixclusive.model.film.util.FilmType
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

                        // Try to get metadata, but use search item as fallback
                        val metadata = getMetadata(headerItem)
                        
                        // Create a Film object from headerItem (with metadata if available)
                        val film = if (metadata != null) {
                            when (metadata) {
                                is Movie -> metadata.copy(genres = metadata.genres)
                                is TvShow -> metadata.copy(genres = metadata.genres)
                                else -> createFilmFromSearchItem(headerItem)
                            }
                        } else {
                            createFilmFromSearchItem(headerItem)
                        }

                        return Resource.Success(film)
                    }
                } catch (e: Exception) {
                    lastError = e
                }
            }

            return lastError?.let { Resource.Failure(it) }
                ?: Resource.Failure(R.string.failure_looking_for_header_item)
        }

        private suspend fun getMetadata(film: Film): FilmMetadata? {
            requireNotNull(film.tmdbId) {
                "FilmSearchItem must have a valid TMDB ID to fetch metadata."
            }

            return when (film.filmType) {
                FilmType.MOVIE -> {
                    tmdbMetadataRepository.getMovie(film.tmdbId!!).data
                }

                FilmType.TV_SHOW -> {
                    tmdbMetadataRepository.getTvShow(film.tmdbId!!).data
                }
            }
        }

        private val Film.isNotPopular: Boolean
            get() =
                safeCall {
                    (this as? FilmSearchItem)?.run {
                        isFromTmdb && voteCount < MIN_VOTE_COUNT
                    } == true
                } == true
    
        private fun createFilmFromSearchItem(item: FilmSearchItem): Film {
            return when (item.filmType) {
                FilmType.MOVIE -> Movie(
                    identifier = item.identifier,
                    tmdbId = item.tmdbId,
                    title = item.title,
                    posterImage = item.posterImage,
                    backdropImage = item.backdropImage,
                    releaseDate = item.releaseDate,
                    rating = item.rating,
                )
                FilmType.TV_SHOW -> TvShow(
                    identifier = item.identifier,
                    tmdbId = item.tmdbId,
                    name = item.title,
                    posterImage = item.posterImage,
                    backdropImage = item.backdropImage,
                    firstAirDate = item.releaseDate,
                    rating = item.rating,
                )
            }
        }

        /**
         * Convert Film to a basic Film object for fallback
         */
        private fun Film.toFilm(): Film? {
            return when (this) {
                is FilmSearchItem -> {
                    if (this.filmType == FilmType.MOVIE) {
                        Movie(
                            identifier = this.identifier,
                            tmdbId = this.tmdbId,
                            title = this.title,
                            posterImage = this.posterImage,
                            backdropImage = this.backdropImage,
                            releaseDate = this.releaseDate,
                            rating = this.rating,
                        )
                    } else {
                        TvShow(
                            identifier = this.identifier,
                            tmdbId = this.tmdbId,
                            name = this.title,
                            posterImage = this.posterImage,
                            backdropImage = this.backdropImage,
                            firstAirDate = this.releaseDate,
                            rating = this.rating,
                        )
                    }
                }
                else -> this
            }
        }
    }
