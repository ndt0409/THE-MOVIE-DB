package com.ndt.themoviedb.ui.search

import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class SearchPresenterTest {
    private val view = mockk<SearchContract.View>(relaxed = true)
    private val presenter by lazy { SearchPresenter(repository) }
    private val repository = mockk<MovieRepository>()
    private val categoriesListener = slot<OnDataLoadedCallback<List<Category>>>()
    private val genresListener = slot<OnDataLoadedCallback<GenresResponse>>()
    private val moviesListener = slot<OnDataLoadedCallback<MoviesResponse>>()
    private val successCategoryData = mock<List<Category>>()
    private val successGenresData = mockk<GenresResponse>()
    private val successMoviesData = mockk<MoviesResponse>()
    private val exception = mockk<Exception>()

    private val type = "action"
    private val query = "abc"
    private val page = 120

    @Before
    fun setUp() {
        presenter.setView(view)
    }

    @Test
    fun `query categories name success`() {
        every {
            repository.getCategories(capture(categoriesListener))
        } answers {
            categoriesListener.captured.onSuccess(successCategoryData)
        }
        presenter.getCategories()

        verify {
            view.getCategoriesSuccess(successCategoryData)
        }
    }

    @Test
    fun `query categories name fail`() {
        every {
            repository.getCategories(capture(categoriesListener))
        } answers {
            categoriesListener.captured.onError(exception)
        }

        presenter.getCategories()

        verify {
            view.onError(exception)
        }
    }

    @Test
    fun `query genres success`() {
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
    fun `query genres fail`() {
        every {
            repository.getGenres(capture(genresListener))
        } answers {
            genresListener.captured.onError(exception)
        }

        presenter.getGenres()

        verify {
            view.onError(exception)
        }
    }

    @Test
    fun `query movies success`() {
        every {
            repository.getMovies(type, query, page, capture(moviesListener))
        } answers {
            moviesListener.captured.onSuccess(successMoviesData)
        }
        presenter.getMovies(type, query, page)

        verify {
            view.onGetMoviesTopRatedSuccess(successMoviesData.listMovie)
        }
    }
}
