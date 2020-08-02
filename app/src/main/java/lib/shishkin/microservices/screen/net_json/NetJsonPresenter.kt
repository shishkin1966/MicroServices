package lib.shishkin.microservices.screen.net_json

import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.action.Actions
import lib.shishkin.microservices.action.OnEditTextChangedAction
import lib.shishkin.microservices.data.Ticker
import lib.shishkin.microservices.provider.Providers
import lib.shishkin.sl.action.*
import lib.shishkin.sl.data.ExtResult
import lib.shishkin.sl.model.AbsModelPresenter
import lib.shishkin.sl.request.IResponseListener

class NetJsonPresenter(model: NetJsonModel) : AbsModelPresenter(model),
    IResponseListener {
    companion object {
        const val NAME = "NetJsonPresenter"
        const val InitFilter = "InitFilter"
    }

    private lateinit var data: TickerData

    override fun isRegister(): Boolean {
        return true
    }

    override fun getName(): String {
        return NAME
    }

    override fun onStart() {
        if (!::data.isInitialized) {
            data = TickerData()
            getView<NetJsonFragment>().addAction(DataAction(InitFilter, data))
            getData()
        }
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

        if (action is OnEditTextChangedAction) {
            if (!::data.isInitialized) {
                data = TickerData()
            }
            val arg = action.obj as String?
            if (arg != data.filter) {
                data.filter = arg
                getView<NetJsonFragment>().addAction(
                    DataAction(
                        Actions.RefreshViews,
                        data
                    )
                )
            }
            return true
        }

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }

    private fun getData() {
        ApplicationUtils.runOnUiThread(Runnable {
            getView<NetJsonFragment>().addAction(ShowProgressBarAction())
        })
        Providers.getTickers(getName())
    }

    override fun response(result: ExtResult) {
        ApplicationUtils.runOnUiThread(Runnable {
            getView<NetJsonFragment>().addAction(HideProgressBarAction())
            if (!result.hasError()) {
                data.tickers = ArrayList(result.getData() as List<Ticker>)
                getView<NetJsonFragment>().addAction(DataAction(Actions.RefreshViews, data))
            } else {
                getView<NetJsonFragment>().addAction(
                    ShowMessageAction(result.getErrorText()).setType(
                        ApplicationUtils.MESSAGE_TYPE_ERROR
                    )
                )
            }
        })
    }

    override fun onDestroyView() {
        data.saveFilter()

        super.onDestroyView()
    }
}
