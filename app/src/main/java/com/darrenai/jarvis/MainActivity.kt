package com.darrenai.jarvis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.darrenai.jarvis.ai.AiService
import com.darrenai.jarvis.ai.HermesProvider
import com.darrenai.jarvis.ui.settings.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var aiService: AiService
    private lateinit var hermesProvider: HermesProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize AI service (singleton)
        aiService = AiService.getInstance(this)
        hermesProvider = HermesProvider(this)

        // Setup navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Bottom nav
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        // Setup toolbar
        val appBarConfig = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.nav_dashboard,
                R.id.nav_chat,
                R.id.nav_voice,
                R.id.nav_schedule,
                R.id.nav_library
            )
        )
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig)
        supportActionBar?.hide()

        // Settings button
        findViewById<android.widget.ImageButton>(R.id.btn_settings_top).setOnClickListener {
            val intent = android.content.Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Update connection status
        updateConnectionStatus()
    }

    private fun updateConnectionStatus() {
        val txtMode = findViewById<android.widget.TextView>(R.id.txt_mode_status)
        val chipProvider = findViewById<Chip>(R.id.chip_provider)

        val isOnline = hermesProvider.isOnlineMode()
        val providerName = hermesProvider.getProviderName()

        txtMode.text = if (isOnline) "Online" else "Offline"
        chipProvider.text = providerName
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
