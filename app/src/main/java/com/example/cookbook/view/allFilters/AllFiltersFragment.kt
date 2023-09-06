package com.example.cookbook.view.allFilters

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.transition.AutoTransition
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentAllFiltersBinding
import com.example.cookbook.utils.BUNDLE_CUISINE_TYPE_FILTER
import com.example.cookbook.utils.BUNDLE_DISH_TYPE_FILTER
import com.example.cookbook.utils.BUNDLE_EXCLUDE_INGREDIENT_FILTER
import com.example.cookbook.utils.BUNDLE_INCLUDE_INGREDIENT_FILTER
import com.example.cookbook.utils.BUNDLE_MAX_CAL_FILTER
import com.example.cookbook.utils.BUNDLE_MAX_TIME_FILTER
import com.example.cookbook.utils.BUNDLE_MIN_CAL_FILTER
import com.example.cookbook.utils.CUISINE_TYPE_AMERICAN
import com.example.cookbook.utils.CUISINE_TYPE_ASIAN
import com.example.cookbook.utils.CUISINE_TYPE_CHINESE
import com.example.cookbook.utils.CUISINE_TYPE_EASTERN_EUROPEAN
import com.example.cookbook.utils.CUISINE_TYPE_EUROPEAN
import com.example.cookbook.utils.CUISINE_TYPE_FRENCH
import com.example.cookbook.utils.CUISINE_TYPE_GREEK
import com.example.cookbook.utils.CUISINE_TYPE_ITALIAN
import com.example.cookbook.utils.CUISINE_TYPE_JAPANESE
import com.example.cookbook.utils.CUISINE_TYPE_KOREAN
import com.example.cookbook.utils.CUISINE_TYPE_MEDITERRANEAN
import com.example.cookbook.utils.CUISINE_TYPE_MEXICAN
import com.example.cookbook.utils.CUISINE_TYPE_MIDDLE_EASTERN
import com.example.cookbook.utils.CUISINE_TYPE_SPANISH
import com.example.cookbook.utils.CUISINE_TYPE_THAI
import com.example.cookbook.utils.CUISINE_TYPE_VIETNAMESE
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AllFiltersFragment : Fragment() {

    private var _binding: FragmentAllFiltersBinding? = null
    private val binding get() = _binding!!
    private var navigationManager: NavigationManager? = null
    private val model: SearchViewModel by activityViewModel()
    private var flagDishType = false
    private var flagCuisineType = false
    private val includeList: MutableSet<String> = mutableSetOf()
    private val excludeList: MutableSet<String> = mutableSetOf()
    private val dishTypeList: MutableSet<String> = mutableSetOf()
    private val cuisineTypeList: MutableSet<String> = mutableSetOf()
    private var time: Int? = null
    private var minCal: Int? = null
    private var maxCal: Int? = null
    private var clear = 0

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
            initBackClearAndAcceptButtons()

            initAddAndRemoveIngredients(
                etIncludeIngredient,
                btnAddInclude,
                includeList,
                chipsInclude
            )

            initAddAndRemoveIngredients(
                etExcludeIngredient,
                btnAddExclude,
                excludeList,
                chipsExclude
            )

            btnCuisinesType.setOnClickListener {
                initChipsGroup(btnCuisinesType, flagCuisineType, chipsCuisineType)
                flagCuisineType = !flagCuisineType
            }
            btnDishType.setOnClickListener {
                initChipsGroup(btnDishType, flagDishType, chipsDishType)
                flagDishType = !flagDishType
            }

            chipsCalories.forEach {
                (it as Chip).setOnCheckedChangeListener(checkedChangeListener)
            }

            chipsTime.forEach {
                (it as Chip).setOnCheckedChangeListener(checkedChangeListener)
            }

            chipsDishType.forEach {
                (it as Chip).setOnCheckedChangeListener(checkedChangeListener)
            }

            chipsCuisineType.forEach {
                (it as Chip).setOnCheckedChangeListener(checkedChangeListener)
            }
        }
    }

    private fun initChipsGroup(btnChipsType: MaterialButton, flag: Boolean, chipGroup: ChipGroup) {
        if (flag) {
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            btnChipsType.icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.icon_arrow_down)
            chipGroup.forEach {
                if (!(it as Chip).isChecked) {
                    it.isVisible = false
                }
            }
        } else {
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            btnChipsType.icon =
                ContextCompat.getDrawable(requireContext(), R.drawable.icon_arrow_up)
            chipGroup.forEach {
                if (!(it as Chip).isChecked) {
                    it.isVisible = true
                }
            }
        }
    }

    private fun initAddAndRemoveIngredients(
        et: EditText,
        btnAdd: Button,
        ingredientLIst: MutableSet<String>,
        chipGroup: ChipGroup
    ) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    TransitionManager.beginDelayedTransition(binding.root, Fade())
                    btnAdd.isVisible = it.isNotEmpty()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        btnAdd.setOnClickListener {
            val newChip: Chip = Chip(it.context).also { chip ->
                chip.text = et.text
                chip.isCloseIconVisible = true
                ingredientLIst.add(chip.text.toString())
                chip.setOnCloseIconClickListener {
                    ingredientLIst.remove(chip.text.toString())
                    chipGroup.removeView(chip)
                }
            }
            chipGroup.addView(newChip)
            et.text = null
        }
    }

    private fun initBackClearAndAcceptButtons() {
        with(binding) {
            btnBack.setOnClickListener {
                backToSearch()
            }

            btnAccept.setOnClickListener {
                val args = Bundle().apply {
                    putString(BUNDLE_INCLUDE_INGREDIENT_FILTER, includeList.joinToString())
                    putString(BUNDLE_EXCLUDE_INGREDIENT_FILTER, excludeList.joinToString())
                    putString(BUNDLE_DISH_TYPE_FILTER, dishTypeList.joinToString())
                    putString(BUNDLE_CUISINE_TYPE_FILTER, cuisineTypeList.joinToString())
                    time?.let { putInt(BUNDLE_MAX_TIME_FILTER, it) }
                    minCal?.let { putInt(BUNDLE_MIN_CAL_FILTER, it) }
                    maxCal?.let { putInt(BUNDLE_MAX_CAL_FILTER, it) }
                }
                model.updateArguments(args)
                if (flagCuisineType) {
                    btnCuisinesType.performClick()
                }
                if (flagDishType) {
                    btnDishType.performClick()
                }

                backToSearch()
            }

            btnClear.setOnClickListener {
                chipsDishType.forEach {
                    dishTypeList.remove((it as Chip).text.toString())
                    it.isChecked = false
                }
                chipsInclude.removeAllViews()
                chipsExclude.removeAllViews()

                chipsCuisineType.forEach {
                    cuisineTypeList.remove((it as Chip).text.toString())
                    it.isChecked = false
                }
                chipsTime.forEach {
                    (it as Chip).isChecked = false
                }

                chipsCalories.forEach {
                    (it as Chip).isChecked = false
                }

                includeList.clear()
                excludeList.clear()
                cuisineTypeList.clear()
                dishTypeList.clear()
                time = null
                minCal = null
                maxCal = null
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
                    //------------------------------------------------------------------Cuisine type
                    chAmerican.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_AMERICAN,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chAsian.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_ASIAN,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chChinese.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_CHINESE,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chEasternEuropean.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_EASTERN_EUROPEAN,
                            isChecked,
                            compoundButton, flagCuisineType
                        )
                    }

                    chEuropean.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_EUROPEAN,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chFrench.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_FRENCH,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chGreek.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_GREEK,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chItalian.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_ITALIAN,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chJapanese.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_JAPANESE,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chKorean.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_KOREAN,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chMediterranean.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_MEDITERRANEAN,
                            isChecked,
                            compoundButton, flagCuisineType
                        )
                    }

                    chMexican.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_MEXICAN,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chMiddleEastern.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_MIDDLE_EASTERN,
                            isChecked,
                            compoundButton, flagCuisineType
                        )
                    }

                    chSpanish.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_SPANISH,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chThai.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_THAI,
                            isChecked,
                            compoundButton,
                            flagCuisineType
                        )
                    }

                    chVietnamese.id -> {
                        addOrRemoveToIngredientList(
                            cuisineTypeList,
                            CUISINE_TYPE_VIETNAMESE,
                            isChecked,
                            compoundButton, flagCuisineType
                        )
                    }
                    //---------------------------------------------------------------------Dish type
                    chAppetizer.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList,
                            DISH_TYPE_APPETIZER,
                            isChecked,
                            compoundButton,
                            flagDishType
                        )
                    }

                    chBeverage.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList,
                            DISH_TYPE_BEVERAGE,
                            isChecked,
                            compoundButton,
                            flagDishType
                        )
                    }

                    chBread.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList, DISH_TYPE_BREAD, isChecked, compoundButton, flagDishType
                        )
                    }

                    chBreakfast.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList,
                            DISH_TYPE_BREAKFAST,
                            isChecked,
                            compoundButton,
                            flagDishType
                        )
                    }

                    chDessert.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList, DISH_TYPE_DESSERT, isChecked, compoundButton, flagDishType
                        )
                    }

                    chDrink.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList, DISH_TYPE_DRINK, isChecked, compoundButton, flagDishType
                        )
                    }

                    chFingerfood.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList,
                            DISH_TYPE_FINGERFOOD,
                            isChecked,
                            compoundButton,
                            flagDishType
                        )
                    }

                    chMainCourse.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList,
                            DISH_TYPE_MAIN_COURSE,
                            isChecked,
                            compoundButton,
                            flagDishType
                        )
                    }

                    chMarinade.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList,
                            DISH_TYPE_MARINADE,
                            isChecked,
                            compoundButton,
                            flagDishType
                        )
                    }

                    chSalad.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList, DISH_TYPE_SALAD, isChecked, compoundButton, flagDishType
                        )
                    }

                    chSauce.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList, DISH_TYPE_SAUCE, isChecked, compoundButton, flagDishType
                        )
                    }

                    chSideDish.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList,
                            DISH_TYPE_SIDE_DISH,
                            isChecked,
                            compoundButton,
                            flagDishType
                        )
                    }

                    chSnack.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList, DISH_TYPE_SNACK, isChecked, compoundButton, flagDishType
                        )
                    }

                    chSoup.id -> {
                        addOrRemoveToIngredientList(
                            dishTypeList,
                            DISH_TYPE_SOUP,
                            isChecked,
                            compoundButton,
                            flagDishType
                        )
                    }
                    //--------------------------------------------------------------------------Time
                    ch15min.id -> {
                        changeTime(isChecked, 15)
                    }

                    ch30min.id -> {
                        changeTime(isChecked, 30)
                    }

                    ch45min.id -> {
                        changeTime(isChecked, 45)
                    }

                    ch60min.id -> {
                        changeTime(isChecked, 60)
                    }
                    //----------------------------------------------------------------------Calories
                    chTill300.id -> {
                        changeCalories(isChecked, null, 300)
                    }

                    ch300600.id -> {
                        changeCalories(isChecked, 300, 600)
                    }

                    ch600900.id -> {
                        changeCalories(isChecked, 600, 900)
                    }

                    ch9001200.id -> {
                        changeCalories(isChecked, 900, 1200)
                    }

                    chAbove1200.id -> {
                        changeCalories(isChecked, 1200, null)
                    }

                }
                btnClear.isVisible = clear != 0
            }
        }

    private fun changeCalories(isChecked: Boolean, min: Int?, max: Int?) {
        if (isChecked) {
            minCal = min
            maxCal = max
            clear++
        } else {
            minCal = null
            maxCal = null
            clear--
        }
    }

    private fun changeTime(isChecked: Boolean, minute: Int) {
        if (isChecked) {
            time = minute
            clear++
        } else {
            time = null
            clear--
        }
    }

    private fun addOrRemoveToIngredientList(
        listType: MutableSet<String>,
        ingredient: String,
        isChecked: Boolean,
        chip: CompoundButton,
        flag: Boolean
    ) {
        if (isChecked) {
            listType.add(ingredient)
            clear++
        } else {
            listType.remove(ingredient)
            clear--
        }

        if (!flag) {
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            chip.isVisible = isChecked
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AllFiltersFragment()
    }
}