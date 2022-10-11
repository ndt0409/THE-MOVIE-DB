package com.ndt.themoviedb.ui.search

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.Category
import com.ndt.themoviedb.data.model.Genres
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.local.MovieLocalDataSource
import com.ndt.themoviedb.data.source.remote.MovieRemoteDataSource
import com.ndt.themoviedb.databinding.FragmentSearchBinding
import com.ndt.themoviedb.ui.base.BaseFragment
import com.ndt.themoviedb.ui.details.MovieDetailsFragment
import com.ndt.themoviedb.ui.home.adapter.MovieAdapter
import com.ndt.themoviedb.ui.listmovie.ListMovieFragment
import com.ndt.themoviedb.ui.mainscreen.MainActivity
import com.ndt.themoviedb.ui.search.adapter.CategoryAdapter
import com.ndt.themoviedb.ui.utils.NetworkUtil
import com.ndt.themoviedb.ui.utils.constant.UrlConstant
import com.ndt.themoviedb.ui.utils.extension.addFragment
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.toolbar_base.view.*
import java.util.*
import kotlin.concurrent.schedule

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate),
    SearchContract.View {

    private lateinit var presenter: SearchContract.Presenter
    private val topRatedAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter() }

    override fun initData() {
        val movieRepository: MovieRepository =
            MovieRepository.getInstance(
                MovieRemoteDataSource.getInstance(),
                MovieLocalDataSource.getInstance()
            )
        presenter = SearchPresenter(movieRepository)

        presenter.setView(this)
        onLoading(false)
        Timer().schedule(DELAY_TIME) {
            presenter.onStart()
        }

        initToolBar()
        initRefresh()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_view, menu)
        val searchView = menu.findItem(R.id.searchView).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                val fragment = ListMovieFragment.getInstance(
                    UrlConstant.BASE_SEARCH,
                    query,
                    query
                )
                addFragment(fragment)
                return false
            }
        })
    }

    override fun onGetGenresSuccess(genres: List<Genres>) {
        view?.run {
            for (item in genres) {
                val inflate = LayoutInflater.from(activity)
                val genresChip = inflate.inflate(
                    R.layout.item_chip,
                    viewBinding.chipGroupGenres,
                    false
                ) as Chip
                genresChip.id = item.genresID
                genresChip.text = item.genresName
                genresChip.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        val fragment = ListMovieFragment.getInstance(
                            UrlConstant.BASE_GENRES_ID,
                            buttonView.id.toString(),
                            buttonView.text.toString()
                        )
                        addFragment(fragment)
                    }
                }
                viewBinding.chipGroupGenres.addView(genresChip)
            }
        }
    }

    override fun getCategoriesSuccess(categories: List<Category>) {
        categoryAdapter.updateData(categories)
    }

    override fun onGetMoviesTopRatedSuccess(movies: List<Movie>) {
        topRatedAdapter.updateData(movies)
    }

    override fun onError(exception: Exception?) {
        exception?.let {
            Toast.makeText(activity, it.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onLoading(isLoad: Boolean) {
        view?.run {
            if (isLoad) {
                viewBinding.searchSwipeRefresh.isRefreshing = false
                viewBinding.frameProgressbarMovie.isVisible = false
            } else {
                viewBinding.frameProgressbarMovie.isVisible = true
            }
        }
    }

    private fun initToolBar() {
        view?.toolbar_base?.let {
            (activity as? MainActivity)?.run {
                setSupportActionBar(it)
                it.setNavigationOnClickListener {
                    activity?.run { supportFragmentManager.popBackStack() }
                }
            }
        }
    }

    override fun initAdapter() {
        view?.run {
            categoriesRecyclerView.setHasFixedSize(true)
            categoriesRecyclerView.adapter = categoryAdapter.apply {
                onItemClick = { category, _ ->
                    val type =
                        when (category.categoryName) {
                            Category.CategoryEntry.POPULAR -> UrlConstant.BASE_POPULAR
                            Category.CategoryEntry.NOW_PLAYING -> UrlConstant.BASE_NOW_PLAYING
                            Category.CategoryEntry.UPCOMING -> UrlConstant.BASE_UPCOMING
                            Category.CategoryEntry.TOP_RATE -> UrlConstant.BASE_TOP_RATE
                            else -> ""
                        }
                    val fragment = ListMovieFragment.getInstance(
                        type,
                        UrlConstant.BASE_VALUE,
                        category.categoryName
                    )
                    addFragment(fragment)
                }
            }
            viewBinding.recyclerTopRate.setHasFixedSize(true)
            viewBinding.recyclerTopRate.adapter = topRatedAdapter.apply {
                onItemClick = { movie, _ ->
                    addFragment(MovieDetailsFragment.getInstance(movie.id, movie.title))
                }
            }
        }
    }

    private fun initRefresh() {
        viewBinding.searchSwipeRefresh.setOnRefreshListener {
            activity?.let {
                if (NetworkUtil.isConnectedToNetwork(it)) {
                    presenter.onStart()
                } else {
                    onLoading(true)
                    val message = getString(R.string.check_internet_fail)
                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val DELAY_TIME = 500L
    }
}
