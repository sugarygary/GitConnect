package com.sugarygary.gitconnect.ui.settings

import android.content.res.Configuration
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sugarygary.gitconnect.data.local.datastore.SettingPreferences
import com.sugarygary.gitconnect.data.local.datastore.dataStore
import com.sugarygary.gitconnect.databinding.FragmentSettingsBinding
import com.sugarygary.gitconnect.ui.base.BaseFragment


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.Companion.Factory(SettingPreferences.getInstance(requireActivity().application.dataStore))
    }

    private fun isDarkMode(): Boolean =
        requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    override fun setupUI() {
        binding.toolbarProfile.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toggleDark.setOnClickListener {
            viewModel.saveThemeSetting(if (binding.toggleDark.isChecked) "dark" else "light")
        }
        binding.toggleSystem.setOnClickListener {
            if (binding.toggleSystem.isChecked) {
                viewModel.saveThemeSetting("default")
            } else {
                if (isDarkMode()) {
                    viewModel.saveThemeSetting("dark")
                } else {
                    viewModel.saveThemeSetting("light")
                }
            }
        }
    }

    override fun setupObservers() {
        viewModel.getThemeSettings().observe(this) { themeMode: String ->
            when (themeMode) {
                "dark" -> {
                    binding.toggleDark.isClickable = true
                    binding.toggleSystem.isChecked = false
                    binding.toggleDark.isChecked = true
                }

                "light" -> {
                    binding.toggleDark.isClickable = true
                    binding.toggleSystem.isChecked = false
                    binding.toggleDark.isChecked = false
                }

                "default" -> {
                    binding.toggleDark.isClickable = false
                    binding.toggleSystem.isChecked = true
                    binding.toggleDark.isChecked = isDarkMode()
                }
            }
        }
    }
}