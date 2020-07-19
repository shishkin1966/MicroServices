package lib.shishkin.microservices.screen.accounts

import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import lib.shishkin.common.ApplicationUtils.Companion.findView
import lib.shishkin.common.LinearLayoutBehavior
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.DataAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.MapAction
import lib.shishkin.sl.action.handler.FragmentActionHandler
import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.ui.AbsContentFragment
import org.michaelbel.bottomsheet.BottomSheet


class AccountsFragment : AbsContentFragment(), View.OnClickListener,
    DialogInterface.OnClickListener {

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
    private var sortDialog: DialogInterface? = null
    private var filterDialog: DialogInterface? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun createModel(): IModel {
        return AccountsModel(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = LinearLayoutBehavior.from(this.findView(R.id.bottomSheetContainer)!!)

        view.findViewById<View>(R.id.create_account).setOnClickListener(this)
        view.findViewById<View>(R.id.sort_accounts).setOnClickListener(this)
        view.findViewById<View>(R.id.select_accounts).setOnClickListener(this)

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

        if (action is DataAction<*>) {
            when (action.getName()) {
                Actions.RefreshViews -> {
                    refreshViews(action.getData() as AccountsData?)
                    return true
                }
            }
        }

        if (action is MapAction) {
            when (action.getName()) {
                AccountsPresenter.FilterDialog -> {
                    if (context != null) {
                        val builder = BottomSheet.Builder(context!!)
                        filterDialog = builder
                            .setDividers(true)
                            .setTitleTextColorRes(R.color.blue)
                            .setTitle(R.string.select)
                            .setItems(
                                action.map["Items"] as Array<out CharSequence>,
                                action.map["Icons"] as Array<out Drawable>,
                                this
                            )
                            .show()
                    }
                    return true
                }
            }
        }

        if (action is ApplicationAction) {
            when (action.getName()) {
                AccountsPresenter.SortDialog -> {
                    if (context != null) {
                        val builder = BottomSheet.Builder(context!!)
                        sortDialog = builder
                            .setDividers(true)
                            .setTitleTextColorRes(R.color.blue)
                            .setTitle(R.string.sort)
                            .setMenu(R.menu.sort_menu, this)
                            .show()
                    }
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
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    private fun refreshViews(viewData: AccountsData?) {
        if (viewData == null) return

        accountsAdapter.setItems(viewData.getData())
        findView<View>(R.id.sort_accounts)?.isEnabled = viewData.isSortMenuEnabled()
        findView<View>(R.id.select_accounts)?.isEnabled = viewData.isFilterMenuEnabled()

        showAccountsBalance(viewData.balance)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        accountsView.adapter = null
        balanceView.adapter = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.create_account -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                getModel<AccountsModel>().getPresenter<AccountsPresenter>()
                    .addAction(ApplicationAction(AccountsPresenter.OnClickCreateAccount))
            }
            R.id.sort_accounts -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                getModel<AccountsModel>().getPresenter<AccountsPresenter>()
                    .addAction(ApplicationAction(AccountsPresenter.OnClickSort))
            }
            R.id.select_accounts -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                getModel<AccountsModel>().getPresenter<AccountsPresenter>()
                    .addAction(ApplicationAction(AccountsPresenter.OnClickFilter))
            }
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (dialog) {
            sortDialog -> {
                getModel<AccountsModel>().getPresenter<AccountsPresenter>()
                    .addAction(DialogClickAction(AccountsPresenter.SortDialog, which))
            }
            filterDialog -> {
                getModel<AccountsModel>().getPresenter<AccountsPresenter>()
                    .addAction(DialogClickAction(AccountsPresenter.FilterDialog, which))
            }
        }
    }

    override fun isTop(): Boolean {
        return true
    }

    private fun showAccountsBalance(list: List<Balance>?) {
        if (list == null) return

        balanceAdapter.setItems(list)
    }

    override fun getName(): String {
        return NAME
    }

}
