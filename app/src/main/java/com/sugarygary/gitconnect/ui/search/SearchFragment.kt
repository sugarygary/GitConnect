package com.sugarygary.gitconnect.ui.search

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugarygary.gitconnect.data.repository.Result
import com.sugarygary.gitconnect.data.repository.model.UserModel
import com.sugarygary.gitconnect.databinding.FragmentSearchBinding
import com.sugarygary.gitconnect.ui.base.BaseFragment
import com.sugarygary.gitconnect.ui.base.ViewModelFactory
import com.sugarygary.gitconnect.utils.gone
import com.sugarygary.gitconnect.utils.makeSnackbar
import com.sugarygary.gitconnect.utils.visible


class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private val viewModel: SearchViewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var userAdapter: SearchUserAdapter
    override fun onResume() {
        viewModel.searchUsers(binding.svUser.query.toString(), false)
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.listUsers.value == null) {
            viewModel.searchUsers("")
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
            SearchFragmentDirections.actionSearchFragmentToProfileFragment(
                username, sharedTransitionName
            ), navigatorExtras = extras
        )
    }

    private fun onClickFavorite(isFavorite: Boolean, user: UserModel) {
        viewModel.favoriteUser(isFavorite, user)
        viewModel.searchUsers(binding.svUser.query.toString(), false)
        val message = if (isFavorite) {
            "Successfully removed ${user.login} from favorite"
        } else {
            "Successfully added ${user.login} to favorite"
        }
        requireActivity().makeSnackbar(message, "UNDO") {
            viewModel.favoriteUser(!isFavorite, user)
            viewModel.searchUsers(binding.svUser.query.toString(), false)
        }
    }

    override fun setupUI() {
        userAdapter = SearchUserAdapter(::onClickItem, ::onClickFavorite)
        binding.rvUsers.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvUsers.adapter = userAdapter
        binding.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.svUser.clearFocus()
                viewModel.searchUsers(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        binding.ibSettings.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSettingsFragment())
        }
        binding.ibFavorite.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToFavoriteFragment())
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.searchUsers(binding.svUser.query.toString())
            binding.swipeRefresh.isRefreshing = false
        }
        binding.svUser.setOnClickListener {
            binding.svUser.isIconified = false
        }
    }

    override fun setupObservers() {
        viewModel.listUsers.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Empty -> {
                    setError(false)
                    setLoading(false)
                    setEmpty(true)
                }

                is Result.Error -> {
                    setEmpty(false)
                    setLoading(false)
                    setError(true)
                }

                is Result.Loading -> {
                    setError(false)
                    setEmpty(false)
                    setLoading(true)
                }

                is Result.Success -> {
                    setError(false)
                    setEmpty(false)
                    setLoading(false)
                    userAdapter.submitList(result.data)
                }
            }
        }

    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.rvUsers.gone()
            binding.layoutEmpty.gone()
            binding.layoutError.gone()
            binding.loadingIndicator.visible()
            return
        }
        binding.rvUsers.visible()
        binding.loadingIndicator.gone()
        binding.layoutError.gone()
        binding.layoutEmpty.gone()
    }

    private fun setError(isError: Boolean) {
        if (isError) {
            binding.rvUsers.gone()
            binding.layoutEmpty.gone()
            binding.layoutError.visible()
            return
        }
        binding.rvUsers.visible()
        binding.layoutEmpty.gone()
        binding.layoutError.gone()
    }

    private fun setEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvUsers.gone()
            binding.layoutError.gone()
            binding.layoutEmpty.visible()
            return
        }
        binding.rvUsers.visible()
        binding.layoutError.gone()
        binding.layoutEmpty.gone()
    }
}