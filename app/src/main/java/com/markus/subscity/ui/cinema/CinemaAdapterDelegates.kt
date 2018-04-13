package com.markus.subscity.ui.cinema

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.markus.subscity.R
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.api.entities.movie.Movie
import com.markus.subscity.api.entities.screening.Screening
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.providers.LanguageProvider
import com.markus.subscity.providers.MetroProvider
import com.markus.subscity.ui.movie.MovieScreeningAdapter
import com.markus.subscity.widgets.divider.ImageGridItemDecoration
import javax.inject.Inject

/**
 * @author Vitaliy Markus
 */
class CinemaAdapterDelegates(cinema: Cinema,
                             movieScreenings: List<CinemaPresenter.MovieScreenings>,
                             private val mapClickListener: (Cinema) -> Unit,
                             private val phoneClickListener: (Cinema) -> Unit,
                             private val siteClickListener: (Cinema) -> Unit,
                             private val screeningClickListener: (Screening) -> Unit) : ListDelegationAdapter<List<Any>>() {

    private val data: MutableList<Any> = ArrayList()

    @Inject
    lateinit var metroProvider: MetroProvider

    @Inject
    lateinit var languageProvider: LanguageProvider

    init {
        SubsCityDagger.component.inject(this)

        delegatesManager.addDelegate(InfoDelegate())
                .addDelegate(TitleDelegate())
                .addDelegate(ProgressDelegate())
                .addDelegate(MovieScreeningsDelegate())

        data.add(cinema)
        if (movieScreenings.isNotEmpty()) {
            data.add("Title")
            data.addAll(movieScreenings)
        } else {
            data.add(Any())
        }
        setItems(data)
    }

    fun updateScreenings(movieScreenings: List<CinemaPresenter.MovieScreenings>) {
        val oldSize = itemCount
        if (movieScreenings.isNotEmpty()) {
            data[oldSize - 1] = "Title"
            notifyItemChanged(oldSize - 1)
            data.addAll(movieScreenings)
            notifyItemRangeInserted(oldSize, itemCount - oldSize)
        } else {
            data.removeAt(oldSize - 1)
            notifyItemRemoved(oldSize - 1)
        }
    }


    //region ViewHolders

    inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var cinema: Cinema

        private val address = view.findViewById<TextView>(R.id.tv_cinema_address)
        private val metroStation = view.findViewById<TextView>(R.id.tv_cinema_metro_station)
        private val phoneNumber = view.findViewById<TextView>(R.id.tv_phone_number)
        private val webSite = view.findViewById<TextView>(R.id.tv_web_site)
        private val phoneNumberLayout = view.findViewById<View>(R.id.cl_call)
        private val webSiteLayout = view.findViewById<View>(R.id.cl_web)


        init {
            view.findViewById<View>(R.id.cl_show_on_map).setOnClickListener { mapClickListener.invoke(cinema) }
            phoneNumberLayout.setOnClickListener { phoneClickListener.invoke(cinema) }
            webSiteLayout.setOnClickListener { siteClickListener.invoke(cinema) }
        }

        fun bind(cinema: Cinema) {
            this.cinema = cinema

            address.text = cinema.location.address
            metroStation.text = metroProvider.currentMetroTextProvider.formatMetroListStation(cinema.location.metro)

            val phone = cinema.phones.firstOrNull()
            phoneNumber.visibility = if (phone != null) View.VISIBLE else View.GONE
            phoneNumber.text = phone

            val url = cinema.urls.firstOrNull()
            webSite.visibility = if (phone != null) View.VISIBLE else View.GONE
            webSite.text = url
        }
    }

    inner class TitleDividerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.findViewById<TextView>(R.id.tv_divider_title)
                    .setText(R.string.movie_screenings_title)
        }
    }

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class MovieScreeningsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val SPAN_COUNT = 5
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

        private fun movieLanguage(movie: Movie): String {
            val movieLanguage = languageProvider.languageFormat(movie)
            val age = movie.ageRestriction.toString() + "+"
            if (movieLanguage.isNullOrEmpty()) {
                return age
            }
            return "$movieLanguage, $age"
        }
    }

    //endregion

    //region Delegates

    inner class InfoDelegate : AbsListItemAdapterDelegate<Cinema, Any, InfoViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup): InfoViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cinema_info_2, parent, false)
            return InfoViewHolder(view)
        }

        override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
            return position == 0
        }

        override fun onBindViewHolder(item: Cinema, viewHolder: InfoViewHolder, payloads: List<Any>) {
            viewHolder.bind(item)
        }

    }

    inner class TitleDelegate : AbsListItemAdapterDelegate<String, Any, TitleDividerViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup): TitleDividerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_divider_title, parent, false)
            return TitleDividerViewHolder(view)
        }

        override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
            return position == 1 && item is String
        }

        override fun onBindViewHolder(item: String, viewHolder: TitleDividerViewHolder, payloads: MutableList<Any>) {}

    }

    class ProgressDelegate : AbsListItemAdapterDelegate<Any, Any, ProgressViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup): ProgressViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
            return ProgressViewHolder(view)
        }

        override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
            return position == 1 && item !is String
        }

        override fun onBindViewHolder(item: Any, viewHolder: ProgressViewHolder, payloads: List<Any>) {}

    }

    inner class MovieScreeningsDelegate : AbsListItemAdapterDelegate<CinemaPresenter.MovieScreenings, Any, MovieScreeningsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup): MovieScreeningsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_screenings, parent, false)
            return MovieScreeningsViewHolder(view)
        }

        override fun isForViewType(item: Any, items: List<Any>, position: Int): Boolean {
            return item is CinemaPresenter.MovieScreenings
        }

        override fun onBindViewHolder(item: CinemaPresenter.MovieScreenings, viewHolder: MovieScreeningsViewHolder, payloads: List<Any>) {
            viewHolder.bind(item)
        }

    }

    //endregion
}