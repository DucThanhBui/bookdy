package com.example.bookdy.reader

import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.bookdy.databinding.ActivityReaderBinding
import com.example.bookdy.utils.clearPadding
import com.example.bookdy.utils.padSystemUi
import com.example.bookdy.utils.showSystemUi

/**
 * Adds fullscreen support to the ReaderActivity
 */
class FullscreenReaderActivityDelegate(
    private val activity: AppCompatActivity,
    private val readerFragment: VisualReaderFragment,
    private val binding: ActivityReaderBinding
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        // Without this, activity_reader_container receives the insets only once,
        // although we need a call every time the reader is hidden
        activity.window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val newInsets = view.onApplyWindowInsets(insets)
            binding.root.dispatchApplyWindowInsets(newInsets)
        }

        binding.activityContainer.setOnApplyWindowInsetsListener { container, insets ->
            updateSystemUiPadding(container, insets)
            insets
        }

        activity.supportFragmentManager.addOnBackStackChangedListener {
            updateSystemUiVisibility()
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        updateSystemUiVisibility()
    }

    private fun updateSystemUiVisibility() {
        if (readerFragment.isHidden) {
            activity.showSystemUi()
        } else {
            readerFragment.updateSystemUiVisibility()
        }

        // Seems to be required to adjust padding when transitioning from the outlines to the screen reader
        binding.activityContainer.requestApplyInsets()
    }

    private fun updateSystemUiPadding(container: View, insets: WindowInsets) {
        if (readerFragment.isHidden) {
            container.padSystemUi(insets, activity)
        } else {
            container.clearPadding()
        }
    }
}