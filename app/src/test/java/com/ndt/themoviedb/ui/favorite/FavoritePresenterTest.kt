package com.ndt.themoviedb.ui.favorite

import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import com.ndt.themoviedb.data.source.remote.response.GenresResponse
import com.ndt.themoviedb.data.source.remote.response.MoviesResponse
import com.ndt.themoviedb.ui.search.SearchContract
import com.ndt.themoviedb.ui.search.SearchPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class FavoritePresenterTest {
    private val view = mockk<FavoriteContract.View>(relaxed = true)
    private val presenter by lazy { FavoritePresenter(repository) }
    private val repository = mockk<MovieRepository>()
    private val favoriteListener = slot<OnDataLoadedCallback<MutableList<Favorite>>>()
    private val successFavoriteData = mock<MutableList<Favorite>>()

    @Before
    fun setUp() {
        presenter.setView(view)
    }

    @Test
    fun getFavorite() {
        every {
            repository.getFavorites(capture(favoriteListener))
        } answers {
            favoriteListener.captured.onSuccess(successFavoriteData)
        }
        presenter.getFavorite()

        verify {
            view.onGetFavoritesSuccess(successFavoriteData)
        }
    }
}
