package ir.ah.app.runnerman.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.ah.app.runnerman.R
import ir.ah.app.runnerman.ui.viewmodel.MainViewModel

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run) {

    private val viewModel: MainViewModel by viewModels()
}