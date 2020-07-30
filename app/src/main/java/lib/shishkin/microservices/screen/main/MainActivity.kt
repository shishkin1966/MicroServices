package lib.shishkin.microservices.screen.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.OnBackPressedPresenter
import lib.shishkin.microservices.R
import lib.shishkin.microservices.screen.home.HomeFragment
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.DataAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.SnackBarAction
import lib.shishkin.sl.action.handler.ActivityActionHandler
import lib.shishkin.sl.ui.AbsContentActivity

class MainActivity : AbsContentActivity() {
    private val actionHandler = ActivityActionHandler(this)
    private val onBackPressedPresenter = OnBackPressedPresenter()

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

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

        if (actionHandler.onAction(action)) return true

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

        ApplicationSingleton.instance.addNotification(
            getString(R.string.app_name),
            getString(R.string.app_start)
        )
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

    override fun onBackPressed() {
        if (!onBackPressedPresenter.onClick()) {
            super.onBackPressed()
        }
    }

    override fun showRootFragment() {
        clearBackStack()
        showFragment(HomeFragment.newInstance(), true)
    }

    private fun clearIntent() {
        intent = null
    }
}
