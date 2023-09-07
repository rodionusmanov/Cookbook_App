package com.example.cookbook.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.cookbook.R
import com.example.cookbook.model.repository.sharedPreferences.SharedPreferencesRepository
import com.example.cookbook.utils.FRAGMENT_HOME
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.mainActivity.MainActivity
import org.koin.android.ext.android.inject

class FirstLaunchFragment : Fragment() {


    private val sharedPrefs : SharedPreferencesRepository by inject()
    private var navigationManager: NavigationManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first_launch, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_close_first_launch).setOnClickListener {
            sharedPrefs.setNotFirstLaunch()
            navigationManager?.switchFragment(FRAGMENT_HOME, addToBackStack = true)
        }
    }
}