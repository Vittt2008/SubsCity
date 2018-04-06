package com.source.subscity.ui.cinema

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.source.subscity.R
import com.source.subscity.api.entities.cinema.Cinema
import com.source.subscity.api.entities.movie.Movie
import com.source.subscity.api.entities.screening.Screening
import com.source.subscity.dagger.SubsCityDagger
import com.source.subscity.providers.LanguageProvider
import com.source.subscity.providers.MetroProvider
import com.source.subscity.ui.movie.MovieAdapter
import com.source.subscity.ui.movie.MovieScreeningAdapter
import com.source.subscity.widgets.divider.ImageGridItemDecoration
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemaAdapter(private val cinema: Cinema,
                    private var movieScreenings: List<CinemaPresenter.MovieScreenings>,
                    private val mapClickListener: (Cinema) -> Unit,
                    private val phoneClickListener: (Cinema) -> Unit,
                    private val siteClickListener: (Cinema) -> Unit,
                    private val screeningClickListener: (Screening) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val CINEMA_INFO_VIEW_TYPE = 0
    private val CINEMA_MOVIES_TITLE_VIEW_TYPE = 1
    private val CINEMA_SCREENING_VIEW_TYPE = 2
    private val PROGRESS_VIEW_TYPE = 3

    private val SPAN_COUNT = 5

    private var isLoadingFinished = movieScreenings.isNotEmpty()

    @Inject
    lateinit var metroProvider: MetroProvider

    @Inject
    lateinit var languageProvider: LanguageProvider

    init {
        SubsCityDagger.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CINEMA_INFO_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cinema_info_2, parent, false)
                InfoViewHolder2(view)
            }
            CINEMA_MOVIES_TITLE_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_divider_title, parent, false)
                TitleDividerViewHolder(view)
            }
            PROGRESS_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
                ProgressViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_screenings, parent, false)
                MovieScreeningsViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        if (isLoadingFinished && movieScreenings.isEmpty()) {
            return 1
        }
        return 2 + movieScreenings.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            position == CINEMA_INFO_VIEW_TYPE -> (holder as InfoViewHolder2).bind(cinema)
            position != CINEMA_MOVIES_TITLE_VIEW_TYPE -> (holder as MovieScreeningsViewHolder).bind(movieScreenings[position - 2])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> CINEMA_INFO_VIEW_TYPE
            position == 1 && movieScreenings.isEmpty() -> PROGRESS_VIEW_TYPE
            position == 1 && movieScreenings.isNotEmpty() -> CINEMA_MOVIES_TITLE_VIEW_TYPE
            else -> CINEMA_SCREENING_VIEW_TYPE
        }
    }

    fun updateScreenings(movieScreenings: List<CinemaPresenter.MovieScreenings>) {
        val oldSize = itemCount
        if (movieScreenings.isNotEmpty()) {
            this.movieScreenings = movieScreenings
            notifyItemChanged(oldSize - 1)
            notifyItemRangeInserted(oldSize, itemCount - oldSize)
        } else {
            notifyItemRemoved(oldSize - 1)
        }
        isLoadingFinished = true
    }

    inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var cinema: Cinema

        private val address = view.findViewById<TextView>(R.id.tv_cinema_address)
        private val metroStation = view.findViewById<TextView>(R.id.tv_cinema_metro_station)
        private val mapButton = view.findViewById<ViewGroup>(R.id.cl_cinema_map)

        init {
            mapButton.setOnClickListener { mapClickListener.invoke(cinema) }
        }

        fun bind(cinema: Cinema) {
            this.cinema = cinema

            address.text = cinema.location.address
            metroStation.text = metroProvider.currentMetroTextProvider.formatMetroListStation(cinema.location.metro)
        }
    }

    inner class InfoViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var cinema: Cinema

        private val address = view.findViewById<TextView>(R.id.tv_cinema_address)
        private val metroStation = view.findViewById<TextView>(R.id.tv_cinema_metro_station)
        private val phoneNumber = view.findViewById<TextView>(R.id.tv_phone_number)
        private val webSite = view.findViewById<TextView>(R.id.tv_web_site)

        init {
            view.findViewById<View>(R.id.cl_show_on_map).setOnClickListener { mapClickListener.invoke(cinema) }
            view.findViewById<View>(R.id.cl_call).setOnClickListener { phoneClickListener.invoke(cinema) }
            view.findViewById<View>(R.id.cl_web).setOnClickListener { siteClickListener.invoke(cinema) }
        }

        fun bind(cinema: Cinema) {
            this.cinema = cinema

            address.text = cinema.location.address
            metroStation.text = metroProvider.currentMetroTextProvider.formatMetroListStation(cinema.location.metro)
            phoneNumber.text = cinema.phones.joinToString("      ")
            webSite.text = cinema.urls.firstOrNull()
        }
    }

    inner class TitleDividerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.findViewById<TextView>(R.id.tv_divider_title)
                    .setText(R.string.movie_screenings_title)
        }
    }

    inner class MovieScreeningsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val movieTitle = view.findViewById<TextView>(R.id.tv_title)
        private val movieLanguage = view.findViewById<TextView>(R.id.tv_info)
        private val screenings = view.findViewById<RecyclerView>(R.id.rv_list)

        fun bind(movieScreenings: CinemaPresenter.MovieScreenings) {
            movieTitle.text = movieScreenings.movie.title.russian
            movieLanguage.text = movieLanguage(movieScreenings.movie)
            screenings.apply {
                layoutManager = GridLayoutManager(screenings.context, SPAN_COUNT, LinearLayoutManager.VERTICAL, false)
                adapter = MovieScreeningAdapter(screenings.context!!, movieScreenings.screenings, screeningClickListener)
                val margin = context.resources.getDimensionPixelSize(R.dimen.screening_margin)
                addItemDecoration(ImageGridItemDecoration(SPAN_COUNT, margin, false))
            }
        }
    }

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private fun movieLanguage(movie: Movie): String {
        val movieLanguage = languageProvider.languageFormat(movie)
        val age = movie.ageRestriction.toString() + "+"
        if (movieLanguage.isNullOrEmpty()) {
            return age
        }
        return "$movieLanguage, $age"
    }
}