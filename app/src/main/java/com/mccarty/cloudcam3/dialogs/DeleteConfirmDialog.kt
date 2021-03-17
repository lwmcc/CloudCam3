package com.mccarty.cloudcam3.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mccarty.cloudcam3.R
import com.mccarty.cloudcam3.ui.home.HomeViewModel

class DeleteConfirmDialog(private val homeViewModel: HomeViewModel): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.message_confirm_delete))
                    .setPositiveButton(getString(R.string.dialog_positive)) { _,_ -> {}
                        homeViewModel.setDeleteImage()
                        homeViewModel.notifyAdapterChanged(true)
                    }
                    .setNegativeButton(getString(R.string.dialog_negative)) { _,_ -> }
                    .create()
}