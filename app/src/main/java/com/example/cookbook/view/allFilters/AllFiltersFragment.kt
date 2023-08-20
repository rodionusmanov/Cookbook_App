package com.example.cookbook.view.allFilters

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentAllFiltersBinding
import com.example.cookbook.databinding.FragmentSearchDialogBinding
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.mainActivity.MainActivity

class AllFiltersFragment : Fragment() {

    private var _binding: FragmentAllFiltersBinding? = null
    private val binding get() = _binding!!
    private var navigationManager: NavigationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllFiltersBinding.inflate(inflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AllFiltersFragment()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}