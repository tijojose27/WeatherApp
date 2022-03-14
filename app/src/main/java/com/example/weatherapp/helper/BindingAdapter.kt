package com.example.weatherapp.helper

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @BindingAdapter("android:visibility")
    @JvmStatic
    fun setVisibility(view: View, error: Boolean) {
        var visible = false

        if (error) {
            visible = true
        }


        view.visibility = if (visible) View.VISIBLE else View.GONE
    }
}