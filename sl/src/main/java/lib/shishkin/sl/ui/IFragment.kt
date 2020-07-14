package lib.shishkin.sl.ui

import lib.shishkin.sl.ISubscriber
import lib.shishkin.sl.action.IActionHandler
import lib.shishkin.sl.action.IActionListener
import lib.shishkin.sl.lifecycle.ILifecycle
import lib.shishkin.sl.lifecycle.ILifecycleObservable


interface IFragment : ISubscriber, ILifecycle, IActionListener, IPermissionListener, IActionHandler, IViewGroup,
    ILifecycleObservable {

    fun stop()
}
