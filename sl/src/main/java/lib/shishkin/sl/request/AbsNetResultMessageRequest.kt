package lib.shishkin.sl.request

import io.reactivex.Single
import lib.shishkin.sl.data.ExtError
import lib.shishkin.sl.data.ExtResult
import lib.shishkin.sl.message.ResultMessage
import lib.shishkin.sl.provider.ApplicationProvider
import lib.shishkin.sl.provider.IMessengerUnion
import lib.shishkin.sl.provider.MessengerUnion

abstract class AbsNetResultMessageRequest(owner: String) : AbsResultMessageRequest(owner) {

    override fun run() {
        if (!isValid()) return
        val union = ApplicationProvider.serviceLocator?.get<IMessengerUnion>(
            MessengerUnion.NAME
        )?: return

        val single = getData() as Single<*>
        single
            .map { t: Any -> ExtResult(t) }
            .subscribe(
                { result: ExtResult ->
                    if (isValid() && result.getData() != null) {
                        union.addNotMandatoryMessage(
                            ResultMessage(
                                getOwner(),
                                result.setName(getName())
                            )
                                .setSubj(getName())
                                .setCopyTo(getCopyTo())
                        )
                    }
                }, { throwable: Throwable ->
                    if (isValid()) {
                        val result = ExtResult().setError(
                            ExtError().addError(
                                getName(),
                                throwable.getLocalizedMessage()
                            )
                        ).setName(getName())
                        union.addNotMandatoryMessage(
                            ResultMessage(getOwner(), result)
                                .setSubj(getName())
                                .setCopyTo(getCopyTo())
                        )
                    }
                }
            )
    }

}
