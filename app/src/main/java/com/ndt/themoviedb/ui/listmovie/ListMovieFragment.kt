package com.ndt.themoviedb.ui.listmovie

import android.annotation.SuppressLint
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.data.model.MovieResultPage
import com.ndt.themoviedb.data.repository.MovieRepository
import com.ndt.themoviedb.data.source.local.MovieLocalDataSource
import com.ndt.themoviedb.data.source.remote.MovieRemoteDataSource
import com.ndt.themoviedb.databinding.FragmentListMovieBinding
import com.ndt.themoviedb.databinding.FragmentSearchBinding
import com.ndt.themoviedb.databinding.ItemListMovieBinding
import com.ndt.themoviedb.ui.base.BaseFragment
import com.ndt.themoviedb.ui.listmovie.adapter.MovieListAdapter
import com.ndt.themoviedb.ui.mainscreen.MainActivity
import com.ndt.themoviedb.ui.utils.NetworkUtil
import com.ndt.themoviedb.ui.utils.constant.UrlConstant
import com.ndt.themoviedb.ui.utils.extension.addFragment
import kotlinx.android.synthetic.main.toolbar_base.*
import kotlinx.android.synthetic.main.toolbar_base.view.*

class ListMovieFragment : BaseFragment<FragmentListMovieBinding>(FragmentListMovieBinding::inflate),
    ListMovieContract.View {
    private lateinit var presenter: ListMovieContract.Presenter
    private val movieListAdapter: MovieListAdapter by lazy { MovieListAdapter() }
    private var totalPage = 0
    private var currentPage = UrlConstant.BASE_PAGE_DEFAULT
    private var isScrolling = false
    private var type: String? = ""
    private var value: String? = ""


    override fun initData() {
        val movieRepository: MovieRepository =
            MovieRepository.getInstance(
                MovieRemoteDataSource.getInstance(),
                MovieLocalDataSource.getInstance()
            )
        presenter = ListMoviePresenter(movieRepository)
        presenter.setView(this)
        arguments?.run {
            type = getString(UrlConstant.BASE_TYPE)
            value = getString(UrlConstant.BASE_VALUE)
            activity?.let {
                if (NetworkUtil.isConnectedToNetwork(it)) {
                    presenter.getMovies(type.toString(), value.toString())
                } else {
                    val message = getString(R.string.check_internet_fail)
                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        initToolBar()
    }

    private fun initToolBar() {
        viewBinding.progressbarLoading.toolbar_base.let {
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
    }

    override fun onGetMoviesSuccess(movies: List<Movie>) {
        movieListAdapter.insertData(movies)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onGetMovieResultPage(movieResultPage: MovieResultPage) {
        totalPage = movieResultPage.totalPage
        view?.run {
            viewBinding.textResult.text =
                getString(R.string.results_found, movieResultPage.totalResult)

        }
    }

    override fun onError(exception: Exception?) {
        exception?.let {
            Toast.makeText(activity, it.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onLoading(isLoad: Boolean) {
        view?.run {
            if (isLoad) {
                viewBinding.frameProgressbarMovie.isVisible = false
                viewBinding.progressbarLoading.isVisible = false
            } else {
                viewBinding.frameProgressbarMovie.isVisible = true
            }
        }
    }

    override fun initAdapter() {
        viewBinding.recyclerListMovie.apply {
            setHasFixedSize(true)
            adapter = movieListAdapter.apply {
                onItemClick = { item, _ ->
                    addFragment(item)
                }
            }
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                        isScrolling = true
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val linearLayoutManager: LinearLayoutManager =
                        layoutManager as LinearLayoutManager
                    if (isScrolling && linearLayoutManager.findLastVisibleItemPosition() ==
                        movieListAdapter.itemCount - 1
                    ) {
                        isScrolling = false
                        if (currentPage + 1 <= totalPage) {
                            activity?.let {
                                if (NetworkUtil.isConnectedToNetwork(it)) {
                                    if (type != null && value != null) {
                                        viewBinding.progressbarLoading.visibility = View.VISIBLE
                                        currentPage += 1
                                        presenter.getMovies(type!!, value!!, currentPage)
                                    }
                                } else {
                                    val message = getString(R.string.check_internet_fail)
                                    Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    companion object {
        fun getInstance(type: String, query: String, title: String) =
            ListMovieFragment().apply {
                arguments = bundleOf(
                    UrlConstant.BASE_TYPE to type,
                    UrlConstant.BASE_VALUE to query,
                    UrlConstant.BASE_TITLE to title
                )
            }
    }
}
