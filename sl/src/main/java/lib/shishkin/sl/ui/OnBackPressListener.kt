package lib.shishkin.sl.ui

interface OnBackPressListener {
    fun onBackPressed(): Boolean

    fun isTop(): Boolean
}