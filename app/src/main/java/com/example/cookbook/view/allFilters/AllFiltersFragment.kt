package com.example.cookbook.view.allFilters

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.transition.AutoTransition
import androidx.transition.Fade
import androidx.transition.Fade.IN
import androidx.transition.Fade.OUT
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.example.cookbook.databinding.FragmentAllFiltersBinding
import com.example.cookbook.utils.BUNDLE_DISH_TYPE_FILTER
import com.example.cookbook.utils.BUNDLE_INCLUDE_INGREDIENT_FILTER
import com.example.cookbook.utils.DISH_TYPE_APPETIZER
import com.example.cookbook.utils.DISH_TYPE_BEVERAGE
import com.example.cookbook.utils.DISH_TYPE_BREAD
import com.example.cookbook.utils.DISH_TYPE_BREAKFAST
import com.example.cookbook.utils.DISH_TYPE_DESSERT
import com.example.cookbook.utils.DISH_TYPE_DRINK
import com.example.cookbook.utils.DISH_TYPE_FINGERFOOD
import com.example.cookbook.utils.DISH_TYPE_MAIN_COURSE
import com.example.cookbook.utils.DISH_TYPE_MARINADE
import com.example.cookbook.utils.DISH_TYPE_SALAD
import com.example.cookbook.utils.DISH_TYPE_SAUCE
import com.example.cookbook.utils.DISH_TYPE_SIDE_DISH
import com.example.cookbook.utils.DISH_TYPE_SNACK
import com.example.cookbook.utils.DISH_TYPE_SOUP
import com.example.cookbook.utils.FRAGMENT_SEARCH
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.mainActivity.MainActivity
import com.example.cookbook.view.search.SearchFragment
import com.example.cookbook.view.search.SearchViewModel
import com.google.android.material.chip.Chip
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AllFiltersFragment : Fragment() {

    private var _binding: FragmentAllFiltersBinding? = null
    private val binding get() = _binding!!
    private var navigationManager: NavigationManager? = null
    private val model: SearchViewModel by activityViewModel()
    private val includeList: MutableSet<String> = mutableSetOf()
    private val dishTypeList: MutableSet<String> = mutableSetOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllFiltersBinding.inflate(inflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun initView() {
        with(binding) {
            btnBack.setOnClickListener {
                backToSearch()
            }

            btnAccept.setOnClickListener {
                val args = Bundle().apply {
                    putString(BUNDLE_INCLUDE_INGREDIENT_FILTER, includeList.joinToString())
                    putString(BUNDLE_DISH_TYPE_FILTER, dishTypeList.joinToString())
                }
                model.updateArguments(args)
                backToSearch()
            }

            etIngredient.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    p0?.let {
                        btnAdd.isVisible = it.isNotEmpty()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })

            btnAdd.setOnClickListener {
                val newChip: Chip = Chip(it.context).apply {
                    this.text = etIngredient.text
                    this.isCloseIconVisible = true
                    includeList.add(this.text.toString())
                    this.setOnCloseIconClickListener {
                        chipsInclude.removeView(this)
                        includeList.remove(this.text.toString())
                    }
                }
                println(includeList)

                TransitionManager.beginDelayedTransition(chipsInclude, Slide(Gravity.BOTTOM))
                chipsInclude.addView(newChip)
                etIngredient.text = null
            }

            chipsDishType.forEach {
                (it as Chip).setOnCheckedChangeListener(checkedChangeListener)
            }

            btnClear.setOnClickListener {
                binding.chipsDishType.forEach {
                    (it as Chip).isChecked = false
                }
                binding.chipsInclude.forEach {
                    (it as Chip).isChecked = false
                }
            }
        }
    }

    private fun backToSearch() {
        val searchFragment = SearchFragment.newInstance()
        navigationManager?.switchFragment(
            FRAGMENT_SEARCH, searchFragment, addToBackStack = true
        )
    }

    private val checkedChangeListener =
        CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            with(binding) {
                when (compoundButton.id) {
                    //---------------------------------------------------------------------Dish type
                    chAppetizer.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_APPETIZER, isChecked)
                    }

                    chBeverage.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_BEVERAGE, isChecked)
                    }

                    chBread.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_BREAD, isChecked)
                    }

                    chBreakfast.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_BREAKFAST, isChecked)
                    }

                    chDessert.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_DESSERT, isChecked)
                    }

                    chDrink.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_DRINK, isChecked)
                    }

                    chFingerfood.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_FINGERFOOD, isChecked)
                    }

                    chMainCourse.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_MAIN_COURSE, isChecked)
                    }

                    chMarinade.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_MARINADE, isChecked)
                    }

                    chSalad.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_SALAD, isChecked)
                    }

                    chSauce.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_SAUCE, isChecked)
                    }

                    chSideDish.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_SIDE_DISH, isChecked)
                    }

                    chSnack.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_SNACK, isChecked)
                    }

                    chSoup.id -> {
                        addOrRemoveListener(dishTypeList, DISH_TYPE_SOUP, isChecked)
                    }
                }
            }
        }

    private fun addOrRemoveListener(
        listType: MutableSet<String>, ingredient: String, isChecked: Boolean
    ) {
        if (isChecked) {
            listType.add(ingredient)
        } else {
            listType.remove(ingredient)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AllFiltersFragment()
    }
}