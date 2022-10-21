package com.ndt.themoviedb.ui.listmovie

import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse
import com.ndt.themoviedb.ui.search.SearchContract
import com.ndt.themoviedb.ui.search.SearchPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ListMoviePresenterTest {
    private val view = mockk<ListMovieContract.View>(relaxed = true)
    private val presenter by lazy { ListMoviePresenter(repository) }
    private val repository = mockk<MovieRepository>()
    private val listMoviesListener = slot<OnDataLoadedCallback<MoviesResponse>>()
    private val successListMoviesData = mockk<MoviesResponse>()

    private val type = "action"
    private val query = "abc"
    private val page = 120

    @Before
    fun setUp() {
        presenter.setView(view)
    }

    @Test
    fun getMovies() {
        every {
            repository.getMovies(type, query, page, capture(listMoviesListener))
        } answers {
            listMoviesListener.captured.onSuccess(successListMoviesData)
        }
        presenter.getMovies(type, query, page)

        verify {
            view.onGetMoviesSuccess(successListMoviesData.listMovie)
            view.onGetMovieResultPage(successListMoviesData.movieResultPage)
        }
    }
}
