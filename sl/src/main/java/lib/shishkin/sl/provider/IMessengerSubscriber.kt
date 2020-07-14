package lib.shishkin.sl.provider

import lib.shishkin.sl.IProviderSubscriber
import lib.shishkin.sl.lifecycle.ILifecycle
import lib.shishkin.sl.message.IMessage


interface IMessengerSubscriber : IProviderSubscriber, ILifecycle {
    fun read(message: IMessage)
}
