package ir.ah.app.runnerman.ui.fragment

import android.content.*
import android.os.*
import android.view.*
import androidx.fragment.app.*
import androidx.navigation.*
import androidx.navigation.fragment.*
import com.google.android.material.snackbar.*
import dagger.hilt.android.*
import ir.ah.app.runnerman.R
import ir.ah.app.runnerman.other.Constants.KEY_FIRST_TIME_TOGGLE
import ir.ah.app.runnerman.other.Constants.KEY_NAME
import ir.ah.app.runnerman.other.Constants.KEY_WEIGHT
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.*

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isFirstAppOpen) {
            var navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.setupFragment, true)
                    .build()
            findNavController().navigate(
                    R.id.action_setupFragment_to_runFragment,
                    savedInstanceState,
                    navOptions)
        }


        tvContinue.setOnClickListener {
            val success=writePersonalDataToSharedPref()
            if(success) {
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = etName.text.toString()
        val weight = etWeight.text.toString()
        if(name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPreferences.edit()
                .putString(KEY_NAME,name)
                .putFloat(KEY_WEIGHT,weight.toFloat())
                .putBoolean(KEY_FIRST_TIME_TOGGLE,false)
                .apply()
        val toolbarText = "Let's go, $name!"
        requireActivity().tvToolbarTitle.text = toolbarText
        return true

    }
}