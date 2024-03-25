package com.sugarygary.gitconnect.ui.favorite

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugarygary.gitconnect.data.repository.Result
import com.sugarygary.gitconnect.data.repository.model.UserModel
import com.sugarygary.gitconnect.databinding.FragmentFavoriteBinding
import com.sugarygary.gitconnect.ui.base.BaseFragment
import com.sugarygary.gitconnect.ui.base.ViewModelFactory
import com.sugarygary.gitconnect.utils.gone
import com.sugarygary.gitconnect.utils.makeSnackbar
import com.sugarygary.gitconnect.utils.visible

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {

    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var favoriteUserAdapter: FavoriteUserAdapter

    override fun onResume() {
        viewModel.fetchFavoriteUsers(false)
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.favorite.value == null) {
            viewModel.fetchFavoriteUsers()
        }
        //untuk pop transition dengan container transform
        postponeEnterTransition()
        (requireView().parent as ViewGroup).viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
    }

    private fun onClickItem(imageView: View, username: String) {
        //extras untuk container transform
        val extras = FragmentNavigatorExtras(imageView to imageView.transitionName)
        val sharedTransitionName = imageView.transitionName
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToProfileFragment(
                username, sharedTransitionName
            ), navigatorExtras = extras
        )
    }

    private fun onClickFavorite(isFavorite: Boolean, user: UserModel) {
        viewModel.favoriteUser(isFavorite, user)
        viewModel.fetchFavoriteUsers(false)
        val message = if (isFavorite) {
            "Successfully removed ${user.login} from favorite"
        } else {
            "Successfully added ${user.login} to favorite"
        }
        requireActivity().makeSnackbar(message, "UNDO") {
            viewModel.favoriteUser(!isFavorite, user)
            viewModel.fetchFavoriteUsers(true)
        }
    }

    override fun setupUI() {
        favoriteUserAdapter = FavoriteUserAdapter(::onClickItem, ::onClickFavorite)
        binding.rvFavorite.adapter = favoriteUserAdapter
        binding.rvFavorite.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.toolbarFavorite.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchFavoriteUsers()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun setupObservers() {
        viewModel.favorite.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Empty -> {
                    setEmpty()
                }

                is Result.Error -> {
                    setError()
                }

                is Result.Loading -> {
                    setLoading(true)
                }

                is Result.Success -> {
                    setLoading(false)
                    favoriteUserAdapter.submitList(result.data)
                }
            }
        }
    }

    private fun setEmpty() {
        with(binding) {
            layoutError.gone()
            layoutSuccess.gone()
            layoutLoading.gone()
            layoutEmpty.visible()
            return

        }
    }

    private fun setLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                layoutEmpty.gone()
                layoutError.gone()
                layoutSuccess.gone()
                layoutLoading.visible()
                return
            }
            layoutEmpty.gone()
            layoutError.gone()
            layoutLoading.gone()
            layoutSuccess.visible()
        }
    }

    private fun setError() {
        with(binding) {
            layoutEmpty.gone()
            layoutSuccess.gone()
            layoutLoading.gone()
            layoutError.visible()
        }
    }

}