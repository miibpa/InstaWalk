package org.mibanez.instawalk.ui

import android.view.View
// Extensions to change view visibility with less verbose
fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
