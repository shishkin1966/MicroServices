package lib.shishkin.sl.presenter

import lib.shishkin.sl.IProvider
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.lifecycle.Lifecycle
import lib.shishkin.sl.lifecycle.LifecycleObserver
import lib.shishkin.sl.message.IMessage
import lib.shishkin.sl.provider.ApplicationProvider
import lib.shishkin.sl.provider.IMessengerUnion
import lib.shishkin.sl.provider.MessengerUnion
import lib.shishkin.sl.provider.PresenterUnion
import java.util.*


abstract class AbsPresenter() : IPresenter {
    private val lifecycle = LifecycleObserver(this)
    private val actions = LinkedList<IAction>()

    override fun getState(): Int {
        return lifecycle.getState()
    }

    override fun setState(state: Int) {
        lifecycle.setState(state)
    }

    override fun onCreateView() {}

    override fun onReadyView() {
        ApplicationProvider.serviceLocator?.registerSubscriber(this)

        doActions()

        ApplicationProvider.serviceLocator?.get<IMessengerUnion>(
            MessengerUnion.NAME
        )
            ?.readMessages(this)

        onStart()
    }

    open fun onStart() {
    }

    override fun onDestroyView() {
        ApplicationProvider.serviceLocator?.unregisterSubscriber(this)
    }

    override fun isValid(): Boolean {
        return lifecycle.getState() != Lifecycle.VIEW_DESTROY
    }

    override fun getProviderSubscription(): List<String> {
        return listOf(PresenterUnion.NAME, MessengerUnion.NAME)
    }

    override fun onStopProvider(provider: IProvider) {
    }

    override fun read(message: IMessage) {}

    override fun addAction(action: IAction) {
        when (getState()) {
            Lifecycle.VIEW_DESTROY -> return

            Lifecycle.VIEW_CREATE, Lifecycle.VIEW_NOT_READY -> {
                actions.add(action)
                return
            }

            else -> {
                actions.add(action)
                doActions()
            }
        }
    }

    private fun doActions() {
        val deleted = ArrayList<IAction>()
        for (i in actions.indices) {
            if (getState() != Lifecycle.VIEW_NOT_READY) {
                break
            }
            onAction(actions[i])
            deleted.add(actions[i])
        }
        for (action in deleted) {
            actions.remove(action)
        }
    }

    override fun onAction(action: IAction): Boolean {
        return true
    }

    override fun stop() {
    }

}
