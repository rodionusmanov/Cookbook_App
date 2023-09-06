package com.example.cookbook.view.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import coil.load
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentHomeBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.utils.BUNDLE_DISH_TYPE
import com.example.cookbook.utils.DISH_TYPE_BREAKFAST
import com.example.cookbook.utils.DISH_TYPE_DESSERT
import com.example.cookbook.utils.DISH_TYPE_MAIN_COURSE
import com.example.cookbook.utils.DISH_TYPE_SALAD
import com.example.cookbook.utils.DISH_TYPE_SIDE_DISH
import com.example.cookbook.utils.DISH_TYPE_SNACK
import com.example.cookbook.utils.FRAGMENT_FAVORITE
import com.example.cookbook.utils.FRAGMENT_PROFILE
import com.example.cookbook.utils.FRAGMENT_SEARCH
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.favorite.FavoriteFragment
import com.example.cookbook.view.home.healthyRandomRecipe.HealthyRandomRecipeListFragment
import com.example.cookbook.view.home.randomCuisineRecipes.RandomCuisineRecipeListFragment
import com.example.cookbook.view.home.randomRecipe.RandomRecipesListFragment
import com.example.cookbook.view.mainActivity.MainActivity
import com.example.cookbook.view.myProfile.MyProfileFragment
import com.example.cookbook.view.search.SearchFragment
import com.example.cookbook.view.search.SearchViewModel
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class HomeFragment :
    BaseFragment<AppState, String, FragmentHomeBinding>(
        FragmentHomeBinding::inflate
    ) {

    private val model: HomeViewModel by viewModel()
    private var navigationManager: NavigationManager? = null
    private var isJokeTextExpanded = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        setupSearchView()
        //initRandomRecipeFragment()
        //initHealthyRandomRecipeFragment()
        initDishTypeCards()
        //initRandomCuisineFragment()
        initServiceButtons()
        initUserAvatarImage()
        initJokeTextContainer()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initJokeTextContainer() {
        binding.moreTextButton.setOnClickListener {
            with(binding) {
                val transition = createAutoTransition()
                if (isJokeTextExpanded) {
                    moreTextButton.text = getString(R.string.joke_read_more)
                    animateBlockCloseMark(binding.moreTextMark)

                    animateJokeTextHide {
                        TransitionManager.beginDelayedTransition(binding.root, transition)
                    }
                } else {
                    jokeTextFull.maxLines = Integer.MAX_VALUE
                    moreTextButton.text = getString(R.string.joke_show_less)
                    TransitionManager.beginDelayedTransition(binding.root, transition)
                    animateBlockOpenMark(binding.moreTextMark)
                }
            }
            isJokeTextExpanded = !isJokeTextExpanded
        }
    }

    private fun createAutoTransition(): AutoTransition {
        val transition = AutoTransition()
        transition.duration = 500
        return transition
    }

    private fun animateJokeTextHide(
        onAnimationEndExtra: (() -> Unit)? = null
    ) {
        val initialHeight = binding.jokeTextFull.height
        val targetHeight = getTextViewHeight(binding.jokeTextFull, 5)

        val heightAnimator = ValueAnimator.ofInt(initialHeight, targetHeight)
        heightAnimator.addUpdateListener {
            val animatedValue = it.animatedValue as Int
            val layoutParams = binding.jokeTextFull.layoutParams
            layoutParams.height = animatedValue
            binding.jokeTextFull.layoutParams = layoutParams
        }

        heightAnimator.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.jokeTextFull.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.jokeTextFull.maxLines = 5
                onAnimationEndExtra?.invoke()
            }
        })

        heightAnimator.start()
    }

    private fun getTextViewHeight(textView: TextView, maxLine: Int): Int {
        return textView.lineHeight * maxLine
    }

    private fun animateBlockCloseMark(cardView: MaterialCardView) {
        val rotate = RotateAnimation(
            180f, 0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 500
        rotate.fillAfter = true
        cardView.startAnimation(rotate)
    }

    private fun animateBlockOpenMark(cardView: MaterialCardView) {
        val rotate = RotateAnimation(
            0f, 180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 500
        rotate.fillAfter = true
        cardView.startAnimation(rotate)
    }

    private fun initUserAvatarImage() {
        val file = File(requireContext().filesDir, "avatar_image.jpg")
        if (file.exists()) {
            binding.userAvatarImage.load(file)
        }
        binding.userAvatarImage.setOnClickListener {
            val profileFragment = MyProfileFragment()
            navigationManager?.switchFragment(
                FRAGMENT_PROFILE,
                profileFragment,
                addToBackStack = true
            )
        }
    }

    private fun initServiceButtons() {
        with(binding) {
            favoritesService.setOnClickListener {
                val favoriteFragment = FavoriteFragment.newInstance()
                navigationManager?.switchFragment(
                    FRAGMENT_FAVORITE,
                    favoriteFragment,
                    addToBackStack = true
                )
            }
            detailedIngredientSearch.setOnClickListener {
                navigationManager?.switchFragment(
                    FRAGMENT_SEARCH,
                    addToBackStack = true
                ) { fragment ->
                    (fragment as? SearchFragment)?.openAllFiltersFragment()
                }
            }
        }
    }

    private fun initRandomCuisineFragment() {
        val existingFragment =
            childFragmentManager.findFragmentById(R.id.random_cuisine_recipes_container)
        if (existingFragment == null) {
            val fragment = RandomCuisineRecipeListFragment.newInstance()
            childFragmentManager
                .beginTransaction()
                .replace(R.id.random_cuisine_recipes_container, fragment)
                .commit()
        }
    }

    private fun initHealthyRandomRecipeFragment() {
        val existingFragment =
            childFragmentManager.findFragmentById(R.id.healthy_random_recipes_container)
        if (existingFragment == null) {
            val fragment = HealthyRandomRecipeListFragment.newInstance()
            childFragmentManager
                .beginTransaction()
                .replace(R.id.healthy_random_recipes_container, fragment)
                .commit()
        }
    }

    private fun initDishTypeCards() {
        binding.variousDishes.cardBreakfast.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_BREAKFAST)
        }
        binding.variousDishes.cardSideDish.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_SIDE_DISH)
        }
        binding.variousDishes.cardMainCourse.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_MAIN_COURSE)
        }
        binding.variousDishes.cardSalads.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_SALAD)
        }
        binding.variousDishes.cardSnack.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_SNACK)
        }
        binding.variousDishes.cardDessert.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_DESSERT)
        }
    }

    private fun initRandomRecipeFragment() {
        val existingFragment = childFragmentManager.findFragmentById(R.id.random_recipe_container)
        if (existingFragment == null) {
            val fragment = RandomRecipesListFragment.newInstance()
            childFragmentManager
                .beginTransaction()
                .replace(R.id.random_recipe_container, fragment)
                .commit()
        }
    }

    private fun setupSearchView() {

        binding.searchView.setOnQueryTextListener(
            object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        openSearchFragmentWithQuery("search_query", it)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            }
        )
        binding.homeFragmentContainer.setOnScrollChangeListener { _, _, _, _, _ ->
            binding.toolbar.isSelected = binding.homeFragmentContainer.canScrollVertically(-1)
        }
    }

    private fun openSearchFragmentWithQuery(queryKey: String, query: String) {

        val args = Bundle().apply {
            putString(queryKey, query)
        }
        val searchViewModel: SearchViewModel by activityViewModel()
        searchViewModel.updateArguments(args)
        val searchFragment = SearchFragment.newInstance()
        navigationManager?.switchFragment(FRAGMENT_SEARCH, searchFragment, addToBackStack = true)
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
        //requestJokeText()
    }

    private fun requestJokeText() {
        model.getJokeText()
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun setupData(data: String) {
        with(binding) {
            jokeTextFull.text = data
            jokeTextFull.post {
                if (jokeTextFull.lineCount < 8) {
                    moreTextButton.visibility = View.GONE
                    moreTextMark.visibility = View.GONE
                }
            }
        }
    }

    fun updateAvatar(avatarUri: Uri?) {
        avatarUri?.let {
            binding.userAvatarImage.load(it)
        }
    }
}