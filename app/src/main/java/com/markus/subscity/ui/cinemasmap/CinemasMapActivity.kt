package com.markus.subscity.ui.cinemasmap

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.content.res.AppCompatResources
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.markus.subscity.R
import com.markus.subscity.api.entities.cinema.Cinema
import com.markus.subscity.dagger.SubsCityDagger
import com.markus.subscity.extensions.toast
import com.markus.subscity.ui.cinema.CinemaActivity


/**
 * @author Vitaliy Markus
 */
class CinemasMapActivity : MvpAppCompatActivity(), CinemasMapView, GoogleMap.OnInfoWindowClickListener {

    private val FRAGMENT_MAP_TAG = "fragment_map_tag"

    @InjectPresenter
    lateinit var cinemasMapPresenter: CinemasMapPresenter

    private lateinit var mapFragment: SupportMapFragment

    private lateinit var markerCinemaMap: Map<String, Long>

    companion object {

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

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        if (savedInstanceState == null) {
            mapFragment = SupportMapFragment.newInstance(GoogleMapOptions().camera(createCameraPosition()))
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, mapFragment, FRAGMENT_MAP_TAG)
                    .commit()
        } else {
            mapFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_MAP_TAG) as SupportMapFragment
        }

        mapFragment.getMapAsync {
            it.setOnInfoWindowClickListener(this)
            cinemasMapPresenter.getCinemas(it)
        }

    }

    override fun onResume() {
        super.onResume()
        SubsCityDagger.component.provideAnalytics().logActivity(this)
    }

    override fun showCinemas(cinemas: List<Cinema>, googleMap: Any) {
        val map = googleMap as GoogleMap
        val icon = AppCompatResources.getDrawable(this, R.drawable.ic_pin)!!.toBitmap()
        val markerCinemaMap: Map<String, Long> = cinemas.associateBy({ map.addMarker(createMarkerOptions(it, icon)).id }, { it.id })
        cinemasMapPresenter.onMarkersAdd(markerCinemaMap)
    }

    override fun onMarkersAdd(markerCinemaMap: Map<String, Long>) {
        this.markerCinemaMap = markerCinemaMap
    }

    override fun onError(throwable: Throwable) {
        toast(throwable.message)
    }

    override fun onInfoWindowClick(marker: Marker) {
        val cinemaId = markerCinemaMap[marker.id]!!
        CinemaActivity.start(this, cinemaId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createCameraPosition(): CameraPosition {
        val latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0)
        val zoom = intent.getIntExtra(EXTRA_ZOOM, 0)
        return CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), zoom.toFloat())
    }

    private fun createMarkerOptions(it: Cinema, icon: Bitmap): MarkerOptions? {
        val markerOptions = MarkerOptions()
                .position(LatLng(it.location.latitude, it.location.longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .anchor(0.5f, 1f)
                .title(it.name)
                .snippet(it.location.address)
        return markerOptions
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
}