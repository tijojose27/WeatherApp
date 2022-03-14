package com.example.weatherapp.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.helper.WeatherHelpers
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.locBtn.setOnClickListener {
            getLatLon()
        }

        binding.cityBtn.setOnClickListener {
            WeatherHelpers.updateCity(binding.cityTextInputEditText.text.toString())
            goToDetailPage()
        }

        binding.zipBtn.setOnClickListener {
            val zip = binding.zipTextInputEditText.text.toString()
            WeatherHelpers.updateZip(zip)
            goToDetailPage()
        }
    }

    //NAVIGATE TO WEATHER DETAIL FRAGMENT
    fun goToDetailPage() {
        findNavController().navigate(R.id.action_homeFragment_to_weatherDetailFragment)
    }

    //CLEAR DATA STORAGE
    override fun onResume() {
        super.onResume()
        WeatherHelpers.clearData()
    }

    private fun getLatLon() {
        //CHECKING LOCATON PERMISSION
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        } else {
            //IF PERMISSION GRANTED GETTING LAST LOCATION
            fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                val location: Location? = task.result
                if (location == null) {
                    Toast.makeText(requireContext(), "Cannot get Location", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    WeatherHelpers.updateLatLong(location.latitude, location.longitude)
                    goToDetailPage()
                }
            }
        }
    }


    //LOCATION PERMISSION REQUIREMENTS
    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This is needed for getting weather Info",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
    }

}
