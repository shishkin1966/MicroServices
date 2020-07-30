package lib.shishkin.microservices.screen.map

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.microservices.action.Actions
import lib.shishkin.sl.action.DataAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.PermissionAction
import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.ui.AbsContentFragment

class MapFragment : AbsContentFragment() {
    companion object {
        const val NAME = "MapFragment"

        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun createModel(): IModel {
        return MapModel(this)
    }

    override fun getName(): String {
        return NAME
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is DataAction<*>) {
            when (action.getName()) {
                Actions.RefreshViews -> {
                    refreshViews(action.getData() as MapData?)
                }
            }
            return true
        }

        if (action is PermissionAction) {
            ApplicationUtils.grantPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                this
            )
            return true
        }

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }

    override fun onPermissionGranted(permission: String) {
        when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                startMap()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        startMap()
    }

    private fun startMap() {
        if (ApplicationUtils.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            val mapOptions = GoogleMapOptions()
                .compassEnabled(true)
                .zoomControlsEnabled(false)
                .mapType(GoogleMap.MAP_TYPE_NORMAL)
            val fragment = SupportMapFragment.newInstance(mapOptions)
            fragment.getMapAsync(getModel<MapModel>().getPresenter())
            val transaction = childFragmentManager.beginTransaction()
            transaction.add(R.id.map, fragment, "map")
            transaction.commit()
        }
    }

    /*
    override fun onBackPressed(): Boolean {
        ApplicationSingleton.instance.activityProvider.switchToTopFragment()
        return true
    }
    */

    private fun refreshViews(data: MapData?) {
        if (data != null) {
            (findView<TextView>(R.id.name) as TextView).text = data.address
        }
    }


}
