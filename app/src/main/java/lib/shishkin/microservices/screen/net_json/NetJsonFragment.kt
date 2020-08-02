package lib.shishkin.microservices.screen.net_json

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.microservices.action.Actions
import lib.shishkin.microservices.action.OnEditTextChangedAction
import lib.shishkin.microservices.observe.EditTextObservable
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.DataAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.handler.FragmentActionHandler
import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.provider.ApplicationProvider
import lib.shishkin.sl.ui.AbsContentFragment
import java.util.*


class NetJsonFragment : AbsContentFragment(), SwipeRefreshLayout.OnRefreshListener,
    Observer {

    companion object {
        const val NAME = "NetJsonFragment"

        fun newInstance(): NetJsonFragment {
            return NetJsonFragment()
        }
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val actionHandler = FragmentActionHandler(this)
    private lateinit var recyclerView: RecyclerView
    private val adapter: TickerRecyclerViewAdapter = TickerRecyclerViewAdapter()
    private var observable: EditTextObservable? = null
    private lateinit var searchView: EditText

    override fun createModel(): IModel {
        return NetJsonModel(this)
    }

    override fun getName(): String {
        return NAME
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_netjson, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeResources(R.color.blue)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_light)
        swipeRefreshLayout.setOnRefreshListener(this)

        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        searchView = view.findViewById(R.id.search)
        val context = ApplicationProvider.appContext
        searchView.setCompoundDrawablesWithIntrinsicBounds(
            ApplicationUtils.getVectorDrawable(
                context,
                R.drawable.magnify,
                context.theme
            ), null, null, null
        )

        view.findViewById<View>(R.id.clear).setOnClickListener {
            searchView.setText("")
        }
    }

    override fun onRefresh() {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
        getModel<NetJsonModel>().getPresenter<NetJsonPresenter>()
            .addAction(ApplicationAction(Actions.OnSwipeRefresh))
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is DataAction<*>) {
            when (action.getName()) {
                Actions.RefreshViews -> {
                    refreshViews(action.getData() as TickerData?)
                    return true
                }
                NetJsonPresenter.InitFilter -> {
                    initFilter(action.getData() as TickerData?)
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

    override fun onDestroyView() {
        super.onDestroyView()

        recyclerView.adapter = null
    }

    private fun refreshViews(viewData: TickerData?) {
        if (viewData == null) return

        adapter.setItems(viewData.getData())
    }

    override fun update(o: Observable?, arg: Any?) {
        getModel<NetJsonModel>().getPresenter<NetJsonPresenter>().addAction(
            OnEditTextChangedAction(o, arg)
        )
    }

    private fun initFilter(viewData: TickerData?) {
        observable?.finish()
        searchView.setText(viewData?.filter)
        observable = EditTextObservable(this, searchView)
    }
}
