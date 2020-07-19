package lib.shishkin.sl.request

import lib.shishkin.sl.data.ExtError
import lib.shishkin.sl.data.ExtResult
import lib.shishkin.sl.message.ResultMessage
import lib.shishkin.sl.provider.ApplicationProvider
import lib.shishkin.sl.provider.IMessengerUnion
import lib.shishkin.sl.provider.MessengerUnion
import java.util.*


abstract class AbsResultMessageRequest() : AbsRequest(),
    IResultMessageRequest {

    private lateinit var owner: String
    private var copyTo: List<String> = ArrayList()

    constructor(owner: String) : this() {
        this.owner = owner
    }

    override fun isValid(): Boolean {
        return !isCancelled()
    }

    override fun getOwner(): String {
        return owner
    }

    override fun getCopyTo(): List<String> {
        return copyTo
    }

    override fun setCopyTo(copyTo: List<String>) {
        this.copyTo = copyTo
    }

    override fun run() {
        if (isValid()) {
            val result: ExtResult = try {
                ExtResult().setName(getName()).setData(getData())
            } catch (e: Exception) {
                ExtResult().setName(getName()).setError(
                    ExtError().addError(
                        getName(),
                        e.localizedMessage
                    )
                )
            }
            val union =
                ApplicationProvider.serviceLocator?.get<IMessengerUnion>(
                    MessengerUnion.NAME
                )
            union?.addNotMandatoryMessage(
                ResultMessage(
                    getOwner(),
                    result
                )
                    .setSubj(getName())
                    .setCopyTo(getCopyTo())
            )
        }
    }

    abstract fun getData(): Any?

}
