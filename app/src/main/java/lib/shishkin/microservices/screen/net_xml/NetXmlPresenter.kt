package lib.shishkin.microservices.screen.net_xml

import lib.shishkin.common.ApplicationUtils
import lib.shishkin.common.formatDate
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.action.Actions
import lib.shishkin.microservices.data.ValCurs
import lib.shishkin.microservices.provider.Providers
import lib.shishkin.sl.action.*
import lib.shishkin.sl.data.ExtResult
import lib.shishkin.sl.model.AbsModelPresenter
import lib.shishkin.sl.request.IResponseListener

class NetXmlPresenter(model: NetXmlModel) : AbsModelPresenter(model),
    IResponseListener {
    companion object {
        const val NAME = "NetXmlPresenter"
    }

    private lateinit var data: ValCursData

    override fun isRegister(): Boolean {
        return true
    }

    override fun getName(): String {
        return NAME
    }

    override fun onStart() {
        if (!::data.isInitialized) {
            data = ValCursData()
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

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }

    private fun getData() {
        ApplicationUtils.runOnUiThread(Runnable {
            getView<NetXmlFragment>().addAction(ShowProgressBarAction())
        })
        Providers.getValCurs(getName(), System.currentTimeMillis().formatDate("dd/MM/yyyy"))
    }

    override fun response(result: ExtResult) {
        ApplicationUtils.runOnUiThread(Runnable {
            getView<NetXmlFragment>().addAction(HideProgressBarAction())
            if (!result.hasError()) {
                data.valCurs = result.getData() as ValCurs
                getView<NetXmlFragment>().addAction(DataAction(Actions.RefreshViews, data))
            } else {
                getView<NetXmlFragment>().addAction(
                    ShowMessageAction(result.getErrorText()).setType(
                        ApplicationUtils.MESSAGE_TYPE_ERROR
                    )
                )
            }
        })
    }
}
