package com.example.bookdy.reader.preferences

import android.app.Dialog
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.bookdy.reader.ReaderViewModel
import com.example.bookdy.utils.compose.ComposeBottomSheetDialogFragment

abstract class UserPreferencesBottomSheetDialogFragment(
    private val title: String
) : ComposeBottomSheetDialogFragment(
    isScrollable = true
) {
    abstract val preferencesModel: UserPreferencesViewModel<*, *>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            // Reduce the dim to see the impact of the settings on the page.
            window?.setDimAmount(0.1f)

            behavior.apply {
                peekHeight = 1000
                maxHeight = 1000
            }
        }

    @Composable
    override fun Content() {
        UserPreferences(preferencesModel, title)
    }
}

class MainPreferencesBottomSheetDialogFragment : UserPreferencesBottomSheetDialogFragment(
    "User Settings"
) {

    private val viewModel: ReaderViewModel by activityViewModels()

    override val preferencesModel: UserPreferencesViewModel<*, *> by lazy {
        checkNotNull(viewModel.settings)
    }
}
