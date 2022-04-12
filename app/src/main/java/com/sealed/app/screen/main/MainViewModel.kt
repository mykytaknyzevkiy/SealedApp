package com.sealed.app.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sealed.repository.AppRepository
import com.sealed.repository.model.AppModel
import com.sealed.repository.unit.StateImp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : ViewModel() {

    private val appRepository = AppRepository()

    val favouriteApps = MutableStateFlow<StateImp<List<AppModel>>>(
        StateImp.Loading()
    )

    val apps = MutableStateFlow<StateImp<List<AppModel>>>(
        StateImp.Loading()
    )

    fun load() {
        appRepository.apps
            .onEach {
                favouriteApps.value = StateImp.Data(it.filter { a -> a.isBookmark })

                apps.value = StateImp.Data(it.filter { a -> !a.isBookmark })
            }
            .launchIn(viewModelScope)
    }

}