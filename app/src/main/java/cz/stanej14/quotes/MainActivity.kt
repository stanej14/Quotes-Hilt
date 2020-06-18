package cz.stanej14.quotes

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cz.stanej14.quotes.domain.livedata.EventObserver
import cz.stanej14.quotes.ui.feed.FeedFragment
import cz.stanej14.quotes.ui.landing.LandingFragment
import cz.stanej14.quotes.ui.login.LoginFragment
import cz.stanej14.quotes.ui.main.NavigationViewModel
import cz.stanej14.quotes.ui.main.NavigationViewModel.NavigationEvent.*
import cz.stanej14.quotes.ui.quoteDetail.QuoteDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: NavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            replaceFragment(LandingFragment.newInstance())
        }

        viewModel.navigationEvents.observe(this, EventObserver {
            when (it) {
                is Feed -> replaceFragment(FeedFragment.newInstance())
                is Login -> replaceFragment(LoginFragment.newInstance(), true)
                is QuoteDetail -> replaceFragment(QuoteDetailFragment.newInstance(it.quote), true)
            }
        })
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .also {
                if (addToBackStack) {
                    it.addToBackStack(fragment.javaClass.name)
                        .commit()
                } else {
                    it.commitNow()
                }
            }

    }
}