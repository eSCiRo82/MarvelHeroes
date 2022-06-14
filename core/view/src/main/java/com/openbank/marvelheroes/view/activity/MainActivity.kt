package com.openbank.marvelheroes.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.openbank.marvelheroes.view.R
import com.openbank.marvelheroes.view.databinding.ActivityMainBinding

/**
 * Main activity of the application
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navigationView
        val navController = findNavController(R.id.fragment_content)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.heroes_galley_option, R.id.search_heroes_option
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_content)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * Navigates to the detail screen passing the identifier of the character to show
     */
    fun navigateToDetail(characterId : Int) {
        findNavController(R.id.fragment_content).apply {
            val args = Bundle().apply {
                putInt("characterId", characterId)
            }
            navigate(R.id.to_detail_option, args)
        }
    }
}