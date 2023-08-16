package com.example.cookbook.view.home.randomCuisineRecipes

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.cookbook.stacklayoutmanager.StackLayoutManager
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentRandomCuisineRecipesBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.utils.CUISINE_ASIA
import com.example.cookbook.utils.CUISINE_INDIA
import com.example.cookbook.utils.CUISINE_MDTRN
import com.example.cookbook.utils.CUISINE_NORDIC
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.home.randomRecipe.RandomRecipeListAdapter
import com.example.cookbook.view.mainActivity.MainActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RandomCuisineRecipeListFragment :
    BaseFragment<AppState, List<RandomRecipeData>, FragmentRandomCuisineRecipesBinding>(
        FragmentRandomCuisineRecipesBinding::inflate
    ) {

    private lateinit var model: RandomCuisineRecipeListViewModel

    private val adapter: RandomRecipeListAdapter by lazy { RandomRecipeListAdapter() }
    private var navigationManager: NavigationManager? = null

    private lateinit var cuisine: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    companion object {
        fun newInstance(): RandomCuisineRecipeListFragment {
            return RandomCuisineRecipeListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cuisine = listOf(CUISINE_ASIA, CUISINE_MDTRN, CUISINE_NORDIC, CUISINE_INDIA).random()
        initViewModel()
        model.getRandomRecipesByCuisine(cuisine)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackgroundImage(cuisine)
    }

    private fun initBackgroundImage(cuisine: String) {
        initTitleText()
        with(binding){
            val imageResource = when(cuisine) {
                CUISINE_ASIA -> R.drawable.background_asia
                CUISINE_MDTRN -> R.drawable.background_mediterranian
                CUISINE_INDIA -> R.drawable.background_indian
                CUISINE_NORDIC -> R.drawable.background_nordic
                else -> return
            }
            backgroundImage.load(imageResource) {
                listener(
                    onSuccess = {_,_ ->
                        backgroundImage.viewTreeObserver.addOnPreDrawListener(
                            object : ViewTreeObserver.OnPreDrawListener{
                                override fun onPreDraw(): Boolean {
                                    backgroundImage.viewTreeObserver.removeOnPreDrawListener(this)
                                    val imageWidth = backgroundImage.drawable.intrinsicWidth
                                    val imageViewWith = backgroundImage.width
                                    val translationX = (imageWidth - imageViewWith).toFloat()
                                    val animation = ObjectAnimator.ofFloat(
                                        backgroundImage, "translationX", 0f, -translationX)
                                    animation.duration = 10000
                                    animation.repeatCount = ValueAnimator.INFINITE
                                    animation.repeatMode = ValueAnimator.REVERSE
                                    animation.start()
                                    return true
                                }
                            }
                        )
                    }
                )
            }
        }
    }

    private fun initTitleText() {
        with(binding) {
            when(cuisine) {
                CUISINE_ASIA -> {
                    firstLineTitle.apply{
                        text = getString(R.string.asia_title_first_line)
                        setTextColor(ContextCompat.getColor(context, R.color.random_cuisine_text_title_color))
                    }
                    secondLineTitle.apply{
                        text = getString(R.string.asia_title_second_line)
                        setTextColor(ContextCompat.getColor(context, R.color.random_cuisine_text_second_title_color))
                    }
                }
                CUISINE_MDTRN -> {
                    firstLineTitle.apply{
                        text = getString(R.string.mdtrn_title_first_line)
                        setTextColor(ContextCompat.getColor(context, R.color.random_cuisine_text_title_color))
                    }
                    secondLineTitle.apply{
                        text = getString(R.string.mdtrn_title_second_line)
                        setTextColor(ContextCompat.getColor(context, R.color.random_cuisine_text_second_title_color))
                    }
                }
                CUISINE_INDIA -> {
                    firstLineTitle.apply{
                        text = getString(R.string.india_title_first_line)
                        setTextColor(ContextCompat.getColor(context, R.color.random_cuisine_text_title_color))
                    }
                    secondLineTitle.apply{
                        text = getString(R.string.india_title_second_line)
                        setTextColor(ContextCompat.getColor(context, R.color.random_cuisine_text_second_title_color))
                    }
                }
                CUISINE_NORDIC -> {
                    firstLineTitle.apply{
                        text = getString(R.string.nordic_title_first_line)
                        setTextColor(ContextCompat.getColor(context, R.color.random_cuisine_text_title_color))
                    }
                    secondLineTitle.apply{
                        text = getString(R.string.nordic_title_second_line)
                        setTextColor(ContextCompat.getColor(context, R.color.random_cuisine_text_second_title_color))
                    }
                }
                else -> return
            }
        }
    }

    private fun initViewModel() {
        val viewModel by viewModel<RandomCuisineRecipeListViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
    }

    override fun setupData(data: List<RandomRecipeData>) {
        adapter.setData(data)
        val layoutManager = StackLayoutManager()
        binding.randomRecipesRecyclerView.adapter = adapter
        binding.randomRecipesRecyclerView.layoutManager = layoutManager

        initFavoritesListeners()

        adapter.listener = { recipe ->
            openRecipeInfoFragment(recipe.id)
        }
    }

    private fun openRecipeInfoFragment(recipeId: Int) {
        navigationManager?.openRecipeInfoFragment(recipeId)
    }

    private fun initFavoritesListeners() {
        adapter.listenerOnSaveRecipe = { recipe ->
            model.insertNewRecipeToDataBase(recipe)
        }

        adapter.listenerOnRemoveRecipe = { recipe ->
            model.deleteRecipeFromData(recipe.id)
        }
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Error {$message}", Toast.LENGTH_LONG).show()
    }
}