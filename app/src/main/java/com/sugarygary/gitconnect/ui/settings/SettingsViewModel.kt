package com.sugarygary.gitconnect.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sugarygary.gitconnect.data.datastore.SettingPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<String> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(themeMode: String) {
        viewModelScope.launch {
            pref.saveThemeSetting(themeMode)
        }
    }

    companion object {
        class Factory(private val pref: SettingPreferences) :
            ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    return SettingsViewModel(pref) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }
}