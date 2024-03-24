package com.sugarygary.gitconnect.ui.follow

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugarygary.gitconnect.R
import com.sugarygary.gitconnect.data.repository.Result
import com.sugarygary.gitconnect.data.repository.model.UserModel
import com.sugarygary.gitconnect.databinding.FragmentFollowBinding
import com.sugarygary.gitconnect.ui.base.BaseFragment
import com.sugarygary.gitconnect.ui.base.ViewModelFactory
import com.sugarygary.gitconnect.ui.profile.ProfileFragmentDirections
import com.sugarygary.gitconnect.utils.gone
import com.sugarygary.gitconnect.utils.snackbarAction
import com.sugarygary.gitconnect.utils.visible

class FollowFragment : BaseFragment<FragmentFollowBinding>(FragmentFollowBinding::inflate) {
    private val viewModel: FollowViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var followUserAdapter: FollowUserAdapter

    override fun onResume() {
        val position = arguments?.getInt(ARG_POSITION)
        val username = arguments?.getString(ARG_USERNAME)
        when (position) {
            //FOLLOWER
            1 -> {
                viewModel.fetchUserFollowers(username ?: "", false)
            }
            //FOLLOWING
            2 -> {
                viewModel.fetchUserFollowing(username ?: "", false)
            }
        }
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt(ARG_POSITION)
        val username = arguments?.getString(ARG_USERNAME)
        when (position) {
            //FOLLOWER
            1 -> {
                if (viewModel.follow.value == null) {
                    viewModel.fetchUserFollowers(username ?: "")
                } else {
                    viewModel.fetchUserFollowers(username ?: "", false)
                }
            }
            //FOLLOWING
            2 -> {
                if (viewModel.follow.value == null) {
                    viewModel.fetchUserFollowing(username ?: "")
                } else {
                    viewModel.fetchUserFollowing(username ?: "", false)
                }
            }
        }
    }

    private fun onClickItem(imageView: View, username: String) {
        //extras untuk container transform
        val extras = FragmentNavigatorExtras(imageView to imageView.transitionName)
        val sharedTransitionName = imageView.transitionName
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentSelf(
                username, sharedTransitionName
            ), navigatorExtras = extras
        )
    }

    private fun onClickFavorite(isFavorite: Boolean, user: UserModel) {
        val username = arguments?.getString(ARG_USERNAME)
        val position = arguments?.getInt(ARG_POSITION)
        viewModel.favoriteUser(isFavorite, user)
        when (position) {
            //FOLLOWER
            1 -> {
                viewModel.fetchUserFollowers(username ?: "", false)
            }
            //FOLLOWING
            2 -> {
                viewModel.fetchUserFollowing(username ?: "", false)
            }
        }
        val message = if (isFavorite) {
            "Successfully removed ${user.login} from favorite"
        } else {
            "Successfully added ${user.login} to favorite"
        }
        requireActivity().snackbarAction(message, "UNDO") {
            viewModel.favoriteUser(!isFavorite, user)
            when (position) {
                //FOLLOWER
                1 -> {
                    viewModel.fetchUserFollowers(username ?: "", false)
                }
                //FOLLOWING
                2 -> {
                    viewModel.fetchUserFollowing(username ?: "", false)
                }
            }
        }
    }

    override fun setupUI() {
        val position = arguments?.getInt(ARG_POSITION)
        when (position) {
            //FOLLOWER
            1 -> {
                followUserAdapter =
                    FollowUserAdapter("from-follower", ::onClickItem, ::onClickFavorite)
                binding.tvEmpty.text = resources.getString(R.string.empty_follower)
            }
            //FOLLOWING
            2 -> {
                followUserAdapter =
                    FollowUserAdapter("from-following", ::onClickItem, ::onClickFavorite)
                binding.tvEmpty.text = resources.getString(R.string.empty_following)
            }
        }
        binding.rvFollow.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvFollow.adapter = followUserAdapter
    }

    override fun setupObservers() {
        viewModel.follow.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Empty -> {
                    setLoading(false)
                    setError(false)
                    setEmpty(true)
                }

                is Result.Error -> {
                    setLoading(false)
                    setEmpty(false)
                    setError(true)
                }

                is Result.Loading -> {
                    setEmpty(false)
                    setError(false)
                    setLoading(true)
                }

                is Result.Success -> {
                    setLoading(false)
                    setEmpty(false)
                    setError(false)
                    followUserAdapter.submitList(result.data)
                }
            }

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