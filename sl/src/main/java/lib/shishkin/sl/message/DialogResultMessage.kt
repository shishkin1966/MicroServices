package lib.shishkin.sl.message

import lib.shishkin.sl.action.DialogResultAction
import lib.shishkin.sl.provider.IMessengerSubscriber

class DialogResultMessage : AbsMessage {
    companion object {
        const val SUBJ = "DialogResultMessage"
    }

    private var action: DialogResultAction

    constructor(address: String, action: DialogResultAction) : super(address) {
        this.action = action
    }

    constructor(message: DialogResultMessage, action: DialogResultAction) : super(message) {
        this.action = action
    }

    override fun copy(): IMessage {
        return DialogResultMessage(this, action)
    }

    override fun getSubj(): String {
        return SUBJ
    }

    override fun read(subscriber: IMessengerSubscriber) {
        if (subscriber is IDialogResultListener) {
            subscriber.onDialogResult(action)
        }
    }

}
