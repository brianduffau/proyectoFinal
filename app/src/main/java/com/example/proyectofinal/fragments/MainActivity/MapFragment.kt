package com.example.proyectofinal.fragments.MainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.proyectofinal.R
import com.example.proyectofinal.entities.Professional
import com.example.proyectofinal.entities.Review
import com.example.proyectofinal.viewmodels.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener {

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    lateinit var v : View

    private lateinit var mMap : GoogleMap
    private lateinit var viewModel : MapViewModel
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    private lateinit var paseador : Chip
    private lateinit var cuidador : Chip
    private lateinit var veterinaria : Chip
    private lateinit var petShop : Chip
    private lateinit var verTodos : Chip

    private var markerMap = HashMap<String, String>()

    var db = Firebase.firestore

    private var profList : ArrayList<Professional> = arrayListOf()

    private lateinit var searchInput : AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        paseador = v.findViewById(R.id.paseador)
        cuidador = v.findViewById(R.id.cuidador)
        veterinaria = v.findViewById(R.id.veterinaria)
        petShop = v.findViewById(R.id.petShop)
        verTodos = v.findViewById(R.id.verTodos)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        //muestra todos los profesionales de entrada
        //getAllMarkers()

        return v

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        mMap.setOnInfoWindowClickListener(this)

        setUpMap()
        searchBar()
        chipFilters()

    }

    private fun generateMarkers(document : QueryDocumentSnapshot, mMap : GoogleMap, markerMap : HashMap<String, String>){

        // GENERA LOS MARKERS EN LA FUNCION DE GETMARKERSBYTYPE

        lateinit var geoPoint : GeoPoint

        if(document != null) {

            var geoPointFB = document.getGeoPoint("geo")
            if (geoPointFB != null) {
                geoPoint = geoPointFB
            }

            var latitude = geoPoint.latitude
            var longitude = geoPoint.longitude
            val myPos = LatLng(latitude, longitude)

            val marker = mMap.addMarker(
                MarkerOptions().position(myPos)
                    .title(document.data["name"] as String?)
                    .snippet(document.data["professionalType"] as String?)
            )

            val idOne = marker?.id
            if (idOne != null) {
                markerMap.put(idOne, document.id)
            }
        }
    }

    private fun getAllMarkers(){

        // TRAE TODOS LOS PROFESIONALES DE LA DB

        val docRef = db.collection("professionals")

        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    generateMarkers(document, mMap, markerMap)
                }

                mMap.setOnInfoWindowClickListener { marker ->
                    val actionId = markerMap[marker.id]
                    if (actionId != null) {
                        val action = MapFragmentDirections.actionMapToProfessional(
                            actionId
                        )
                        v.findNavController().navigate(action)
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.d("markersNotOK", "Error getting documents: ", exception)
            }
    }

    private fun getMarkersByType(type : String){

        // LLAMADAS A DB SEGUN TIPO DE PROFESIONAL
        // LUEGO MUESTRA MARKERS SEGUN ESE TIPO

        db.collection("professionals")
            .whereEqualTo("professionalType", type)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    generateMarkers(document, mMap, markerMap)
                }

                mMap.setOnInfoWindowClickListener { marker ->
                    val actionId = markerMap[marker.id]
                    if (actionId != null) {
                        val action = MapFragmentDirections.actionMapToProfessional(actionId)
                        v.findNavController().navigate(action)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("markersNotOK", "Error getting documents: ", exception)
            }
    }

    private fun chipFilters(){
        paseador.setOnClickListener {
            val paseadorString = capitalizeString(paseador.text.toString())
            mMap.clear()
            myMarker()
            getMarkersByType(paseadorString)
        }

        cuidador.setOnClickListener {
            mMap.clear()
            myMarker()
            val cuidadorString = capitalizeString(cuidador.text.toString())
            getMarkersByType(cuidadorString)
        }

        veterinaria.setOnClickListener {
            mMap.clear()
            myMarker()
            val vetString = capitalizeString(veterinaria.text.toString())
            getMarkersByType(vetString)
        }

        verTodos.setOnClickListener {
            getAllMarkers()
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap(){

        val caba = LatLng(-34.594776, -58.446751)
        myMarker()

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(caba, 12f))

        if(ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if(location != null){
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    private fun myMarker() {
        val caba = LatLng(-34.594776, -58.446751)
        mMap.addMarker(
            MarkerOptions()
                .position(caba)
                .title("Mi Ubicación"))
            ?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
    }

    private fun placeMarkerOnMap(currentLatLng: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.title("$currentLatLng")
        mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker) = false

    override fun onInfoWindowClick( marker : Marker) {
    }

    private fun searchBar(){

        searchInput = v.findViewById(R.id.searchInput)

        db.collection("professionals")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val listEntry = document.toObject<Professional>()
                    profList.add(listEntry)
                    Log.d("autocompletarOK", "${document.id} => ${document.data}")


                }
            }
            .addOnFailureListener { exception ->
                Log.w("autocompletarNotOK", "Error getting documents: ", exception)
            }

        val adapter = activity?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, profList) }
        searchInput.setAdapter(adapter)

        searchInput.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            mMap.clear()
            markerMap.clear()
            myMarker()

            if (adapter != null) {
                val professional = adapter.getItem(id.toInt())
                if (professional != null) {

                    lateinit var geoPoint : GeoPoint ;

                    var geoPointFB = professional.geo
                    if (geoPointFB != null) {
                        geoPoint = geoPointFB
                    }

                    var latitude = geoPoint.latitude
                    var longitude = geoPoint.longitude

                    val myPos = LatLng(latitude, longitude)

                    val marker = mMap.addMarker(
                        MarkerOptions().position(myPos)
                            .title(professional.name)
                            .snippet(professional.professionalType)
                    )

                    val idOne = marker?.id
                    if (idOne != null) {
                        markerMap[idOne] = professional.id

                        mMap.setOnInfoWindowClickListener {
                            Log.i("markerData", markerMap.entries.toString() + " " + markerMap[marker.id])
                            /*val actionId = markerMap[marker.id]
                            if (actionId != null) {
                                val action =
                                    MapFragmentDirections.actionMapToProfessional(
                                        actionId
                                    )
                                v.findNavController().navigate(action)
                            }*/
                        }
                    }
                }
            }
        }
    }

    private fun capitalizeString(str: String): String {
        var retStr = str
        try {
            retStr = str.substring(0, 1).uppercase() + str.substring(1)
        } catch (e: Exception) {
        }
        return retStr
    }

}

