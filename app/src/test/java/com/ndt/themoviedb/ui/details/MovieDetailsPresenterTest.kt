package com.ndt.themoviedb.ui.details

import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MovieDetailsResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse
import com.ndt.themoviedb.ui.search.SearchContract
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock


class MovieDetailsPresenterTest {
    private val view = mockk<MovieDetailsContract.View>(relaxed = true)
    private val repository = mockk<MovieRepository>()
    private val presenter by lazy { MovieDetailsPresenter(repository) }
    private val movieDetailListener = slot<OnDataLoadedCallback<MovieDetailsResponse>>()
    private val successDetailData = mock<MovieDetailsResponse>()
    private val id = 40

    @Before
    fun setUp() {
        presenter.setView(view)
    }

    @Test
    fun getMovieDetails() {
        every {
            repository.getMovieDetails(id, capture(movieDetailListener))
        } answers {
            movieDetailListener.captured.onSuccess(successDetailData)
        }
        presenter.getMovieDetails(id)

        verify {
            view.onGetGenresSuccess(successDetailData.genres)
            view.onGetMovieSuccess(successDetailData.movies)
            view.onGetCastsSuccess(successDetailData.casts)
            view.onGetMovieTrailerSuccess(successDetailData.trailers)
            view.onGetProducesSuccess(successDetailData.produce)
        }
    }

//    @Test
//    fun getImageAsync() {
//
//    }

//    @Test
//    fun handleFavorites() {
//
//    }
}
