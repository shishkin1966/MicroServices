package lib.shishkin.sl.action.handler

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import lib.shishkin.common.ApplicationUtils
import lib.shishkin.common.KeyboardRunnable
import lib.shishkin.sl.IValidated
import lib.shishkin.sl.R
import lib.shishkin.sl.action.*
import lib.shishkin.sl.ui.MaterialDialogExt


class FragmentActionHandler(private val fragment: Fragment) : BaseActionHandler() {
    override fun onAction(action: IAction): Boolean {
        if (fragment is IValidated && !fragment.isValid()) return false

        if (super.onAction(action)) return true

        if (action is HideProgressBarAction) {
            hideProgressBar()
            return true
        }
        if (action is ShowProgressBarAction) {
            showProgressBar()
            return true
        }
        if (action is ShowKeyboardAction) {
            showKeyboard(action)
            return true
        }
        if (action is HideKeyboardAction) {
            hideKeyboard()
            return true
        }
        if (action is StopAction) {
            if (fragment.activity != null) {
                val activity = fragment.activity as AppCompatActivity
                activity.onBackPressed()
            }
            return true
        }
        if (action is ShowListDialogAction) {
            showListDialog(action)
            return true
        }
        if (action is ShowDialogAction) {
            showDialog(action)
            return true
        }

        return false
    }

    private fun showProgressBar() {
        val view = fragment.view?.findViewById<View>(R.id.progressBar)
        view?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        val view = fragment.view?.findViewById<View>(R.id.progressBar)
        view?.visibility = View.INVISIBLE
    }

    private fun showKeyboard(action: ShowKeyboardAction) {
        val activity = fragment.activity ?: return

        KeyboardRunnable(activity, action.getView()).run()
    }

    private fun hideKeyboard() {
        val activity = fragment.activity ?: return
        if (activity.isFinishing) return

        val imm = ApplicationUtils.getSystemService<InputMethodManager>(
            activity,
            Activity.INPUT_METHOD_SERVICE
        )
        var view = activity.currentFocus
        if (view == null) {
            view = getRootView()
        }
        if (view != null) {
            activity.window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun getRootView(): View? {
        val view = fragment.view?.findViewById<View>(R.id.root)
        return view ?: fragment.view
    }

    private fun showDialog(action: ShowDialogAction) {
        val activity = fragment.activity ?: return
        if (activity.isFinishing) return

        MaterialDialogExt(
            activity,
            action.getListener(),
            action.getId(),
            action.getTitle(),
            action.getMessage(),
            action.getButtonPositive(),
            action.getButtonNegative(),
            action.isCancelable()
        ).show()
    }

    private fun showListDialog(action: ShowListDialogAction) {
        val activity = fragment.activity ?: return
        if (activity.isFinishing) return

        if (action.getList() == null) return

        MaterialDialogExt(
            activity,
            action.getListener(),
            action.getId(),
            action.getTitle(),
            action.getMessage(),
            action.getList()!!,
            action.getSelected(),
            action.isMultiSelect(),
            action.getButtonPositive(),
            action.getButtonNegative(),
            action.isCancelable()
        ).show()
    }
}
