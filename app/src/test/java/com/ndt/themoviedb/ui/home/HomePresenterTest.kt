package com.ndt.themoviedb.ui.home

import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse
import com.ndt.themoviedb.ui.favorite.FavoriteContract
import com.ndt.themoviedb.ui.favorite.FavoritePresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class HomePresenterTest {
    private val view = mockk<HomeContract.View>(relaxed = true)
    private val presenter by lazy { HomePresenter(repository) }
    private val repository = mockk<MovieRepository>()
    private val genresListener = slot<OnDataLoadedCallback<GenresResponse>>()
    private val successGenresData = mock<GenresResponse>()
    private val movieListener = slot<OnDataLoadedCallback<MoviesResponse>>()
    private val successMovieData = mock<MoviesResponse>()

    private val type = "action"
    private val query = "abc"
    private val page = 120


    @Before
    fun setUp() {
        presenter.setView(view)
    }

    @Test
    fun getGenres() {
        every {
            repository.getGenres(capture(genresListener))
        } answers {
            genresListener.captured.onSuccess(successGenresData)
        }
        presenter.getGenres()

        verify {
            view.onGetGenresSuccess(successGenresData.listGenres)
        }
    }

    @Test
    fun getMovie() {
        every {
            repository.getMovies(type, query, page, capture(movieListener))
        } answers {
            movieListener.captured.onSuccess(successMovieData)
        }
        presenter.getMovie(type, query, page)

        verify {
            view.onGetMoviesNowPlayingSuccess(successMovieData.listMovie)
            view.onGetMoviesPopularSuccess(successMovieData.listMovie)
            view.onGetMoviesUpcomingSuccess(successMovieData.listMovie)
            view.onGetMoviesByGenresIDSuccess(successMovieData.listMovie)
        }
    }
}
