package lib.shishkin.sl.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.sl.IProviderSubscriber
import lib.shishkin.sl.R
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.lifecycle.ILifecycle
import lib.shishkin.sl.lifecycle.Lifecycle
import lib.shishkin.sl.lifecycle.LifecycleObservable
import lib.shishkin.sl.provider.ApplicationProvider
import java.util.*


abstract class AbsFragment : Fragment(), IFragment {

    private val stateObservable = LifecycleObservable(Lifecycle.VIEW_CREATE)
    private val actions = LinkedList<IAction>()

    override fun <V : View> findView(@IdRes id: Int): V? {
        val root = view
        return if (root != null) {
            ApplicationUtils.findView(root, id)
        } else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stateObservable.setState(Lifecycle.VIEW_CREATE)
    }

    override fun onStart() {
        super.onStart()

        doActions()

        if (this is IProviderSubscriber) {
            ApplicationProvider.serviceLocator?.registerSubscriber(this)
        }

        stateObservable.setState(Lifecycle.VIEW_READY)
    }

    override fun onDestroy() {
        super.onDestroy()

        stateObservable.setState(Lifecycle.VIEW_DESTROY)
        stateObservable.clearLifecycleObservers()

        if (this is IProviderSubscriber) {
            ApplicationProvider.serviceLocator?.unregisterSubscriber(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        stateObservable.setState(Lifecycle.VIEW_NOT_READY)
    }

    override fun getState(): Int {
        return stateObservable.getState()
    }

    override fun setState(state: Int) {}

    override fun stop() {
        if (activity != null) {
            (activity as AppCompatActivity).onBackPressed()
        }
    }

    override fun isValid(): Boolean {
        return getState() != Lifecycle.VIEW_DESTROY
    }

    override fun getRootView(): View? {
        val view = findView<View>(R.id.root)
        return view ?: getView()
    }

    override fun addLifecycleObserver(stateable: ILifecycle) {
        stateObservable.addLifecycleObserver(stateable)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permissions[i])
            } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                onPermissionDenied(permissions[i])
            }
        }
    }

    override fun onPermissionGranted(permission: String) {}

    override fun onPermissionDenied(permission: String) {}

    override fun addAction(action: IAction) {
        ApplicationUtils.runOnUiThread(Runnable {
            when (getState()) {
                Lifecycle.VIEW_READY -> {
                    actions.add(action)
                    doActions()
                }

                Lifecycle.VIEW_CREATE, Lifecycle.VIEW_NOT_READY -> {
                    actions.add(action)
                }
            }
        })
    }

    private fun doActions() {
        val deleted = ArrayList<IAction>()
        for (i in actions.indices) {
            if (getState() != Lifecycle.VIEW_READY) {
                break
            }
            onAction(actions[i])
            deleted.add(actions[i])
        }
        for (action in deleted) {
            actions.remove(action)
        }
    }

}
