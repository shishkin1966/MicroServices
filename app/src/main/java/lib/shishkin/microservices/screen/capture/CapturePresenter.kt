package lib.shishkin.microservices.screen.capture

import android.Manifest
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.action.Actions
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.model.AbsModelPresenter
import lib.shishkin.sl.provider.ApplicationProvider

class CapturePresenter(model: CaptureModel) : AbsModelPresenter(model) {
    companion object {
        const val NAME = "CapturePresenter"
        const val Capture = "Capture"
        const val TakeFoto = "TakeFoto"
        const val SelectFoto = "SelectFoto"
    }

    override fun isRegister(): Boolean {
        return true
    }

    override fun getName(): String {
        return NAME
    }

    override fun onStart() {
        getData()
    }

    fun getData() {
        if (!ApplicationUtils.checkPermission(
                ApplicationProvider.appContext,
                Manifest.permission.CAMERA
            ) && !ApplicationUtils.checkPermission(
                ApplicationProvider.appContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            ApplicationUtils.grantPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), getView<CaptureFragment>()
            )
            return
        }
        if (!ApplicationUtils.checkPermission(
                ApplicationProvider.appContext,
                Manifest.permission.CAMERA
            )
        ) {
            ApplicationUtils.grantPermissions(
                arrayOf(Manifest.permission.CAMERA),
                getView<CaptureFragment>()
            )
            return
        }
        if (!ApplicationUtils.checkPermission(
                ApplicationProvider.appContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            ApplicationUtils.grantPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                getView<CaptureFragment>()
            )
            return
        }
        getView<CaptureFragment>().addAction(ApplicationAction(Capture))
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is ApplicationAction) {
            when (action.getName()) {
                Actions.OnSwipeRefresh -> {
                    getData()
                    return true
                }
            }
        }

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }
}
