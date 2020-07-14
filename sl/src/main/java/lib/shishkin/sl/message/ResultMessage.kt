package lib.shishkin.sl.message

import lib.shishkin.sl.data.ExtResult
import lib.shishkin.sl.provider.IMessengerSubscriber
import lib.shishkin.sl.request.IResponseListener

class ResultMessage : AbsMessage {
    private lateinit var result: ExtResult

    private constructor(address: String) : super(address)

    constructor(address: String, result: ExtResult) : this(address) {
        this.result = result
    }

    private constructor(message: ResultMessage) : super(message)

    constructor(message: ResultMessage, result: ExtResult) : this(message) {
        this.result = result
    }

    override fun read(subscriber: IMessengerSubscriber) {
        if (subscriber is IResponseListener) {
            subscriber.response(result)
        }
    }

    override fun copy(): IMessage {
        return ResultMessage(this, result)
    }

    override fun isCheckDublicate(): Boolean {
        return true
    }

}
