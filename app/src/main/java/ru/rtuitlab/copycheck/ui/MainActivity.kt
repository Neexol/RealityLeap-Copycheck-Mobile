package ru.rtuitlab.copycheck.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.rtuitlab.copycheck.R
import ru.rtuitlab.copycheck.utils.extensions.setupWithNavController

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setupBottomNavigationBar()
	}

	private fun setupBottomNavigationBar() {
		findViewById<BottomNavigationView>(R.id.bottomNav).setupWithNavController(
			navGraphIds = listOf(
				R.navigation.check,
				R.navigation.history,
				R.navigation.favourites
			),
			fragmentManager = supportFragmentManager,
			containerId = R.id.navHostContainer,
			intent = intent
		)
	}

	fun navigateButtonVisible(isVisible: Boolean) = supportActionBar?.run {
		setDisplayHomeAsUpEnabled(isVisible)
		setDisplayShowHomeEnabled(isVisible)
	}

	override fun onSupportNavigateUp() = run {
		onBackPressed()
		true
	}
}