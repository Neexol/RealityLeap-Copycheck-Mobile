package ru.rtuitlab.copycheck.ui

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

	fun enableNavigateButton(toolbar: Toolbar) {
		toolbar.setNavigationIcon(R.drawable.ic_arrow)
		toolbar.setNavigationOnClickListener {
			onBackPressed()
			(getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
				?.hideSoftInputFromWindow(it.windowToken, 0)
		}
	}
}