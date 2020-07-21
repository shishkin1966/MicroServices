package lib.shishkin.microservices.screen.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.OnBackPressedPresenter
import lib.shishkin.microservices.R
import lib.shishkin.microservices.ServiceLocatorSingleton
import lib.shishkin.microservices.screen.accounts.AccountsFragment
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.DataAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.SnackBarAction
import lib.shishkin.sl.action.handler.ActivityActionHandler
import lib.shishkin.sl.provider.ErrorProvider
import lib.shishkin.sl.provider.IErrorProvider
import lib.shishkin.sl.ui.AbsContentActivity

class MainActivity : AbsContentActivity() {
    private val actionHandler = ActivityActionHandler(this)
    private val onBackPressedPresenter = OnBackPressedPresenter()

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (actionHandler.onAction(action)) return true

        if (action is SnackBarAction) {
            onBackPressedPresenter.onClick()
            return true
        }

        if (action is ApplicationAction) {
            when (action.getName()) {
                MainPresenter.ClearIntentAction -> {
                    clearIntent()
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

    override fun createModel(): MainModel {
        return MainModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()

        if (intent != null) {
            getModel<MainModel>().getPresenter<MainPresenter>()
                .addAction(DataAction(MainPresenter.IntentAction, intent))
        }
    }

    override fun getContentResId(): Int {
        return R.id.content
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        this.intent = intent
    }

    override fun onStart() {
        super.onStart()

        addLifecycleObserver(onBackPressedPresenter)

        ApplicationUtils.grantPermissions(
            permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            activity = this
        )

        if (intent != null) {
            getModel<MainModel>().getPresenter<MainPresenter>()
                .addAction(DataAction(MainPresenter.IntentAction, intent))
        } else {
            val fragment =
                ApplicationSingleton.instance.activityProvider.getCurrentFragment<Fragment>()
            if (fragment == null) {
                showRootFragment()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == ApplicationUtils.REQUEST_PERMISSIONS) {
            for (permission in permissions) {
                if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    val union =
                        ServiceLocatorSingleton.instance.get<IErrorProvider>(
                            ErrorProvider.NAME
                        )
                    union?.checkLog(applicationContext)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (!onBackPressedPresenter.onClick()) {
            super.onBackPressed()
        }
    }

    override fun showRootFragment() {
        clearBackStack()
        showFragment(AccountsFragment.newInstance(), true)
    }

    private fun clearIntent() {
        intent = null
    }
}
