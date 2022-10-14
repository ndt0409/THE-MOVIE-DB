package com.ndt.themoviedb.ui.favorite

import android.widget.Toast
import androidx.core.view.isVisible
import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.local.MovieLocalDataSource
import com.ndt.themoviedb.data.source.local.dao.FavoritesDaoImpl
import com.ndt.themoviedb.data.source.remote.MovieRemoteDataSource
import com.ndt.themoviedb.databinding.FragmentFavoriteBinding
import com.ndt.themoviedb.ui.base.BaseFragment
import com.ndt.themoviedb.ui.favorite.adapter.FavoriteAdapter
import com.ndt.themoviedb.utils.constant.UrlConstant
import com.ndt.themoviedb.utils.extension.addFragment
import com.ndt.themoviedb.utils.showSnackBar
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import java.util.*
import kotlin.concurrent.schedule

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate),
    FavoriteContract.View {
    private lateinit var presenter: FavoriteContract.Presenter
    var favoriteAdapter: FavoriteAdapter? = null

    override fun initData() {
        context?.let {
            val movieRepository: MovieRepository =
                MovieRepository.getInstance(
                    MovieRemoteDataSource.getInstance(),
                    MovieLocalDataSource.getInstance(FavoritesDaoImpl.getInstance(it))
                )
            presenter = FavoritePresenter(movieRepository)
            presenter.setView(this)
            Timer().schedule(DELAY_TIME) {
                presenter.getFavorite()
            }
        }
    }

    override fun onGetFavoritesSuccess(favorites: MutableList<Favorite>) {
        favoriteAdapter = FavoriteAdapter(favorites)
        viewBinding.run {
            recyclerFavoriteMovie.setHasFixedSize(true)
            recyclerFavoriteMovie.adapter = favoriteAdapter?.apply {
                onItemClick = { category, _ ->
                    addFragment(category)
                }
            }?.apply {
                onItemDelete = { favorite, i ->
                    presenter.deleteFavorite(i, favorite.id)
                }
            }
        }
    }

    override fun updateFavoritesAfterRemovingItem(position: Int) {
        favoriteAdapter?.removeData(position)
    }

    override fun notifyDeleteFavorite(type: String) {
        if (type == UrlConstant.BASE_NOTIFY_DELETE_FAVORITE_SUCCESS) {
            activity?.showSnackBar(R.string.notification_delete_success)
        } else {
            activity?.showSnackBar(R.string.notification_delete_failed)
        }
    }

    override fun onError(exception: Exception?) {
        exception?.let {
            Toast.makeText(activity, it.message.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onLoading(isLoad: Boolean) {
        view?.run {
            if (isLoad) {
                swipe_refresh_favorite.isRefreshing = false
                frame_progressbar_movie.isVisible = false
            } else {
                frame_progressbar_movie.isVisible = true
            }
        }
    }

    override fun initAdapter() {
        TODO("Not yet implemented")
    }

    companion object {
        const val DELAY_TIME = 500L
    }
}
