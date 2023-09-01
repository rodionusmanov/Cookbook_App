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
import android.view.animation.BounceInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
        initRandomRecipeFragment()
        initHealthyRandomRecipeFragment()
        initDishTypeCards()
        initRandomCuisineFragment()
        initServiceButtons()
        initUserAvatarImage()
        initJokeTextContainer()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initJokeTextContainer() {
        binding.moreTextButton.setOnClickListener {
            animateJokeTextChange()
            with(binding) {
                if(isJokeTextExpanded) {
                    jokeText.maxLines = 5
                    jokeText.requestLayout()
                    moreTextButton.text = getString(R.string.joke_read_more)
                    animateBlockCloseMark(binding.moreTextMark)
                } else {
                    jokeText.maxLines = Integer.MAX_VALUE
                    jokeText.requestLayout()
                    moreTextButton.text = getString(R.string.joke_show_less)
                    animateBlockOpenMark(binding.moreTextMark)
                }
            }
            isJokeTextExpanded = !isJokeTextExpanded
        }
    }

    private fun animateJokeTextChange() {
        val startHeight: Int
        val endHeight: Int

        if(isJokeTextExpanded) {
            startHeight = binding.jokeText.measuredHeight
            binding.jokeText.maxLines = 5
            binding.jokeText.measure(
                View.MeasureSpec.makeMeasureSpec(binding.jokeText.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            endHeight = binding.jokeText.measuredHeight
        } else {
            startHeight = binding.jokeText.measuredHeight
            binding.jokeText.maxLines = Integer.MAX_VALUE
            binding.jokeText.measure(
                View.MeasureSpec.makeMeasureSpec(binding.jokeText.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            endHeight = binding.jokeText.measuredHeight
        }

        val animator = ValueAnimator.ofInt(startHeight, endHeight).apply{
            duration = 500
            addUpdateListener { animation->
                val animatedValue = animation.animatedValue as Int
                val layoutParams = binding.jokeText.layoutParams
                layoutParams.height = animatedValue
                binding.jokeText.layoutParams = layoutParams
                binding.jokeText.alpha = (animatedValue - startHeight).toFloat()/
                        (endHeight - startHeight)
                binding.jokeText.requestLayout()
            }
            addListener(object: AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.jokeText.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
            })
        }
        animator.start()
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
        binding.favoritesService.setOnClickListener {
            val favoriteFragment = FavoriteFragment.newInstance()
            navigationManager?.switchFragment(
                FRAGMENT_FAVORITE,
                favoriteFragment,
                addToBackStack = true
            )
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
        requestJokeText()
    }

    private fun requestJokeText() {
        model.getJokeText()
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun setupData(data: String) {
        binding.jokeText.text = data
    }

    fun updateAvatar(avatarUri: Uri?) {
        avatarUri?.let {
            binding.userAvatarImage.load(it)
        }
    }
}