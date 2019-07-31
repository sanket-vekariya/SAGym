package com.sa.gym.view


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sa.gym.R


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    var builder = LatLngBounds.Builder()
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        FirebaseFirestore.getInstance().collection("location")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val latlang = LatLng(document.get("lati") as Double, document.get("longi") as Double)
                        builder.include(latlang)
                        googleMap.addMarker(MarkerOptions().position(latlang).title(document.get("info") as String))
                    }
                    val bounds = builder.build()

                    try {
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 200)
                        googleMap.moveCamera(cameraUpdate)
                        googleMap.animateCamera(cameraUpdate)
                    } catch (e: IllegalStateException) {
                        // layout not yet initialized
                        val mapView = fragmentManager!!.findFragmentById(R.id.fragment_map)!!.view
                        if (mapView!!.viewTreeObserver.isAlive) {
                            mapView.viewTreeObserver.addOnGlobalLayoutListener(object :
                                ViewTreeObserver.OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                        mapView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                                    } else {
                                        mapView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                    }
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
                                }
                            })
                        }
                    }

                } else {
                    Toast.makeText(context, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
                    return@OnCompleteListener
                }
            })
//
//        googleMap.setOnInfoWindowClickListener { marker ->
//            Toast.makeText(context, marker.title, Toast.LENGTH_SHORT).show()
//        }
    }
}
