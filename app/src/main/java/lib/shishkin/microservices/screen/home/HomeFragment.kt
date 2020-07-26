package lib.shishkin.microservices.screen.home

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


class HomeFragment : AbsContentFragment() {

    companion object {
        const val NAME = "HomeFragment"

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val actionHandler = FragmentActionHandler(this)
    private val accountsAdapter: AccountsRecyclerViewAdapter =
        AccountsRecyclerViewAdapter()
    private val balanceAdapter: BalanceRecyclerViewAdapter = BalanceRecyclerViewAdapter()
    private lateinit var accountsView: RecyclerView
    private lateinit var balanceView: RecyclerView
    private lateinit var depositsView: RecyclerView
    private lateinit var cardsView: RecyclerView

    override fun createModel(): IModel {
        return HomeModel(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountsView = view.findViewById(R.id.accounts_list)
        accountsView.layoutManager = LinearLayoutManager(activity)
        accountsView.itemAnimator = DefaultItemAnimator()
        accountsView.adapter = accountsAdapter

        depositsView = view.findViewById(R.id.deposits_list)
        depositsView.layoutManager = LinearLayoutManager(activity)
        depositsView.itemAnimator = DefaultItemAnimator()

        cardsView = view.findViewById(R.id.cards_list)
        cardsView.layoutManager = LinearLayoutManager(activity)
        cardsView.itemAnimator = DefaultItemAnimator()

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
                    refreshViews(action.getData() as HomeData?)
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        accountsView.adapter = null
        balanceView.adapter = null
    }

    override fun isTop(): Boolean {
        return true
    }

    private fun refreshViews(viewData: HomeData?) {
        if (viewData == null) return

        accountsAdapter.setItems(viewData.accounts)
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
