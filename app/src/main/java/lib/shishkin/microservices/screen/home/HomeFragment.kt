package lib.shishkin.microservices.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.DataAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.handler.FragmentActionHandler
import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.ui.AbsContentFragment


class HomeFragment : AbsContentFragment(), SwipeRefreshLayout.OnRefreshListener {

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
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun createModel(): IModel {
        return HomeModel(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.CreateDeposit).setOnClickListener(this::onClick)
        view.findViewById<TextView>(R.id.CreateAccount).setOnClickListener(this::onClick)
        view.findViewById<TextView>(R.id.CreateCard).setOnClickListener(this::onClick)

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

        view.findViewById<TextView>(R.id.more).setOnClickListener(this::onClick)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeResources(R.color.blue)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_light)
        swipeRefreshLayout.setOnRefreshListener(this)

    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is DataAction<*>) {
            when (action.getName()) {
                HomePresenter.RefreshAccounts -> {
                    refreshAccounts(action.getData() as HomeData?)
                    return true
                }
                HomePresenter.RefreshBalance -> {
                    refreshBalance(action.getData() as HomeData?)
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

    private fun refreshAccounts(viewData: HomeData?) {
        if (viewData == null) return

        accountsAdapter.setItems(viewData.accounts!!)
    }

    private fun refreshBalance(viewData: HomeData?) {
        if (viewData == null) return

        balanceAdapter.setItems(viewData.balance!!)
    }

    override fun getName(): String {
        return NAME
    }

    private fun onClick(v: View?) {
        when (v?.id) {
            R.id.CreateDeposit -> {
                getModel<HomeModel>().getPresenter<HomePresenter>()
                    .addAction(ApplicationAction(HomePresenter.OnClickCreateDeposit))
            }
            R.id.CreateAccount -> {
                getModel<HomeModel>().getPresenter<HomePresenter>()
                    .addAction(ApplicationAction(HomePresenter.OnClickCreateAccount))
            }
            R.id.CreateCard -> {
                getModel<HomeModel>().getPresenter<HomePresenter>()
                    .addAction(ApplicationAction(HomePresenter.OnClickCreateCard))
            }
            R.id.more -> {
                getModel<HomeModel>().getPresenter<HomePresenter>()
                    .addAction(ApplicationAction(HomePresenter.OnClickMore))
            }
        }
    }

    override fun onRefresh() {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
        getModel<HomeModel>().getPresenter<HomePresenter>()
            .addAction(ApplicationAction(HomePresenter.OnSwipeRefresh))
    }

}
