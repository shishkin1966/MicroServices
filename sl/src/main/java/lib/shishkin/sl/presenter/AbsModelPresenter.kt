package lib.shishkin.sl.model

import lib.shishkin.sl.presenter.AbsPresenter
import lib.shishkin.sl.presenter.IModelPresenter


abstract class AbsModelPresenter() : AbsPresenter(), IModelPresenter {
    private lateinit var model: IModel

    constructor(model: IModel) : this() {
        this.model = model
    }

    override fun <M : IModel> getModel(): M {
        return model as M
    }

    override fun <C : IModelView> getView(): C {
        return model.getView()
    }
}
