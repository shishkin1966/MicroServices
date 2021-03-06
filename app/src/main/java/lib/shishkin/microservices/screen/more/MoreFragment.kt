package lib.shishkin.microservices.screen.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.sl.action.ApplicationAction
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.handler.FragmentActionHandler
import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.ui.AbsContentFragment


class MoreFragment : AbsContentFragment() {
    companion object {
        const val NAME = "MoreFragment"

        fun newInstance(): MoreFragment {
            return MoreFragment()
        }
    }

    private val actionHandler = FragmentActionHandler(this)

    override fun createModel(): IModel {
        return MoreModel(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<LinearLayout>(R.id.map).setOnClickListener(this::onClick)
        view.findViewById<LinearLayout>(R.id.netJSON).setOnClickListener(this::onClick)
        view.findViewById<LinearLayout>(R.id.netXML).setOnClickListener(this::onClick)
        view.findViewById<LinearLayout>(R.id.image).setOnClickListener(this::onClick)
        view.findViewById<LinearLayout>(R.id.capture).setOnClickListener(this::onClick)
    }

    private fun onClick(v: View?) {
        when (v?.id) {
            R.id.map -> {
                getModel<MoreModel>().getPresenter<MorePresenter>()
                    .addAction(ApplicationAction(MorePresenter.OnClickMap))
            }
            R.id.netJSON -> {
                getModel<MoreModel>().getPresenter<MorePresenter>()
                    .addAction(ApplicationAction(MorePresenter.OnClickNetJson))
            }
            R.id.netXML -> {
                getModel<MoreModel>().getPresenter<MorePresenter>()
                    .addAction(ApplicationAction(MorePresenter.OnClickNetXML))
            }
            R.id.image -> {
                getModel<MoreModel>().getPresenter<MorePresenter>()
                    .addAction(ApplicationAction(MorePresenter.OnClickImage))
            }
            R.id.capture -> {
                getModel<MoreModel>().getPresenter<MorePresenter>()
                    .addAction(ApplicationAction(MorePresenter.OnClickCapture))
            }
        }
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun getName(): String {
        return NAME
    }

}

