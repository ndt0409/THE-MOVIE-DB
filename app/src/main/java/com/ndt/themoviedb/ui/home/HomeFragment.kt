package com.ndt.themoviedb.ui.home

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.local.MovieLocalDataSource
import com.ndt.themoviedb.data.source.local.dao.FavoritesDaoImpl
import com.ndt.themoviedb.data.source.remote.MovieRemoteDataSource
import com.ndt.themoviedb.databinding.FragmentHomeBinding
import com.ndt.themoviedb.ui.base.BaseFragment
import com.ndt.themoviedb.ui.home.adapter.MovieAdapter
import com.ndt.themoviedb.ui.home.adapter.SliderViewPagerAdapter
import com.ndt.themoviedb.utils.NetworkUtil
import com.ndt.themoviedb.utils.OnClickListener
import com.ndt.themoviedb.utils.constant.UrlConstant
import com.ndt.themoviedb.utils.extension.addFragment
import java.util.*
import kotlin.concurrent.schedule

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    HomeContract.View, OnClickListener<Movie> {

    private lateinit var presenter: HomeContract.Presenter
    private val upcomingAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val popularAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val movieByIDAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val movieSlideAdapter: SliderViewPagerAdapter by lazy { SliderViewPagerAdapter() }
    private var genresSelected = 0

    override fun initData() {
        context?.let {
            val movieRepository: MovieRepository =
                MovieRepository.getInstance(
                    MovieRemoteDataSource.getInstance(),
                    MovieLocalDataSource.getInstance(FavoritesDaoImpl.getInstance(it))
                )
            presenter = HomePresenter(movieRepository)
        }
        presenter.setView(this)
        activity?.let {
            if (NetworkUtil.isConnectedToNetwork(it)) {
                Timer().schedule(DELAY_TIME) {
                    presenter.onStart()
                }
            } else {
                onLoading(true)
                val message = getString(R.string.check_internet_fail)
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.homeSwipeRefresh.setOnRefreshListener {
            activity?.let {
                if (NetworkUtil.isConnectedToNetwork(it)) {
                    presenter.onStart()
                    presenter.getMovie(
                        UrlConstant.BASE_GENRES_ID,
                        genresSelected.toString()
                    )
                } else {
                    onLoading(true)
                    val message = getString(R.string.check_internet_fail)
                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        onLoading(false)
    }

    override fun onGetGenresSuccess(genres: List<Genres>) {
        view?.run {
            if (viewBinding.movieByKeyTabLayout.tabCount == 0) {
                activity?.let {
                    viewBinding.movieByKeyTabLayout.setTabTextColors(
                        ContextCompat.getColor(it, R.color.color_dark_blue),
                        ContextCompat.getColor(it, R.color.color_orange)
                    )
                }

                for (element in genres) {
                    viewBinding.movieByKeyTabLayout.addTab(
                        viewBinding.movieByKeyTabLayout.newTab().setText(element.genresName)
                    )
                }
                genresSelected = genres[0].genresID
                presenter.getMovie(
                    UrlConstant.BASE_GENRES_ID,
                    genresSelected.toString()
                )
                viewBinding.movieByKeyTabLayout.addOnTabSelectedListener(
                    object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            activity?.let {
                                if (NetworkUtil.isConnectedToNetwork(it)) {
                                    tab?.let { genresSelected = genres[it.position].genresID }
                                    presenter.getMovie(
                                        UrlConstant.BASE_GENRES_ID,
                                        genresSelected.toString()
                                    )
                                } else {
                                    onLoading(true)
                                    Toast.makeText(
                                        it,
                                        getString(R.string.check_internet_fail),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {
                            //TODO implement later
                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {
                            //TODO implement later
                        }
                    })
                viewBinding.movieByKeyTabLayout.getTabAt(0)?.select()
            }
        }
    }

    override fun onGetMoviesNowPlayingSuccess(movies: List<Movie>) {
        movieSlideAdapter.updateData(movies)
    }

    override fun onGetMoviesUpcomingSuccess(movies: List<Movie>) {
        upcomingAdapter.updateData(movies)
    }

    override fun onGetMoviesPopularSuccess(movies: List<Movie>) {
        popularAdapter.updateData(movies)
    }

    override fun onGetMoviesByGenresIDSuccess(movies: List<Movie>) {
        movieByIDAdapter.updateData(movies)
    }

    override fun onError(exception: Exception?) {
        exception?.let {
            Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLoading(isLoad: Boolean) {
        view?.let {
            if (isLoad) {
                viewBinding.homeSwipeRefresh.isRefreshing = false
                viewBinding.frameProgressBar.isVisible = false
            } else {
                viewBinding.frameProgressBar.isVisible = true
            }
        }
        view?.run {
            if (isLoad) {
                viewBinding.homeSwipeRefresh.isRefreshing = false
                viewBinding.frameProgressBar.isVisible = false
            } else {
                viewBinding.frameProgressBar.isVisible = true
            }
        }
    }

    override fun initAdapter() {
        view?.run {
            viewBinding.recyclerPopular.setHasFixedSize(true)
            viewBinding.recyclerPopular.adapter = popularAdapter.apply {
                onItemClick = { item, _ ->
                    addFragment(item)
                }
            }
            viewBinding.upcomingRecyclerView.setHasFixedSize(true)
            viewBinding.upcomingRecyclerView.adapter = upcomingAdapter.apply {
                onItemClick = { item, _ ->
                    addFragment(item)
                }
            }
            viewBinding.movieByGenresRecyclerView.setHasFixedSize(true)
            viewBinding.movieByGenresRecyclerView.adapter = movieByIDAdapter.apply {
                onItemClick = { item, _ ->
                    addFragment(item)
                }
            }
            viewBinding.sliderViewPager.adapter = movieSlideAdapter
            viewBinding.indicatorTabLayout.setupWithViewPager(
                viewBinding.sliderViewPager,
                true
            )
        }
        movieSlideAdapter.setSlideItemClickListener(this)
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    view?.run {
                        if (viewBinding.sliderViewPager.currentItem < movieSlideAdapter.count - 1) {
                            viewBinding.sliderViewPager.currentItem =
                                viewBinding.sliderViewPager.currentItem + 1
                        } else {
                            viewBinding.sliderViewPager.currentItem = 0
                        }
                    }
                }
            }
        }, UrlConstant.BASE_DELAY_SLIDE, UrlConstant.BASE_DELAY_SLIDE)
    }

    override fun click(item: Movie?) {
        item?.let { addFragment(it) }
    }

    companion object {
        const val DELAY_TIME = 500L
    }
}
