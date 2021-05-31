package com.coffeetrainlabs.kmpplayground

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.coffeetrainlabs.kmpplayground.FooPagerAdapter.PageType.ALL
import com.coffeetrainlabs.kmpplayground.FooPagerAdapter.PageType.LATEST
import com.coffeetrainlabs.kmpplayground.databinding.HomeScreenBinding
import kotlinx.android.parcel.Parcelize

@Parcelize
object HomeScreen : Screen {
  override fun layoutResId() = R.layout.home_screen
}

class HomeScreenView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
  init {
    orientation = VERTICAL
  }

  private val binding by viewBinding(HomeScreenBinding::bind)

  override fun onFinishInflate() {
    super.onFinishInflate()
    binding.fooViewPager.isUserInputEnabled = false
    binding.fooViewPager.adapter = FooPagerAdapter(context)
    binding.fooViewPager.registerOnPageChangeCallback(object :
      ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        setLiftOnScrollIDForFooTab(position)
      }
    })
    binding.fooViewPager.offscreenPageLimit = 1
    synchronizeTabsAndViewPager(binding.tabs, binding.fooViewPager)
    binding.bottomNav.setOnNavigationItemSelectedListener { item ->
      // The visibility management here is super-nasty because
      // 1) CoordinatorLayout requires appbar_scrolling_view_behavior views to be direct siblings
      //    of the CoordinatorLayout to get the scroll behavior we want (lift on scroll).
      // 2) The transition we want is that old views disappear immediately and new ones fade in.
      // 3) But TabLayout doesn't support alpha animations so it just has to pop in.
      // 4) We're using ViewStubs for the taco/burrito tabs so we don't prematurely inflate them
      val showAsSelected: Boolean
      when (item.itemId) {
        R.id.foo_tab -> {
          binding.mainContent.showOnlyChildFadeIn(
            binding.fooViewPager,
            R.id.taco_inflated,
            R.id.burrito_inflated
          )
          showAsSelected = true
          binding.tabs.visibility = VISIBLE
          binding.title.text = "Foo"
          val position = binding.tabs.selectedTabPosition
          setLiftOnScrollIDForFooTab(position)
        }
        R.id.taco_tab -> {
          showAsSelected = true
          binding.tabs.visibility = GONE
          val tacoView =
            findOrInflate<TacoView>(binding.tacoStub) { view -> view.alpha = 0f }
          binding.mainContent.showOnlyChildFadeIn(
            tacoView,
            R.id.foo_view_pager,
            R.id.burrito_inflated
          )
          binding.title.text = "Taco"
          binding.appBarLayout.liftOnScrollTargetViewId = R.id.taco_inflated
        }
        R.id.burrito_tab -> {
          showAsSelected = true
          binding.tabs.visibility = GONE
          val burritoView =
            findOrInflate<BurritoListView>(binding.burritoStub) { view -> view.alpha = 0f }
          binding.mainContent.showOnlyChildFadeIn(
            burritoView,
            R.id.foo_view_pager,
            R.id.taco_inflated
          )
          binding.title.text = "Burrito"
          binding.appBarLayout.liftOnScrollTargetViewId = R.id.burrito_inflated
        }
        else -> throw IllegalStateException("omfg")
      }
      return@setOnNavigationItemSelectedListener showAsSelected
    }
  }

  private fun setLiftOnScrollIDForFooTab(position: Int) {
    val listViewID = FooPagerAdapter.PageType.values()[position].listViewID
    binding.appBarLayout.liftOnScrollTargetViewId = listViewID
  }
}

class FooPagerAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  init {
    setHasStableIds(true)
  }

  enum class PageType(@IdRes val listViewID: Int) {
    LATEST(R.id.actual_list_latest),
    ALL(R.id.actual_list_all)
  }

  private val inflater = LayoutInflater.from(context)

  override fun getItemViewType(position: Int): Int {
    return position
  }

  private fun pageType(position: Int): PageType {
    when (position) {
      0 -> return LATEST
      1 -> return ALL
      else -> throw IllegalStateException("Invalid position $position")
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val pageType = pageType(viewType)
    val view = inflater.inflate(R.layout.burrito_view, parent, false) as BurritoListView
    view.id = pageType.listViewID
    return PageViewHolder(view)
  }

  override fun getItemCount(): Int {
    return PageType.values().size
  }

  override fun getItemId(position: Int): Long = position.toLong()

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
  }

  class PageViewHolder(view: BurritoListView) : RecyclerView.ViewHolder(view)
}

