## RXJava
-dontwarn java.lang.invoke.*

# To Remove the configuration keeps the entry point ... but not the descriptor class ...
-keep class android.support.v4.view.PagerAdapter
-keep class android.support.v4.view.ViewPager$OnAdapterChangeListener
-keep class android.support.v4.view.ViewPager$OnPageChangeListener
-keep class android.support.v4.widget.DrawerLayout$DrawerListener
-keep class android.support.v4.widget.NestedScrollView$OnScrollChangeListener
-keep class android.support.v4.widget.SlidingPaneLayout$PanelSlideListener
-keep class android.support.v4.widget.SwipeRefreshLayout$OnRefreshListener
-keep class android.support.v7.view.menu.MenuBuilder$ItemInvoker
-keep class android.support.v7.view.menu.ActionMenuItemView$PopupCallback
-keep class android.support.v7.widget.ScrollingTabContainerView
-keep class android.support.v7.widget.ActionBarOverlayLayout$ActionBarVisibilityCallback
-keep class android.support.v7.widget.ActionMenuPresenter
-keep class android.support.v7.widget.ActionMenuView$OnMenuItemClickListener
-keep class android.support.v7.widget.ActivityChooserModel
-keep class android.support.v4.view.ActionProvider
-keep class android.support.v7.widget.ContentFrameLayout$OnAttachListener
-keep class android.support.v7.widget.FitWindowsViewGroup$OnFitSystemWindowsListener
-keep class android.support.v7.widget.FitWindowsViewGroup$OnFitSystemWindowsListener
-keep class android.support.v7.widget.SearchView$OnQueryTextListener
-keep class android.support.v7.widget.SearchView$OnCloseListener
-keep class android.support.v7.widget.SearchView$OnSuggestionListener
-keep class android.support.v4.widget.CursorAdapter
-keep class android.support.v7.widget.SearchView
-keep class android.support.v7.widget.Toolbar$OnMenuItemClickListener
-keep class android.support.v7.widget.ViewStubCompat$OnInflateListener
-keep class android.support.design.widget.NavigationView$OnNavigationItemSelectedListener
-keep class android.support.design.widget.Snackbar$SnackbarLayout$OnLayoutChangeListener
-keep class android.support.design.widget.Snackbar$SnackbarLayout$OnAttachStateChangeListener
-keep class android.support.design.widget.TabLayout$OnTabSelectedListener
-keep class android.support.v4.view.ViewPager
-keep class android.support.v7.widget.RecyclerViewAccessibilityDelegate
-keep class android.support.v7.widget.RecyclerView$Adapter
-keep class android.support.v7.widget.RecyclerView$RecyclerListener
-keep class android.support.v7.widget.RecyclerView$LayoutManager
-keep class android.support.v7.widget.RecyclerView$RecycledViewPool
-keep class android.support.v7.widget.RecyclerView$ViewCacheExtension
-keep class android.support.v7.widget.RecyclerView$ChildDrawingOrderCallback
-keep class android.support.v7.widget.RecyclerView$OnScrollListener
-keep class android.support.v7.widget.RecyclerView$ItemAnimator
-keep class android.support.v4.app.DialogFragment
-keep class android.support.v4.app.Fragment
-keep class android.support.v4.app.Activity
-keep class android.support.design.widget.FloatingActionButton
-keep class android.support.v7.widget.AppCompatImageHelper

## Retrofit

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# OkHttp3
-dontwarn okhttp3.**
-dontwarn okio.**

-dontwarn com.squareup.picasso.**