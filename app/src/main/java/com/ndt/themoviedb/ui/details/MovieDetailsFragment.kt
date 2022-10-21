package com.ndt.themoviedb.ui.details

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.MovieTrailer
import com.ndt.themoviedb.data.model.Produce
import com.ndt.themoviedb.data.model.Cast
import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.local.MovieLocalDataSource
import com.ndt.themoviedb.data.source.local.dao.FavoritesDaoImpl
import com.ndt.themoviedb.data.source.remote.MovieRemoteDataSource
import com.ndt.themoviedb.databinding.FragmentMovieDetailsBinding
import com.ndt.themoviedb.ui.base.BaseFragment
import com.ndt.themoviedb.ui.details.adapter.CastAdapter
import com.ndt.themoviedb.ui.details.adapter.ProduceAdapter
import com.ndt.themoviedb.ui.details.adapter.TrailerAdapter
import com.ndt.themoviedb.ui.mainscreen.MainActivity
import com.ndt.themoviedb.utils.NetworkUtil
import com.ndt.themoviedb.utils.constant.UrlConstant
import com.ndt.themoviedb.utils.extension.addFragment
import com.ndt.themoviedb.utils.showSnackBar

@Suppress("Detekt.TooManyFunctions")
class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding>(FragmentMovieDetailsBinding::inflate),
    MovieDetailsContract.View {
    private lateinit var presenter: MovieDetailsContract.Presenter
    private lateinit var movieFavorite: Movie
    private val castAdapter: CastAdapter by lazy { CastAdapter() }
    private val produceAdapter: ProduceAdapter by lazy { ProduceAdapter() }
    private val trailerAdapter: TrailerAdapter by lazy { TrailerAdapter() }
    private var strMoviePoster: String? = null
    private var byteArrayPoster: ByteArray? = null

    override fun initData() {
        context?.let {
            val movieRepository: MovieRepository =
                MovieRepository.getInstance(
                    MovieRemoteDataSource.getInstance(),
                    MovieLocalDataSource.getInstance(FavoritesDaoImpl.getInstance(it))
                )
            presenter = MovieDetailsPresenter(movieRepository)
        }
        presenter.setView(this)

        viewBinding.include.toolbarBase?.let {
            (activity as? MainActivity)?.run {
                setSupportActionBar(it)
                supportActionBar?.run {
                    setDisplayShowTitleEnabled(true)
                    title = arguments?.getString(UrlConstant.BASE_TITLE)
                }
            }
            it.setNavigationOnClickListener {
                activity?.run { supportFragmentManager.popBackStack() }
            }
        }

        activity?.let { activity ->
            if (NetworkUtil.isConnectedToNetwork(activity)) {
                arguments?.let { presenter.getMovieDetails(it.getInt(UrlConstant.BASE_VALUE)) }
            } else {
                onLoading(true)
                val message = getString(R.string.check_internet_fail)
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.swipeRefreshDetail.setOnRefreshListener {
            activity?.let { activity ->
                if (NetworkUtil.isConnectedToNetwork(activity)) {
                    arguments?.let {
                        presenter.getMovieDetails(it.getInt(UrlConstant.BASE_VALUE))
                    }
                } else {
                    onLoading(true)
                    val message = getString(R.string.check_internet_fail)
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewBinding.imageFavorite.setOnClickListener {
            presenter.handleFavorites(Favorite(movieFavorite, byteArrayPoster))
        }
    }

    override fun onGetMovieSuccess(movie: Movie) {
        view?.run {
            viewBinding.textVote.text = movie.voteAverage.toString()
            viewBinding.textTitle.text = movie.title
            viewBinding.textReleaseDate.text = movie.releaseDate
            viewBinding.textOverView.text = movie.overView
            presenter.getImageAsync(movie.backdropPath, viewBinding.imageBackdrop)
            presenter.getImageAsync(movie.posterPath, viewBinding.imgPoster)
            strMoviePoster = movie.posterPath
            viewBinding.imageBackdrop.animation =
                AnimationUtils.loadAnimation(activity, R.anim.scale_animation)
            movieFavorite = movie
        }
    }

    override fun onGetGenresSuccess(genres: List<Genres>) {
        viewBinding.run {
            for (item in genres) {
                val genresChip = LayoutInflater.from(activity)
                    .inflate(R.layout.item_chip, viewBinding.chipGroupGenres, false) as Chip
                genresChip.id = item.genresID
                genresChip.text = item.genresName
                genresChip.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        addFragment(
                            UrlConstant.BASE_GENRES_ID,
                            buttonView.id.toString(),
                            buttonView.text.toString()
                        )
                    }
                }
                viewBinding.chipGroupGenres.addView(genresChip)
            }
        }
    }

    override fun onGetCastsSuccess(casts: List<Cast>) {
        castAdapter.updateData(casts)
    }

    override fun onGetProducesSuccess(produces: List<Produce>) {
        produceAdapter.updateData(produces)
    }

    override fun onGetMovieTrailerSuccess(movieTrailers: List<MovieTrailer>) {
        trailerAdapter.updateData(movieTrailers)
    }

    override fun showFavoriteImage(type: String) {
        if (type == UrlConstant.BASE_NOTIFY_ADD_FAVORITE_SUCCESS) {
            viewBinding.imageFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            viewBinding.imageFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    override fun notifyFavorite(type: String) {
        when (type) {
            UrlConstant.BASE_NOTIFY_ADD_FAVORITE_SUCCESS ->
                activity?.showSnackBar(R.string.notification_add_favorite_success)
            UrlConstant.BASE_NOTIFY_ADD_FAVORITE_ERROR ->
                activity?.showSnackBar(R.string.notification_add_favorite_failed)
            UrlConstant.BASE_NOTIFY_DELETE_FAVORITE_SUCCESS ->
                activity?.showSnackBar(R.string.notification_delete_success)
            UrlConstant.BASE_NOTIFY_DELETE_FAVORITE_ERROR ->
                activity?.showSnackBar(R.string.notification_delete_failed)
        }
    }

    override fun onLoading(isLoad: Boolean) {
        viewBinding.run {
            if (isLoad) {
                swipeRefreshDetail.isRefreshing = false
                frameProgressbarMovie.isVisible = false
            } else {
                frameProgressbarMovie.isVisible = true
            }
        }
    }

    override fun onError(exception: Exception?) {
        exception?.let {
            Toast.makeText(activity, it.message.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun initAdapter() {
        viewBinding.run {
            recyclerCasts.setHasFixedSize(true)
            recyclerCasts.adapter = castAdapter.apply {
                onItemClick = { cast, _ ->
                    addFragment(UrlConstant.BASE_CAST_ID, cast.id.toString(), cast.name)
                }
            }
            recyclerProduces.setHasFixedSize(true)
            recyclerProduces.adapter = produceAdapter.apply {
                onItemClick = { produce, _ ->
                    addFragment(
                        UrlConstant.BASE_PRODUCE_ID,
                        produce.produceID.toString(),
                        produce.produceName
                    )
                }
            }
            recyclerMoviesTrailer.setHasFixedSize(true)
            recyclerMoviesTrailer.adapter = trailerAdapter.apply {
                onItemClick = { movieTrailer, _ ->
                    watchYoutubeVideo(movieTrailer.trailerKey)
                }
            }
        }
    }

    private fun watchYoutubeVideo(id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        context?.startActivity(appIntent)
        context?.startActivity(webIntent)
    }

    companion object {
        fun getInstance(id: Int, title: String) = MovieDetailsFragment().apply {
            arguments = bundleOf(UrlConstant.BASE_VALUE to id, UrlConstant.BASE_TITLE to title)
        }
    }
}
