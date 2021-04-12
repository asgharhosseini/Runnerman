package ir.ah.app.runnerman.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import ir.ah.app.runnerman.data.model.*
import ir.ah.app.runnerman.data.repositories.MainRepository
import kotlinx.coroutines.*

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    val runsSortedByDate = mainRepository.getAllRunsSortedByDate()


    fun insertRun(run:Run)=viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}