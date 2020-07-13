package lib.shishkin.sl

import lib.shishkin.sl.lifecycle.ILifecycle
import lib.shishkin.sl.lifecycle.Lifecycle

abstract class AbsSmallUnion<T : IProviderSubscriber> : AbsProvider(), ISmallUnion<T> {
    private val secretary = createSecretary()

    override fun createSecretary(): ISecretary<T> {
        return RefSecretary()
    }

    override fun register(subscriber: T): Boolean {
        if (!subscriber.isValid()) {
            return false
        }

        val cnt = secretary.size()

        secretary.put(subscriber.getName(), subscriber)

        if (cnt == 0 && secretary.size() == 1) {
            onRegisterFirstSubscriber()
        }
        onAddSubscriber(subscriber)
        return true
    }

    override fun unregister(subscriber: T) {
        val cnt = secretary.size()
        if (secretary.containsKey(subscriber.getName()) && subscriber == secretary.get(subscriber.getName())) {
            secretary.remove(subscriber.getName())
        }

        if (cnt == 1 && secretary.size() == 0) {
            onUnRegisterLastSubscriber()
        }
    }

    override fun unregister(name: String) {
        if (hasSubscriber(name)) {
            val subscriber = getSubscriber(name)
            if (subscriber != null) {
                unregister(subscriber)
            }
        }
    }

    override fun getSubscribers(): List<T> {
        return secretary.values()
    }

    override fun getValidatedSubscribers(): List<T> {
        val subscribers = ArrayList<T>()
        for (subscriber in getSubscribers()) {
            if (subscriber.isValid()) {
                subscribers.add(subscriber)
            }
        }
        return subscribers
    }

    override fun getReadySubscribers(): List<T> {
        val subscribers = ArrayList<T>()
        for (subscriber in getSubscribers()) {
            if (subscriber.isValid() && subscriber is ILifecycle) {
                val state = (subscriber as ILifecycle).getState()
                if (state == Lifecycle.VIEW_READY) {
                    subscribers.add(subscriber)
                }
            }
        }
        return subscribers
    }

    override fun getSubscriber(name: String): T? {
        return if (!secretary.containsKey(name)) {
            null
        } else secretary.get(name)
    }

    override fun hasSubscribers(): Boolean {
        return !secretary.isEmpty()
    }

    override fun hasSubscriber(name: String): Boolean {
        return secretary.containsKey(name)
    }

    override fun stop() {
        for (subscriber in getSubscribers()) {
            unregister(subscriber)
            subscriber.onStopProvider(this)
        }
        secretary.clear()
    }

    override fun onRegisterFirstSubscriber() {
    }

    override fun onUnRegisterLastSubscriber() {
    }

    override fun onAddSubscriber(subscriber: T) {
    }

    override fun contains(subscriber: T): Boolean {
        return secretary.containsKey(subscriber.getName())
    }
}
