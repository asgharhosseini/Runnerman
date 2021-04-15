package ir.ah.app.runnerman.ui.dialog

import android.app.*
import android.os.*
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.*
import ir.ah.app.runnerman.*

class CancelTrackingDialog : DialogFragment() {

    private var yesListener: (() -> Unit)? = null

    fun setYesListener(listener: () -> Unit) {
        yesListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
                .setTitle("Cancel the Run?")
                .setMessage("Are you sure to cancel the current run and delete all its data?")
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton("Yes") { _, _ ->
                    yesListener?.let { yes ->
                        yes()
                    }
                }
                .setNegativeButton("No") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .create()

    }
}