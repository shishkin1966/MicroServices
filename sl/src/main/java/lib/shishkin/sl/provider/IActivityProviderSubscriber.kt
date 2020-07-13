package lib.shishkin.sl.provider

import lib.shishkin.sl.IProviderSubscriber

interface IActivityProviderSubscriber : IProviderSubscriber {
    fun clearBackStack()
}