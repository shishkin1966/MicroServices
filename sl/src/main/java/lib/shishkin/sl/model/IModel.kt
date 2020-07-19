package lib.shishkin.sl.model

import lib.shishkin.sl.IValidated
import lib.shishkin.sl.lifecycle.ILifecycle


interface IModel : IValidated {
    /**
     * Получить View объект модели
     *
     * @return View объект модели
     */
    fun <M : IModelView> getView(): M

    /**
     * Добавить слушателя к модели
     *
     * @param stateable stateable объект
     */
    fun addLifecycleObserver(stateable: ILifecycle)

}
