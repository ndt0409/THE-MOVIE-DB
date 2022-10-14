package com.ndt.themoviedb.data.model

import android.annotation.SuppressLint
import android.database.Cursor
import com.ndt.themoviedb.ui.base.BaseFilterAdapter

data class Favorite(
    val id: String,
    val title: String,
    val overview: String,
    val posterPath: ByteArray?,
    val voteAverage: String,
    val releaseDate: String
) : BaseFilterAdapter.Searchable {
    override fun getSearchCriteria(): String {
        return title
    }

    @SuppressLint("Range")
    constructor(cursor: Cursor) : this(
        id = cursor.getString(cursor.getColumnIndex(MovieEntry.ID)),
        title = cursor.getString(cursor.getColumnIndex(MovieEntry.TITLE)),
        overview = cursor.getString(cursor.getColumnIndex(MovieEntry.OVERVIEW)),
        posterPath = cursor.getBlob(cursor.getColumnIndex(MovieEntry.POSTER_PATH)),
        voteAverage = cursor.getString(cursor.getColumnIndex(MovieEntry.VOTE_AVERAGE)),
        releaseDate = cursor.getString(cursor.getColumnIndex(MovieEntry.RELEASE_DATE))
    )

    constructor(movie: Movie, byteArray: ByteArray?) : this(
        id = movie.id.toString(),
        title = movie.title,
        overview = movie.overView,
        posterPath = byteArray,
        voteAverage = movie.voteAverage.toString(),
        releaseDate = movie.releaseDate
    )

    object MovieEntry {
        const val FAVORITE = "tbl_favorite_movie"
        const val ID = "movie_id"
        const val TITLE = "movie_title"
        const val OVERVIEW = "movie_overview"
        const val POSTER_PATH = "movie_poster_path"
        const val VOTE_AVERAGE = "movie_vote_average"
        const val RELEASE_DATE = "movie_release_date"
        const val SELECTION_LIKE_ID = "movie_id = ?"
        const val SQL_FAVORITE =
            "CREATE TABLE $FAVORITE (" +
                    "$ID text primary key, " +
                    "$TITLE text, " +
                    "$OVERVIEW text, " +
                    "$POSTER_PATH BLOB, " +
                    "$VOTE_AVERAGE text, " +
                    "$RELEASE_DATE text);"
    }
}
