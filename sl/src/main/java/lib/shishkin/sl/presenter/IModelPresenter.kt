package lib.shishkin.sl.presenter

import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.model.IModelView


interface IModelPresenter : IPresenter {
    /**
     * Получить модель презентера
     *
     * @return the model
     */
    fun <M : IModel> getModel(): M

    /**
     * Получить View модели
     *
     * @return the view model
     */
    fun <V : IModelView> getView(): V

}
