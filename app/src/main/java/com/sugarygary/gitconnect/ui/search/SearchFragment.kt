package com.sugarygary.gitconnect.ui.search

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugarygary.gitconnect.databinding.FragmentSearchBinding
import com.sugarygary.gitconnect.ui.base.BaseFragment
import com.sugarygary.gitconnect.utils.gone
import com.sugarygary.gitconnect.utils.visible


class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var userAdapter: SearchUserAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.listUsers.value == null) {
            viewModel.fetchUsers()
        }
        //untuk pop transition dengan container transform
        postponeEnterTransition()
        (requireView().parent as ViewGroup).viewTreeObserver
            .addOnPreDrawListener {
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

    override fun setupUI() {
        userAdapter = SearchUserAdapter(::onClickItem)
        binding.rvUsers.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvUsers.adapter = userAdapter
        binding.svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.svUser.clearFocus()
                viewModel.fetchUsers(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        binding.imageButton.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSettingsFragment())
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchUsers(binding.svUser.query.toString())
            binding.swipeRefresh.isRefreshing = false
        }
        binding.svUser.setOnClickListener {
            binding.svUser.isIconified = false
        }
    }

    override fun setupObservers() {
        viewModel.isError.observe(viewLifecycleOwner) { isError ->
            setError(isError)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setLoading(isLoading)
        }
        viewModel.listUsers.observe(viewLifecycleOwner) { listUser ->
            setEmpty(listUser.isEmpty())
            userAdapter.submitList(listUser)
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