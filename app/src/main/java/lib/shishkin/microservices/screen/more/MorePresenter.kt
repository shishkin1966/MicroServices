package lib.shishkin.microservices.screen.more

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.screen.map.MapFragment
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.model.AbsModelPresenter
import lib.shishkin.sl.provider.IRouterProvider

class MorePresenter(model: MoreModel) : AbsModelPresenter(model) {
    companion object {
        const val NAME = "MorePresenter"
        const val OnClickMap = "OnClickMap"
        const val OnClickNet = "OnClickNet"
    }

    override fun isRegister(): Boolean {
        return true
    }

    override fun getName(): String {
        return NAME
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is ApplicationAction) {
            when (action.getName()) {
                OnClickMap -> {
                    showMapFragment()
                    return true
                }
                OnClickNet -> {
                    showNetFragment()
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

    private fun showNetFragment() {
    }

    private fun showMapFragment() {
        val activity = getView<MoreFragment>().activity
        if (activity is IRouterProvider && activity.isValid()) {
            activity.showFragment(MapFragment.newInstance())
        }
    }
}
