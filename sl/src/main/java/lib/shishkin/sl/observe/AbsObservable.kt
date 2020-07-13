package lib.shishkin.sl.observe

import lib.shishkin.sl.Secretary
import lib.shishkin.sl.provider.IObservableSubscriber

abstract class AbsObservable : IObservable {

    private val secretary = Secretary<IObservableSubscriber>()

    override fun addObserver(subscriber: IObservableSubscriber) {
        secretary.put(subscriber.getName(), subscriber)

        if (secretary.size() == 1) {
            register()
        }
    }

    override fun removeObserver(subscriber: IObservableSubscriber) {
        if (secretary.containsKey(subscriber.getName())) {
            if (subscriber == secretary.get(subscriber.getName())) {
                secretary.remove(subscriber.getName())
            }

            if (secretary.isEmpty()) {
                unregister()
            }
        }
    }

    override fun onChange(obj: Any) {
        for (subscriber in secretary.values()) {
            if (subscriber.isValid()) {
                subscriber.onChange(getName(), obj)
            }
        }
    }

    override fun getObservers(): List<IObservableSubscriber> {
        return secretary.values()
    }

    override fun getObserver(name: String): IObservableSubscriber? {
        return secretary.get(name)
    }

    override fun register() {
    }

    override fun unregister() {
    }

    override fun stop() {
    }
}
