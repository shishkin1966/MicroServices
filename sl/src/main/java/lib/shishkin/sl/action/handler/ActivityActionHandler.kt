package lib.shishkin.sl.action.handler

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.common.BaseSnackbar
import lib.shishkin.common.KeyboardRunnable
import lib.shishkin.sl.IValidated
import lib.shishkin.sl.R
import lib.shishkin.sl.action.*


class ActivityActionHandler(private val activity: AppCompatActivity) : BaseActionHandler() {
    private var snackbar: Snackbar? = null

    override fun onAction(action: IAction): Boolean {
        if (activity is IValidated) {
            if (!activity.isValid()) return false
        }

        if (super.onAction(action)) return true

        if (action is ShowKeyboardAction) {
            showKeyboard(action)
            return true
        }
        if (action is HideKeyboardAction) {
            hideKeyboard()
            return true
        }
        if (action is ShowSnackbarAction) {
            showSnackbar(action)
            return true
        }
        if (action is HideSnackbarAction) {
            hideSnackbar()
            return true
        }
        if (action is StartActivityAction) {
            if (action.getIntent().resolveActivity(activity.packageManager) != null) {
                activity.startActivity((action).getIntent())
            }
            return true
        }
        if (action is StartChooseActivityAction) {
            if (action.getIntent().resolveActivity(activity.packageManager) != null) {
                activity.startActivity(
                    Intent.createChooser(
                        action.getIntent(),
                        action.getTitle()
                    )
                )
            }
            return true
        }
        if (action is StartActivityForResultAction) {
            if (action.getIntent().resolveActivity(activity.packageManager) != null) {
                activity.startActivityForResult(
                    action.getIntent(),
                    action.getRequestCode()
                )
            }
            return true
        }
        if (action is PermissionAction) {
            if (action.getListener().isNullOrEmpty()) {
                grantPermission(action.getPermission())
            } else {
                grantPermission(
                    action.getPermission(),
                    action.getListener(),
                    action.getHelpMessage()
                )
            }
            return true
        }
        return false
    }

    private fun showKeyboard(action: ShowKeyboardAction) {
        KeyboardRunnable(activity, action.getView()).run()
    }

    private fun hideKeyboard() {
        if (activity.isFinishing) return

        val imm = ApplicationUtils.getSystemService<InputMethodManager>(
            activity,
            Activity.INPUT_METHOD_SERVICE
        )
        var view = activity.currentFocus
        if (view == null) {
            view = getRootView()
        }
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getRootView(): View {
        return if (activity.findViewById<View>(R.id.root) != null) {
            activity.findViewById(R.id.root)
        } else (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    }

    private fun showSnackbar(action: ShowSnackbarAction) {
        val view: View = getRootView()
        val actionName = action.getAction()
        if (actionName.isNullOrEmpty()) {
            snackbar = BaseSnackbar().make(
                view, action.getMessage(), action
                    .getDuration(), action.getType()
            )
            snackbar?.show()
        } else {
            snackbar = BaseSnackbar().make(
                view,
                action.getMessage(),
                action.getDuration(),
                action.getType()
            )
                .setAction(actionName, this::onSnackbarClick)
            snackbar?.show()
        }
    }

    private fun onSnackbarClick(view: View) {
        var action: String? = null
        if (view is AppCompatButton) {
            action = view.text.toString()
        } else if (view is Button) {
            action = view.text.toString()
        }
        if (activity is IActionListener && !action.isNullOrBlank()) {
            activity.addAction(SnackBarAction(action))
        }
    }

    private fun hideSnackbar() {
        snackbar?.dismiss()
    }

    private fun grantPermission(permission: String) {
        if (ApplicationUtils.hasMarshmallow()) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                activity.requestPermissions(
                    arrayOf(permission),
                    ApplicationUtils.REQUEST_PERMISSIONS
                )
            }
        }
    }

    private fun grantPermission(permission: String, listener: String?, helpMessage: String?) {
        if (ApplicationUtils.hasMarshmallow()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                if (activity is IActionListener) {
                    activity.addAction(
                        ShowDialogAction(
                            R.id.dialog_request_permissions,
                            listener!!,
                            null,
                            helpMessage!!
                        ).setPositiveButton(R.string.setting).setNegativeButton(R.string.cancel)
                            .setCancelable(
                                false
                            )
                    )
                }
            } else {
                activity.requestPermissions(
                    arrayOf(permission),
                    ApplicationUtils.REQUEST_PERMISSIONS
                )
            }
        }
    }

}
