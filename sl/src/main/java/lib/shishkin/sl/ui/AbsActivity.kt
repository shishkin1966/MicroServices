package lib.shishkin.sl.ui

import android.content.pm.PackageManager
import android.view.View
import androidx.annotation.IdRes

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import lib.shishkin.sl.IProvider
import lib.shishkin.sl.R
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.lifecycle.ILifecycle
import lib.shishkin.sl.lifecycle.Lifecycle
import lib.shishkin.sl.lifecycle.LifecycleObservable
import lib.shishkin.sl.provider.ActivityUnion
import lib.shishkin.sl.provider.ApplicationProvider
import java.util.*
import kotlin.collections.ArrayList

abstract class AbsActivity : AppCompatActivity(), IActivity {

    private val lifecycleObservable = LifecycleObservable(Lifecycle.VIEW_CREATE)
    private val actions = LinkedList<IAction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        lifecycleObservable.setState(Lifecycle.VIEW_CREATE)
    }

    override fun <V : View> findView(@IdRes id: Int): V? {
        return ApplicationUtils.findView(this, id)
    }

    override fun onStart() {
        super.onStart()

        doActions()

        ApplicationProvider.serviceLocator?.registerSubscriber(this)

        lifecycleObservable.setState(Lifecycle.VIEW_READY)
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycleObservable.setState(Lifecycle.VIEW_DESTROY)
        lifecycleObservable.clearLifecycleObservers()

        ApplicationProvider.serviceLocator?.unregisterSubscriber(this)
    }

    override fun onResume() {
        super.onResume()

        ApplicationProvider.serviceLocator?.setCurrentSubscriber(this)
    }

    override fun getProviderSubscription(): List<String> {
        return listOf(ActivityUnion.NAME)
    }

    override fun clearBackStack() {
        BackStack.clearBackStack(this)
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

    override fun getState(): Int {
        return lifecycleObservable.getState()
    }

    override fun setState(state: Int) {}

    override fun isValid(): Boolean {
        return getState() != Lifecycle.VIEW_DESTROY && !isFinishing
    }

    override fun stop() {
        if (ApplicationUtils.hasJellyBean()) {
            super.finishAffinity()
        } else {
            super.finish()
        }
    }

    override fun onPermissionGranted(permission: String) {}

    override fun onPermissionDenied(permission: String) {}

    override fun getRootView(): View {
        val view = findView<View>(R.id.root)
        return if (view != null) {
            return view
        } else (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The default implementation simply finishes the current activity,
     * but you can override this to do whatever you want.
     */
    fun onActivityBackPressed() {
        super.onBackPressed()
    }

    override fun addLifecycleObserver(stateable: ILifecycle) {
        lifecycleObservable.addLifecycleObserver(stateable)
    }

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

    override fun onStopProvider(provider: IProvider) {
    }

    override fun getName(): String {
        return this::class.java.simpleName
    }

}
