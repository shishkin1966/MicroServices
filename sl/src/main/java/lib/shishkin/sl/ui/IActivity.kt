package lib.shishkin.sl.ui

import lib.shishkin.sl.action.IActionHandler
import lib.shishkin.sl.action.IActionListener
import lib.shishkin.sl.lifecycle.ILifecycle
import lib.shishkin.sl.lifecycle.ILifecycleObservable
import lib.shishkin.sl.provider.IActivityProviderSubscriber

interface IActivity : IActivityProviderSubscriber, ILifecycle, IActionListener, IActionHandler, ILifecycleObservable, IViewGroup, IPermissionListener {
}
