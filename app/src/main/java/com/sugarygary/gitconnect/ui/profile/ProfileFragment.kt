package com.sugarygary.gitconnect.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialContainerTransform
import com.sugarygary.gitconnect.R
import com.sugarygary.gitconnect.data.repository.Result
import com.sugarygary.gitconnect.databinding.FragmentProfileBinding
import com.sugarygary.gitconnect.ui.base.BaseFragment
import com.sugarygary.gitconnect.ui.base.ViewModelFactory
import com.sugarygary.gitconnect.utils.glide
import com.sugarygary.gitconnect.utils.invisible
import com.sugarygary.gitconnect.utils.makeSnackbar
import com.sugarygary.gitconnect.utils.visible

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ganti transition fragment untuk menggunakan MaterialContainerTransform
        sharedElementEnterTransition = MaterialContainerTransform()
        binding.ivProfileAvatar.transitionName = arguments?.getString("shared_transition_name")
        if (viewModel.user.value == null) {
            viewModel.fetchUserProfile(arguments?.getString("username") ?: "")
        } else {
            viewModel.fetchUserProfile(arguments?.getString("username") ?: "", false)
        }
        //untuk pop transition dengan container transform
        postponeEnterTransition()
        (requireView().parent as ViewGroup).viewTreeObserver.addOnPreDrawListener {
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
            viewModel.fetchUserProfile(arguments?.getString("username") ?: "")
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
        viewModel.user.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Empty -> {
                    setLoading(false)
                    setError(true)
                }

                is Result.Error -> {
                    setLoading(false)
                    setError(true)
                }

                is Result.Loading -> {
                    setError(false)
                    setLoading(true)
                }

                is Result.Success -> {
                    setLoading(false)
                    setError(false)
                    binding.ivProfileAvatar.glide(result.data.avatarUrl.toString())
                    binding.tvProfileUsername.text = result.data.login
                    binding.tvProfileName.text = result.data.name
                    binding.tvFollowers.text =
                        resources.getString(R.string.followers_value, result.data.followers)
                    binding.tvFollowing.text =
                        resources.getString(R.string.following_value, result.data.following)
                    binding.btnBrowser.setOnClickListener {
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(result.data.htmlUrl))
                        requireActivity().startActivity(webIntent)
                    }
                    binding.btnProfileFavorite.apply {
                        if (result.data.isFavorite) {
                            text = resources.getString(R.string.unfavorite)
                            icon = ContextCompat.getDrawable(requireContext(), R.drawable.favorite)
                            setOnClickListener {
                                viewModel.favoriteUser(result.data.isFavorite, result.data)
                                viewModel.fetchUserProfile(result.data.login, false)
                                requireActivity().makeSnackbar(
                                    "Succesfully removed ${result.data.login} from favorite", "UNDO"
                                ) {
                                    viewModel.favoriteUser(!result.data.isFavorite, result.data)
                                    viewModel.fetchUserProfile(result.data.login, false)
                                }
                            }
                        } else {
                            text = resources.getString(
                                R.string.favorite
                            )
                            icon =
                                ContextCompat.getDrawable(requireContext(), R.drawable.not_favorite)
                            setOnClickListener {
                                viewModel.favoriteUser(result.data.isFavorite, result.data)
                                viewModel.fetchUserProfile(result.data.login, false)
                                requireActivity().makeSnackbar(
                                    "Succesfully added ${result.data.login} to favorite", "UNDO"
                                ) {
                                    viewModel.favoriteUser(!result.data.isFavorite, result.data)
                                    viewModel.fetchUserProfile(result.data.login, false)
                                }
                            }
                        }
                    }

                }
            }

        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers, R.string.following
        )
    }

}