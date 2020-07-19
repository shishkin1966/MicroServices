package lib.shishkin.microservices.provider

import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.db.Db
import lib.shishkin.sl.provider.AbsDbProvider
import lib.shishkin.sl.request.IRequest

class DbProvider : AbsDbProvider() {
    companion object {
        const val NAME = "DbProvider"
    }

    override fun request(request: IRequest) {
        if (!isValid()) return

        ApplicationSingleton.instance.executeDb(request)
    }

    override fun onRegister() {
        super.getDb(
            Db::class.java,
            Db.NAME
        )
    }

    override fun getName(): String {
        return NAME
    }

}
