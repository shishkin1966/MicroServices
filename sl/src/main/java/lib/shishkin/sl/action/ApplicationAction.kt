package lib.shishkin.sl.action

import lib.shishkin.sl.INamed


open class ApplicationAction(private val name: String) : IAction, INamed {

    override fun getName(): String {
        return name
    }
}
