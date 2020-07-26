package lib.shishkin.microservices.screen.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.microservices.action.Actions
import lib.shishkin.microservices.data.Balance
import lib.shishkin.sl.action.DataAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.handler.FragmentActionHandler
import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.ui.AbsContentFragment


class AccountsFragment : AbsContentFragment() {

    companion object {
        const val NAME = "AccountsFragment"

        fun newInstance(): AccountsFragment {
            return AccountsFragment()
        }
    }

    private val actionHandler = FragmentActionHandler(this)
    private val accountsAdapter: AccountsRecyclerViewAdapter =
        AccountsRecyclerViewAdapter()
    private val balanceAdapter: BalanceRecyclerViewAdapter = BalanceRecyclerViewAdapter()
    private lateinit var accountsView: RecyclerView
    private lateinit var balanceView: RecyclerView

    override fun createModel(): IModel {
        return AccountsModel(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountsView = view.findViewById(R.id.list)
        accountsView.layoutManager = LinearLayoutManager(activity)
        accountsView.itemAnimator = DefaultItemAnimator()
        accountsView.adapter = accountsAdapter

        balanceView = view.findViewById(R.id.balance_list)
        balanceView.layoutManager = LinearLayoutManager(activity)
        balanceView.adapter = balanceAdapter
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (actionHandler.onAction(action)) return true

        if (action is DataAction<*>) {
            when (action.getName()) {
                Actions.RefreshViews -> {
                    refreshViews(action.getData() as AccountsData?)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        accountsView.adapter = null
        balanceView.adapter = null
    }

    override fun isTop(): Boolean {
        return true
    }

    private fun refreshViews(viewData: AccountsData?) {
        if (viewData == null) return

        accountsAdapter.setItems(viewData.getData())
        showAccountsBalance(viewData.balance)
    }

    private fun showAccountsBalance(list: List<Balance>?) {
        if (list == null) return

        balanceAdapter.setItems(list)
    }

    override fun getName(): String {
        return NAME
    }

}
