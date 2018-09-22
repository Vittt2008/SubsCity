package com.markus.subscity.ui.cinemasmap

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.content.res.AppCompatResources
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.markus.subscity.R
import com.markus.subscity.widgets.ViewPagerBottomSheetBehavior
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.analytics
import com.markus.subscity.extensions.toast
import com.markus.subscity.ui.cinema.CinemaActivity
import com.markus.subscity.utils.MapSlidr


/**
 * @author Vitaliy Markus
 */
class CinemasMapActivity : MvpAppCompatActivity(), CinemasMapView, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    @InjectPresenter
    lateinit var cinemasMapPresenter: CinemasMapPresenter

    private lateinit var viewPager: ViewPager
    private lateinit var mapFragment: SupportMapFragment

    private lateinit var map: GoogleMap
    private lateinit var markers: List<Marker>
    private lateinit var cinemas: List<Cinema>
    private lateinit var markerIdCinemaIdMap: Map<String, Long>
    private lateinit var cinemaIdMarkerIdMap: Map<Long, String>

    private lateinit var behavior: ViewPagerBottomSheetBehavior<ViewPager>

    private val inactiveIcon by lazy { getMarkerIcon(R.drawable.ic_pin_unselected) }
    private val activeIcon by lazy { getMarkerIcon(R.drawable.ic_pin) }

    companion object {

        private const val FRAGMENT_MAP_TAG = "fragment_map_tag"

        private const val EXTRA_LATITUDE = "latitude"
        private const val EXTRA_LONGITUDE = "longitude"
        private const val EXTRA_ZOOM = "zoom"

        fun start(context: Context, latitude: Double, longitude: Double, zoom: Int) {
            val intent = Intent(context, CinemasMapActivity::class.java)
                    .putExtra(EXTRA_LATITUDE, latitude)
                    .putExtra(EXTRA_LONGITUDE, longitude)
                    .putExtra(EXTRA_ZOOM, zoom)
            context.startActivity(intent)
        }
    }

    @ProvidePresenter
    fun cinemasPresenter(): CinemasMapPresenter {
        return SubsCityDagger.component.createCinemasMapPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_cinemas_map)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val content = findViewById<View>(android.R.id.content)
        content.setBackgroundColor(ContextCompat.getColor(this, R.color.map_background_color))
        viewPager = findViewById(R.id.cinema_view_pager)
        behavior = (viewPager.layoutParams as CoordinatorLayout.LayoutParams).behavior as ViewPagerBottomSheetBehavior<ViewPager>
        behavior.state = ViewPagerBottomSheetBehavior.STATE_HIDDEN
        behavior.setBottomSheetCallback(MapBottomSheetCallback())

        if (savedInstanceState == null) {
            mapFragment = SupportMapFragment.newInstance(GoogleMapOptions().camera(createCameraPosition()))
            supportFragmentManager.beginTransaction()
                    .add(R.id.map_root, mapFragment, FRAGMENT_MAP_TAG)
                    .commit()
        } else {
            mapFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG) as SupportMapFragment
        }

        mapFragment.getMapAsync {
            it.setOnInfoWindowClickListener(this)
            it.setOnMarkerClickListener(this)
            cinemasMapPresenter.getCinemas(it)
        }

        MapSlidr.attach(this)

    }

    override fun onResume() {
        super.onResume()
        analytics().logActivity(this)
    }

    override fun showCinemas(cinemas: List<Cinema>, googleMap: Any) {
        map = googleMap as GoogleMap
        this.cinemas = cinemas
        val markersMap = cinemas.associateBy({ map.addMarker(createMarkerOptions(it)) }, { it.id })
        val markerCinemaMap = markersMap.mapKeys { it.key.id }
        val markers = markersMap.map { it.key }
        viewPager.adapter = CinemaFragmentAdapter(supportFragmentManager, cinemas)
        viewPager.addOnPageChangeListener(CinemaMapOnPageChangeListener())
        cinemasMapPresenter.onMarkersAdd(markerCinemaMap, markers)
    }

    override fun onMarkersAdd(markerCinemaMap: Map<String, Long>, markers: List<Any>) {
        this.markerIdCinemaIdMap = markerCinemaMap
        this.cinemaIdMarkerIdMap = markerCinemaMap.entries.associateBy({ it.value }, { it.key })
        this.markers = markers as List<Marker>

        val markerId = cinemaIdMarkerIdMap[cinemas.first().id]!!
        selectMarker(markerId)
    }

    override fun onError(throwable: Throwable) {
        toast(throwable.message)
    }

    override fun onInfoWindowClick(marker: Marker) {
        val cinemaId = markerIdCinemaIdMap[marker.id]!!
        analytics().logOpenCinemaFromMap(cinemaId)
        CinemaActivity.start(this, cinemaId)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val cinemaId = markerIdCinemaIdMap[marker.id]!!
        val oldIndex = viewPager.currentItem
        val smoothScroll = behavior.state != BottomSheetBehavior.STATE_HIDDEN
        viewPager.setCurrentItem(cinemas.indexOfFirst { it.id == cinemaId }, smoothScroll)
        if (oldIndex == viewPager.currentItem) {
            selectMarker(oldIndex)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (behavior.state == ViewPagerBottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = ViewPagerBottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    private fun createCameraPosition(): CameraPosition {
        val latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0)
        val zoom = intent.getIntExtra(EXTRA_ZOOM, 0)
        return CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), zoom.toFloat())
    }

    private fun selectMarker(position: Int) {
        val cinema = cinemas[position]
        val markerId = cinemaIdMarkerIdMap[cinema.id]!!
        selectMarker(markerId)
    }

    private fun selectMarker(id: String) {
        for (marker in markers) {
            if (marker.id == id) {
                marker.setIcon(activeIcon)
                animateCamera(marker, true)
                if (behavior.state == ViewPagerBottomSheetBehavior.STATE_HIDDEN) {
                    behavior.state = ViewPagerBottomSheetBehavior.STATE_COLLAPSED
                }
            } else {
                marker.setIcon(inactiveIcon)
            }
        }
    }

    private fun animateCamera(marker: Marker, withOffset: Boolean) {
        if (withOffset) {
            val projection = map.projection
            val point = projection.toScreenLocation(marker.position)
            point.y = point.y + resources.getDimensionPixelSize(R.dimen.map_bottom_sheet_height) / 2
            val latLng = projection.fromScreenLocation(point)
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
        }
    }

    private fun clearSelectedMarker(position: Int) {
        val marker = markers[position]
        markers.forEach { it.setIcon(inactiveIcon) }
        animateCamera(marker, false)
    }

    private fun createMarkerOptions(it: Cinema): MarkerOptions? {
        val markerOptions = MarkerOptions()
                .position(LatLng(it.location.latitude, it.location.longitude))
                .icon(inactiveIcon)
                .anchor(0.5f, 1f)
                .title(it.name)
                .snippet(it.location.address)
        return markerOptions
    }

    private fun getMarkerIcon(@DrawableRes iconId: Int): BitmapDescriptor {
        val icon = AppCompatResources.getDrawable(this, iconId)!!.toBitmap()
        val markerIcon = BitmapDescriptorFactory.fromBitmap(icon)
        return markerIcon
    }

    private fun Drawable.toBitmap(): Bitmap {
        if (this is BitmapDrawable && this.bitmap != null) {
            return this.bitmap
        }

        val bitmap = if (this.intrinsicWidth <= 0 || this.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)
        return bitmap
    }

    inner class CinemaMapOnPageChangeListener : ViewPager.SimpleOnPageChangeListener() {

        override fun onPageSelected(position: Int) {
            selectMarker(position)
            viewPager.post { behavior.invalidateScrollingChild() }
        }
    }

    inner class MapBottomSheetCallback : ViewPagerBottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                clearSelectedMarker(viewPager.currentItem)
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }
}