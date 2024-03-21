package com.sugarygary.gitconnect.ui.settings

import androidx.navigation.fragment.findNavController
import com.sugarygary.gitconnect.databinding.FragmentSettingsBinding
import com.sugarygary.gitconnect.ui.base.BaseFragment


class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override fun setupUI() {
        binding.toolbarProfile.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setupObservers() {}

}