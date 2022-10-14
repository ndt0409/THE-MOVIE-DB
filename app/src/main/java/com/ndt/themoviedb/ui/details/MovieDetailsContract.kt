package com.ndt.themoviedb.ui.details

import android.widget.ImageView
import com.ndt.themoviedb.data.model.Cast
import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.model.Produce
import com.ndt.themoviedb.data.model.MovieTrailer
import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.ui.base.BasePresenter

class MovieDetailsContract {

    interface View {
        fun onGetMovieSuccess(movie: Movie)
        fun onGetGenresSuccess(genres: List<Genres>)
        fun onGetCastsSuccess(casts: List<Cast>)
        fun onGetProducesSuccess(produces: List<Produce>)
        fun onGetMovieTrailerSuccess(movieTrailers: List<MovieTrailer>)
        fun showFavoriteImage(type: String)
        fun notifyFavorite(type: String)
        fun onLoading(isLoad: Boolean)
        fun onError(exception: Exception?)
    }

    interface Presenter : BasePresenter<View?> {
        fun getMovieDetails(movieID: Int)
        fun getImageAsync(strUrl: String?, imageView: ImageView)
        fun handleFavorites(favorite: Favorite)
    }
}
