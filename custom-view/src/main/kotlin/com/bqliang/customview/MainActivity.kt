package com.bqliang.customview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var containerId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(containerId, HomeFragment())
                .commit()
        }
    }

    private fun getContentView(): View = FrameLayout(this).apply {
        id = View.generateViewId().also { containerId = it }
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
