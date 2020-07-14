package lib.shishkin.sl.provider

import lib.shishkin.sl.AbsSmallUnion
import lib.shishkin.sl.IProvider
import lib.shishkin.sl.presenter.IPresenter

class PresenterUnion : AbsSmallUnion<IPresenter>(), IPresenterUnion {
    companion object {
        const val NAME = "PresenterUnion"
    }

    override fun register(subscriber: IPresenter): Boolean {
        return if (subscriber.isValid()) {
            if (subscriber.isRegister()) {
                super.register(subscriber)
            } else true
        } else false
    }

    override fun getName(): String {
        return NAME
    }

    override fun <C : IPresenter> getPresenter(name: String): C? {
        return getSubscriber(name) as C?
    }

    override operator fun compareTo(other: IProvider): Int {
        return if (other is IPresenterUnion) 0 else 1
    }
}
