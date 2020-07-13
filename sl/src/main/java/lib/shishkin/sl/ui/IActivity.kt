package lib.shishkin.sl.ui

import lib.shishkin.sl.IProviderSubscriber
import lib.shishkin.sl.action.IActionHandler
import lib.shishkin.sl.action.IActionListener
import lib.shishkin.sl.lifecycle.ILifecycle
import lib.shishkin.sl.lifecycle.ILifecycleObservable

interface IActivity : IProviderSubscriber, ILifecycle, IActionListener, IActionHandler, ILifecycleObservable, IViewGroup, IPermissionListener {
}
