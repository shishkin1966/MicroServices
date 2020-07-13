package lib.shishkin.sl.provider

import lib.shishkin.sl.IProviderSubscriber
import lib.shishkin.sl.IValidated
import lib.shishkin.sl.lifecycle.ILifecycle

interface IObservableSubscriber : ILifecycle, IProviderSubscriber, IValidated {
    /**
     * Получить список имен слушаемых объектов
     *
     * @return список имен слушаемых объектов
     */
    fun getObservable(): List<String>

    /**
     * Событие - объект изменен
     *
     * @param name имя слушаемого объекта
     * @param obj значение слушаемого объекта
     */
    fun onChange(name: String, obj: Any)

}
