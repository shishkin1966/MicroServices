package lib.shishkin.sl.observe

import lib.shishkin.sl.IProvider
import lib.shishkin.sl.lifecycle.Lifecycle
import lib.shishkin.sl.provider.IObservableSubscriber
import lib.shishkin.sl.provider.ObservableUnion

abstract class AbsObservableSubscriber : IObservableSubscriber {
    override fun getState(): Int {
        return Lifecycle.VIEW_READY
    }

    override fun setState(state: Int) {
    }

    override fun getProviderSubscription(): List<String> {
        return listOf(ObservableUnion.NAME)
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun onStopProvider(provider: IProvider) {
    }

    override  fun stop() {
    }

}
