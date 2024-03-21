package com.sugarygary.gitconnect.ui.follow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugarygary.gitconnect.R
import com.sugarygary.gitconnect.databinding.FragmentFollowBinding
import com.sugarygary.gitconnect.ui.base.BaseFragment
import com.sugarygary.gitconnect.ui.profile.ProfileFragmentDirections
import com.sugarygary.gitconnect.utils.gone
import com.sugarygary.gitconnect.utils.visible

class FollowFragment : BaseFragment<FragmentFollowBinding>(FragmentFollowBinding::inflate) {
    private val viewModel: FollowViewModel by viewModels()
    private lateinit var followUserAdapter: FollowUserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt(ARG_POSITION)
        val username = arguments?.getString(ARG_USERNAME)
        if (viewModel.follow.value == null) {
            when (position) {
                //FOLLOWER
                1 -> {
                    viewModel.fetchUserFollowers(username ?: "")
                }
                //FOLLOWING
                2 -> {
                    viewModel.fetchUserFollowing(username ?: "")
                }
            }
        }
    }

    private fun onClickItem(imageView: View, username: String) {
        //extras untuk container transform
        val extras = FragmentNavigatorExtras(imageView to imageView.transitionName.also(::println))
        val sharedTransitionName = imageView.transitionName
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentSelf(
                username, sharedTransitionName
            ), navigatorExtras = extras
        )
    }

    override fun setupUI() {
        val position = arguments?.getInt(ARG_POSITION)
        when (position) {
            //FOLLOWER
            1 -> {
                followUserAdapter = FollowUserAdapter("from-follower", ::onClickItem)
                binding.tvEmpty.text = resources.getString(R.string.empty_follower)
            }
            //FOLLOWING
            2 -> {
                followUserAdapter = FollowUserAdapter("from-following", ::onClickItem)
                binding.tvEmpty.text = resources.getString(R.string.empty_following)
            }
        }
        binding.rvFollow.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvFollow.adapter = followUserAdapter
    }

    override fun setupObservers() {
        viewModel.isError.observe(viewLifecycleOwner) { isError ->
            setError(isError)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }
        viewModel.follow.observe(viewLifecycleOwner) { follow ->
            setEmpty(follow.isEmpty())
            followUserAdapter.submitList(follow)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.layoutRV.gone()
            binding.layoutEmpty.gone()
            binding.layoutFollowLoading.visible()
            return
        }
        binding.layoutRV.visible()
        binding.layoutFollowLoading.gone()
        binding.layoutEmpty.gone()
    }

    private fun setError(isError: Boolean) {
        if (isError) {
            binding.layoutRV.gone()
            binding.layoutEmpty.gone()
            binding.layoutError.visible()
            return
        }
        binding.layoutRV.visible()
        binding.layoutError.gone()
        binding.layoutEmpty.gone()
    }

    private fun setEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding.layoutRV.gone()
            binding.layoutError.gone()
            binding.layoutEmpty.visible()
            return
        }
        binding.layoutRV.visible()
        binding.layoutError.gone()
        binding.layoutEmpty.gone()
    }

    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
    }
}