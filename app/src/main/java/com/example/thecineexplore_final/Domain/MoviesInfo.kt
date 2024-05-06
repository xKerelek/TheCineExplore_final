import com.example.thecineexplore_final.Domain.MovieResult

import com.google.gson.annotations.SerializedName

class MoviesInfo {
    @SerializedName("page")
    var page: Int? = null

    @SerializedName("results")
    var results: List<MovieResult>? = null

    @SerializedName("total_pages")
    var totalPages: Int? = null

    @SerializedName("total_results")
    var totalResults: Int? = null
}