package lib.shishkin.microservices.screen.home

import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.action.Actions
import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.data.Balance
import lib.shishkin.microservices.data.Card
import lib.shishkin.microservices.data.Deposit
import lib.shishkin.microservices.provider.Providers
import lib.shishkin.microservices.request.GetAccountsRequest
import lib.shishkin.microservices.request.GetBalanceRequest
import lib.shishkin.microservices.request.GetCurrencyRequest
import lib.shishkin.microservices.screen.create_account.CreateAccountFragment
import lib.shishkin.microservices.screen.more.MoreFragment
import lib.shishkin.sl.action.*
import lib.shishkin.sl.data.ExtResult
import lib.shishkin.sl.model.AbsModelPresenter
import lib.shishkin.sl.observe.IObjectObservableSubscriber
import lib.shishkin.sl.observe.ObjectObservable
import lib.shishkin.sl.provider.IRouterProvider
import lib.shishkin.sl.provider.ObservableUnion
import lib.shishkin.sl.request.IResponseListener

class HomePresenter(model: HomeModel) : AbsModelPresenter(model), IResponseListener,
    IObjectObservableSubscriber {

    companion object {
        const val NAME = "HomePresenter"
        const val OnClickAccount = "OnClickAccount"
        const val OnClickCreateAccount = "OnClickCreateAccount"
        const val OnClickCreateDeposit = "OnClickCreateDeposit"
        const val OnClickCreateCard = "OnClickCreateCard"
        const val OnClickMore = "OnClickMore"
        const val RefreshAccounts = "RefreshAccounts"
        const val RefreshBalance = "RefreshBalance"
    }

    private lateinit var data: HomeData

    override fun isRegister(): Boolean {
        return true
    }

    override fun getName(): String {
        return NAME
    }

    override fun onStart() {
        if (!::data.isInitialized) {
            data = HomeData()
            getData()
        }
    }

    private fun getData() {
        ApplicationUtils.runOnUiThread(Runnable {
            getView<HomeFragment>().addAction(ShowProgressBarAction())
        })
        getAccounts()
    }

    private fun getAccounts() {
        Providers.getAccounts(getName())
        Providers.getBalance(getName())
        Providers.getCurrency(getName())
    }

    override fun response(result: ExtResult) {
        ApplicationUtils.runOnUiThread(Runnable {
            if (!result.hasError()) {
                when (result.getName()) {
                    GetAccountsRequest.NAME -> {
                        data.accounts = result.getData() as List<Account>
                        getView<HomeFragment>().addAction(DataAction(RefreshAccounts, data))
                    }
                    GetBalanceRequest.NAME -> {
                        data.balance = result.getData() as List<Balance>
                        getView<HomeFragment>().addAction(DataAction(RefreshBalance, data))
                    }
                    GetCurrencyRequest.NAME -> {
                        data.currencies = result.getData() as List<String>
                    }
                }
                if (data.isFull()) {
                    getView<HomeFragment>().addAction(HideProgressBarAction())
                }
            } else {
                getView<HomeFragment>().addAction(
                    ShowMessageAction(result.getErrorText()!!).setType(
                        ApplicationUtils.MESSAGE_TYPE_ERROR
                    )
                )
            }
        })
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is DataAction<*>) {
            when (action.getName()) {
                OnClickAccount -> {
                    return true
                }
            }
        }

        if (action is ApplicationAction) {
            when (action.getName()) {
                OnClickCreateAccount -> {
                    createAccount()
                    return true
                }
                OnClickCreateDeposit -> {
                    createDeposit()
                    return true
                }
                OnClickCreateCard -> {
                    createCard()
                    return true
                }
                OnClickMore -> {
                    showMore()
                    return true
                }
                Actions.OnSwipeRefresh -> {
                    data.clear()
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

    override fun getListenObjects(): List<String> {
        return listOf(Account.TABLE, Deposit.TABLE, Card.TABLE)
    }

    override fun getObservable(): List<String> {
        return listOf(ObjectObservable.NAME)
    }

    override fun onChange(name: String, obj: Any) {
        if (name == ObjectObservable.NAME) {
            when (obj.toString()) {
                Account.TABLE -> {
                    data.clearAccounts()
                    getAccounts()
                }
            }
        }
    }

    override fun getProviderSubscription(): List<String> {
        val list = ArrayList<String>(super.getProviderSubscription())
        list.add(ObservableUnion.NAME)
        return list
    }

    private fun createAccount() {
        val activity = getView<HomeFragment>().activity
        if (activity is IRouterProvider && activity.isValid()) {
            activity.showFragment(CreateAccountFragment.newInstance())
        }
    }

    private fun createDeposit() {
        val activity = getView<HomeFragment>().activity
        if (activity is IRouterProvider && activity.isValid()) {
            // activity.showFragment(CreateAccountFragment.newInstance())
        }
    }

    private fun createCard() {
        val activity = getView<HomeFragment>().activity
        if (activity is IRouterProvider && activity.isValid()) {
            // activity.showFragment(CreateAccountFragment.newInstance())
        }
    }

    private fun showMore() {
        val activity = getView<HomeFragment>().activity
        if (activity is IRouterProvider && activity.isValid()) {
            activity.showFragment(MoreFragment.newInstance())
        }
    }
}
