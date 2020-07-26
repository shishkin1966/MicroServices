package lib.shishkin.microservices.screen.accounts

import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.action.Actions
import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.data.Balance
import lib.shishkin.microservices.provider.Providers
import lib.shishkin.microservices.request.GetAccountsRequest
import lib.shishkin.microservices.request.GetBalanceRequest
import lib.shishkin.microservices.request.GetCurrencyRequest
import lib.shishkin.sl.action.*
import lib.shishkin.sl.data.ExtResult
import lib.shishkin.sl.model.AbsModelPresenter
import lib.shishkin.sl.observe.IObjectObservableSubscriber
import lib.shishkin.sl.observe.ObjectObservable
import lib.shishkin.sl.provider.ObservableUnion
import lib.shishkin.sl.request.IResponseListener

class AccountsPresenter(model: AccountsModel) : AbsModelPresenter(model), IResponseListener,
    IObjectObservableSubscriber {

    companion object {
        const val NAME = "AccountsPresenter"
        const val OnClickAccount = "OnClickAccount"
    }

    private lateinit var data: AccountsData

    override fun isRegister(): Boolean {
        return true
    }

    override fun getName(): String {
        return NAME
    }

    override fun onStart() {
        if (!::data.isInitialized) {
            data = AccountsData()
            getData()
        } else {
            getView<AccountsFragment>().addAction(DataAction(Actions.RefreshViews, data))
        }
    }

    private fun getData() {
        getView<AccountsFragment>().addAction(ShowProgressBarAction())
        Providers.getAccounts(getName())
        Providers.getBalance(getName())
        Providers.getCurrency(getName())
    }

    override fun response(result: ExtResult) {
        getView<AccountsFragment>().addAction(HideProgressBarAction())
        if (!result.hasError()) {
            when (result.getName()) {
                GetAccountsRequest.NAME -> {
                    data.accounts = result.getData() as List<Account>
                    getView<AccountsFragment>().addAction(DataAction(Actions.RefreshViews, data))
                }
                GetBalanceRequest.NAME -> {
                    data.balance = result.getData() as List<Balance>
                    getView<AccountsFragment>().addAction(DataAction(Actions.RefreshViews, data))
                }
                GetCurrencyRequest.NAME -> {
                    data.currencies = result.getData() as List<String>
                }
            }
        } else {
            getView<AccountsFragment>().addAction(
                ShowMessageAction(result.getErrorText()!!).setType(
                    ApplicationUtils.MESSAGE_TYPE_ERROR
                )
            )
        }
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }

    override fun getListenObjects(): List<String> {
        return listOf(Account.TABLE)
    }

    override fun getObservable(): List<String> {
        return listOf(ObjectObservable.NAME)
    }

    override fun onChange(name: String, obj: Any) {
        if (name == ObjectObservable.NAME) {
            when (obj.toString()) {
                Account.TABLE -> {
                    getData()
                }
            }
        }
    }

    override fun getProviderSubscription(): List<String> {
        val list = ArrayList<String>(super.getProviderSubscription())
        list.add(ObservableUnion.NAME)
        return list
    }

}
