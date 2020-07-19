package lib.shishkin.sl.model

import lib.shishkin.sl.lifecycle.ILifecycle
import lib.shishkin.sl.lifecycle.ILifecycleListener
import lib.shishkin.sl.lifecycle.LifecycleObserver

abstract class AbsModel(private val modelView: IModelView) : IModel, ILifecycleListener {

    private val lifecycle = LifecycleObserver(this)

    init {
        modelView.addLifecycleObserver(this)
    }

    override fun <M : IModelView> getView(): M {
        return modelView as M
    }

    override fun isValid(): Boolean {
        return modelView.isValid()
    }

    override fun addLifecycleObserver(stateable: ILifecycle) {
        modelView.addLifecycleObserver(stateable)
    }

    override fun getState(): Int {
        return lifecycle.getState()
    }

    override fun setState(state: Int) {
        lifecycle.setState(state)
    }

    override fun onCreateView() {}

    override fun onReadyView() {}

    override fun onDestroyView() {}

}
