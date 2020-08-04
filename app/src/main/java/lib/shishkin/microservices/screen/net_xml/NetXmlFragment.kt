package lib.shishkin.microservices.screen.net_xml

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.microservices.action.Actions
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.DataAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.handler.FragmentActionHandler
import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.ui.AbsContentFragment


class NetXmlFragment : AbsContentFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val NAME = "NetXmlFragment"

        fun newInstance(): NetXmlFragment {
            return NetXmlFragment()
        }
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val actionHandler = FragmentActionHandler(this)
    private lateinit var recyclerView: RecyclerView
    private val adapter: ValCursRecyclerViewAdapter = ValCursRecyclerViewAdapter()

    override fun createModel(): IModel {
        return NetXmlModel(this)
    }

    override fun getName(): String {
        return NAME
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_netxml, container, false)
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

    }

    override fun onRefresh() {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
        getModel<NetXmlModel>().getPresenter<NetXmlPresenter>()
            .addAction(ApplicationAction(Actions.OnSwipeRefresh))
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (action is DataAction<*>) {
            when (action.getName()) {
                Actions.RefreshViews -> {
                    refreshViews(action.getData() as ValCursData?)
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

    private fun refreshViews(viewData: ValCursData?) {
        if (viewData == null) return

        adapter.setItems(viewData.getData())
    }

}
