package lib.shishkin.microservices.provider

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.common.Connectivity
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.sl.AbsSmallUnion
import lib.shishkin.sl.IProvider
import lib.shishkin.sl.action.ShowMessageAction
import lib.shishkin.sl.provider.ApplicationProvider
import lib.shishkin.sl.ui.IActivity
import java.util.*
import java.util.concurrent.TimeUnit

class LocationUnion : AbsSmallUnion<ILocationSubscriber>(),
    ILocationUnion {

    companion object {
        const val NAME = "LocationUnion"

        private val POLLING_FREQ = TimeUnit.SECONDS.toMillis(30)
        private val FASTEST_UPDATE_FREQ = TimeUnit.SECONDS.toMillis(5)
        private val SMALLEST_DISPLACEMENT = 20f
    }

    private var locationProviderClient: FusedLocationProviderClient? = null
    private var location: Location? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var geocoder: Geocoder? = null
    private var isGetLocation = false
    private var isRuning = false

    override fun getName(): String {
        return NAME
    }

    override fun compareTo(other: IProvider): Int {
        return if (other is ILocationUnion) 0 else 1
    }

    override fun onRegister() {
        locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                if (!locationAvailability!!.isLocationAvailable) {
                    val context = ApplicationProvider.appContext
                    ApplicationSingleton.instance.activityProvider.getActivity<IActivity>()?.addAction(
                        ShowMessageAction(
                            context.getString(R.string.location_error),
                            ApplicationUtils.MESSAGE_TYPE_WARNING
                        )
                    )
                }
            }

            override fun onLocationResult(locationResult: LocationResult?) {
                setLocation(locationResult?.lastLocation)
            }
        }

        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval =
            POLLING_FREQ
        locationRequest.fastestInterval =
            FASTEST_UPDATE_FREQ
        locationRequest.smallestDisplacement =
            SMALLEST_DISPLACEMENT
    }

    override fun onUnRegisterLastSubscriber() {
        stopLocation()
    }

    @SuppressLint("MissingPermission")
    override fun startLocation() {
        if (!isValid()) return

        if (!hasSubscribers()) {
            return
        }

        if (isRuning) {
            stopLocation()
        }

        isRuning = true
        ApplicationUtils.runOnUiThread(Runnable {
            isGetLocation = false
            val context = ApplicationProvider.appContext
            locationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            locationProviderClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
                ?.addOnFailureListener { e ->
                    ApplicationSingleton.instance.onError(
                        NAME,
                        e
                    )
                }

            if (geocoder == null) {
                geocoder = Geocoder(context, Locale.getDefault())
            }
        })

        if (location != null) {
            setLocation(location)
        }
    }

    override fun stopLocation() {
        isRuning = false
        locationProviderClient?.removeLocationUpdates(locationCallback)
        locationProviderClient = null
    }

    override fun getLocation(): Location? {
        return location
    }

    override fun onAddSubscriber(subscriber: ILocationSubscriber) {
        if (subscriber.isValid() && location != null) {
            subscriber.setLocation(location!!)
        }
    }

    override fun getAddress(location: Location, countAddress: Int): List<Address> {
        var cnt = countAddress
        if (cnt < 1) {
            cnt = 1
        }

        val list = ArrayList<Address>()
        if (Connectivity.isNetworkConnectedOrConnecting(ApplicationProvider.appContext) && geocoder != null && Geocoder.isPresent()) {
            try {
                val adr = geocoder?.getFromLocation(
                    location.latitude,
                    location.longitude,
                    cnt
                )
                if (adr != null) {
                    list.addAll(adr)
                }
            } catch (e: Exception) {
                ApplicationSingleton.instance.onError(
                    NAME,
                    ApplicationProvider.appContext.getString(R.string.restart_location),
                    true
                )
            }
        }
        return list
    }

    override fun getAddress(location: Location): List<Address> {
        return getAddress(location, 1)
    }

    override fun isGetLocation(): Boolean {
        return isGetLocation
    }

    override fun isRuning(): Boolean {
        return isRuning
    }

    private fun setLocation(location: Location?) {
        isGetLocation = true
        this.location = location

        if (location != null) {
            ApplicationUtils.runOnUiThread(Runnable {
                for (subscriber in getReadySubscribers()) {
                    subscriber.setLocation(this.location!!)
                }
            })
        }
    }

    override fun stop() {
        stopLocation()
        super.stop()
    }

    override fun isValid(): Boolean {
        val context = ApplicationProvider.appContext
        if (!ApplicationUtils.isGooglePlayServices(context)) {
            return false
        }

        if (!ApplicationUtils.checkPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            return false
        }

        if (!ApplicationUtils.isLocationEnabled(context)) {
            return false
        }
        return true
    }

}
