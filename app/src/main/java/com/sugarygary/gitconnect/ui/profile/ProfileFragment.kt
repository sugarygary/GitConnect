package com.sugarygary.gitconnect.ui.profile

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialContainerTransform
import com.sugarygary.gitconnect.R
import com.sugarygary.gitconnect.databinding.FragmentProfileBinding
import com.sugarygary.gitconnect.ui.base.BaseFragment
import com.sugarygary.gitconnect.utils.glide
import com.sugarygary.gitconnect.utils.invisible
import com.sugarygary.gitconnect.utils.visible

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ganti transition fragment untuk menggunakan MaterialContainerTransform
        sharedElementEnterTransition = MaterialContainerTransform()
        binding.ivProfileAvatar.transitionName = arguments?.getString("shared_transition_name")
        if (viewModel.user.value == null) {
            viewModel.fetchUserData(arguments?.getString("username") ?: "")
        }
        //untuk pop transition dengan container transform
        postponeEnterTransition()
        (requireView().parent as ViewGroup).viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

    }

    override fun setupUI() {
        val sectionsPagerAdapter =
            SectionsPagerAdapter(requireActivity(), arguments?.getString("username") ?: "")
        binding.vpFollow.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.TabFollow, binding.vpFollow) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        binding.toolbarProfile.setNavigationOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.btnReload.setOnClickListener {
            viewModel.fetchUserData(arguments?.getString("username") ?: "")
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.dataLayout.invisible()
            binding.loadingDataLayout.visible()
            return
        }
        binding.dataLayout.visible()
        binding.loadingDataLayout.invisible()
    }

    private fun setError(isError: Boolean) {
        if (isError) {
            binding.layoutProfileError.visible()
            return
        }
        binding.layoutProfileError.invisible()
    }

    override fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) {
            binding.ivProfileAvatar.glide(it.avatarUrl.toString())
            binding.tvProfileUsername.text = it.login
            binding.tvProfileName.text = it.name
            binding.tvFollowers.text = resources.getString(R.string.followers_value, it.followers)
            binding.tvFollowing.text = resources.getString(R.string.following_value, it.following)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }
        viewModel.isError.observe(viewLifecycleOwner) { isError ->
            setError(isError)
        }
    }
}