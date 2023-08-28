package com.example.cookbook.view.allFilters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
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
import com.example.cookbook.utils.INCLUDE_INGREDIENT_BEEF
import com.example.cookbook.utils.INCLUDE_INGREDIENT_CHICKEN
import com.example.cookbook.utils.INCLUDE_INGREDIENT_FISH
import com.example.cookbook.utils.INCLUDE_INGREDIENT_PORK
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


            chipsInclude.forEach {
                (it as Chip).setOnCheckedChangeListener(checkedChangeListener)
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
            FRAGMENT_SEARCH,
            searchFragment,
            addToBackStack = true
        )
    }

    private val checkedChangeListener =
        CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            with(binding) {
                when (compoundButton.id) {
                    //------------------------------------------------------------Include ingredient
                    chChicken.id -> {
                        addOrRemoveListener(includeList, INCLUDE_INGREDIENT_CHICKEN, isChecked)
                    }

                    chPork.id -> {
                        addOrRemoveListener(includeList, INCLUDE_INGREDIENT_PORK, isChecked)
                    }

                    chBeef.id -> {
                        addOrRemoveListener(includeList, INCLUDE_INGREDIENT_BEEF, isChecked)
                    }

                    chFish.id -> {
                        addOrRemoveListener(includeList, INCLUDE_INGREDIENT_FISH, isChecked)
                    }
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
        listType: MutableSet<String>,
        ingredient: String,
        isChecked: Boolean
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