package lib.shishkin.sl.model

import lib.shishkin.sl.IValidated
import lib.shishkin.sl.lifecycle.ILifecycle
import lib.shishkin.sl.ui.IViewGroup


interface IModelView : IValidated, IViewGroup {
    /**
     * Добавить слушателя к ModelView объекту
     *
     * @param stateable stateable объект
     */
    fun addStateObserver(stateable: ILifecycle)

    /**
     * Закрыть ModelView объект
     */
    fun stop()

    /**
     * Получить модель
     *
     * @return модель
     */
    fun <M : IModel> getModel(): M

    /**
     * Установить модель
     *
     * @param model модель
     */
    fun <M : IModel> setModel(model: M)
}
