package lib.shishkin.sl.message

import lib.shishkin.sl.IValidated
import lib.shishkin.sl.action.DialogResultAction

interface IDialogResultListener : IValidated {
    fun onDialogResult(action: DialogResultAction)
}
