package lib.shishkin.microservices.screen.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.microservices.action.Actions
import lib.shishkin.microservices.action.ImageAction
import lib.shishkin.microservices.action.OnEditTextChangedAction
import lib.shishkin.microservices.data.ValCurs
import lib.shishkin.microservices.observe.EditTextObservable
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.DataAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.handler.FragmentActionHandler
import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.provider.ApplicationProvider
import lib.shishkin.sl.ui.AbsContentFragment
import java.util.*


class NetImageFragment : AbsContentFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val NAME = "NetImageFragment"
        const val URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c8/%D0%A1%D0%BE%D1%81%D0%BD%D1%8B%2C_%D0%BE%D1%81%D0%B2%D0%B5%D1%89%D0%B5%D0%BD%D0%BD%D1%8B%D0%B5_%D1%81%D0%BE%D0%BB%D0%BD%D1%86%D0%B5%D0%BC_%28%D0%A8%D0%B8%D1%88%D0%BA%D0%B8%D0%BD%29.jpg/1200px-%D0%A1%D0%BE%D1%81%D0%BD%D1%8B%2C_%D0%BE%D1%81%D0%B2%D0%B5%D1%89%D0%B5%D0%BD%D0%BD%D1%8B%D0%B5_%D1%81%D0%BE%D0%BB%D0%BD%D1%86%D0%B5%D0%BC_%28%D0%A8%D0%B8%D1%88%D0%BA%D0%B8%D0%BD%29.jpg"

        fun newInstance(): NetImageFragment {
            return NetImageFragment()
        }
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var image: ImageView
    private val actionHandler = FragmentActionHandler(this)

    override fun createModel(): IModel {
        return NetImageModel(this)
    }

    override fun getName(): String {
        return NAME
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeResources(R.color.blue)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_light)
        swipeRefreshLayout.setOnRefreshListener(this)

        image = view.findViewById(R.id.image)

        getModel<NetImageModel>().getPresenter<NetImagePresenter>().addAction(ImageAction(image, URL))
    }

    override fun onRefresh() {
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
        }
        getModel<NetImageModel>().getPresenter<NetImagePresenter>()
            .addAction(ApplicationAction(Actions.OnSwipeRefresh))
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (actionHandler.onAction(action)) return true

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }

}
