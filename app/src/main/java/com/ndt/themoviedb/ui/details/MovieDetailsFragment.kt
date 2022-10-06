package com.ndt.themoviedb.ui.details

import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.Cast
import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.model.Produce
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.local.MovieLocalDataSource
import com.ndt.themoviedb.data.source.remote.MovieRemoteDataSource
import com.ndt.themoviedb.databinding.FragmentMovieDetailsBinding
import com.ndt.themoviedb.ui.base.BaseFragment
import com.ndt.themoviedb.ui.details.adapter.CastAdapter
import com.ndt.themoviedb.ui.details.adapter.ProduceAdapter
import com.ndt.themoviedb.ui.details.adapter.TrailerAdapter
import com.ndt.themoviedb.ui.utils.NetworkUtil
import com.ndt.themoviedb.ui.utils.constant.UrlConstant
import com.ndt.themoviedb.ui.utils.extension.addFragment

class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding>(FragmentMovieDetailsBinding::inflate),
    MovieDetailsContract.View {
    private lateinit var presenter: MovieDetailsContract.Presenter
    private val castAdapter: CastAdapter by lazy { CastAdapter() }
    private val produceAdapter: ProduceAdapter by lazy { ProduceAdapter() }
    private val trailerAdapter: TrailerAdapter by lazy { TrailerAdapter() }

    override fun initData() {
        val movieRepository: MovieRepository =
            MovieRepository.getInstance(
                MovieRemoteDataSource.getInstance(),
                MovieLocalDataSource.getInstance()
            )
        presenter = MovieDetailsPresenter(movieRepository)
        presenter.setView(this)
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
    }

    override fun onGetMovieSuccess(movie: Movie) {
        view?.run {
            viewBinding.textVote.text = movie.voteAverage.toString()
            viewBinding.textTitle.text = movie.title
            viewBinding.textReleaseDate.text = movie.releaseDate
            viewBinding.textOverView.text = movie.overView
            presenter.getImageAsync(movie.backdropPath, viewBinding.imageBackdrop)
            presenter.getImageAsync(movie.posterPath, viewBinding.imgPoster)
            viewBinding.imageBackdrop.animation =
                AnimationUtils.loadAnimation(activity, R.anim.scale_animation)
        }
    }

    override fun onGetGenresSuccess(genres: List<Genres>) {
        view?.run {
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

    override fun onLoading(isLoad: Boolean) {
        view?.run {
            if (isLoad) {
                viewBinding.swipeRefreshDetail.isRefreshing = false
                viewBinding.frameProgressbarMovie.isVisible = false
            } else {
                viewBinding.frameProgressbarMovie.isVisible = true
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
        view?.run {
            viewBinding.recyclerCasts.setHasFixedSize(true)
            viewBinding.recyclerCasts.adapter = castAdapter.apply {
                onItemClick = { cast, _ ->
                    addFragment(UrlConstant.BASE_CAST_ID, cast.id.toString(), cast.name)
                }
            }
            viewBinding.recyclerProduces.setHasFixedSize(true)
            viewBinding.recyclerProduces.adapter = produceAdapter.apply {
                onItemClick = { produce, _ ->
                    addFragment(
                        UrlConstant.BASE_PRODUCE_ID,
                        produce.produceID.toString(),
                        produce.produceName
                    )
                }
            }
            viewBinding.recyclerMoviesTrailer.setHasFixedSize(true)
            viewBinding.recyclerMoviesTrailer.adapter = trailerAdapter.apply {
                onItemClick = { _, _ ->
                    //TODO something
                }
            }
        }
    }

    companion object {
        fun getInstance(id: Int, title: String) = MovieDetailsFragment().apply {
            arguments = bundleOf(UrlConstant.BASE_VALUE to id, UrlConstant.BASE_TITLE to title)
        }
    }
}
