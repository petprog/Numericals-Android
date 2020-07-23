package com.foreverrafs.numericals.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import butterknife.ButterKnife
import butterknife.OnClick
import com.foreverrafs.numericals.BuildConfig
import com.foreverrafs.numericals.R
import com.foreverrafs.numericals.ui.menus.FragmentShowAlgorithm
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.about_dialog.*
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var sheetBehavior: BottomSheetBehavior<*>
    private lateinit var navController: NavController

    private val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.conversion_menu, R.id.interpolation_menu, R.id.location_of_roots_menu, R.id.ode_menu,
                    R.id.sys_of_eqns_menu, R.id.fragment_main_menu
            )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        navController = findNavController(this, R.id.nav_host_fragment)

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet!!)
        tvVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME)

        setSupportActionBar(toolbar)

        setupActionBarWithNavController(navController)

        btnAboutClose.setOnClickListener {
            toggleBottomSheet()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navigateUp(navController, appBarConfiguration)
        return true
    }


    fun toggleBottomSheet() {
        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        else sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
    }

    fun goToMainMenu(button: Button?) {
        val controller = findNavController(button!!)
        val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_left)
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.nav_graph, true)
                .setExitAnim(R.anim.slide_out_right)
                .build()
        controller.navigate(R.id.fragment_main_menu, null, navOptions, null)
    }

    fun showAlgorithm(navController: NavController, methodName: String?) {
        val navOptions = NavOptions.Builder()
                .build()
        val bundle = Bundle()
        bundle.putString(FragmentShowAlgorithm.EXTRA_METHOD_NAME, methodName)
        navController.navigate(R.id.show_algorithm, bundle, navOptions, null)
    }

    override fun onBackPressed() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) toggleBottomSheet() else super.onBackPressed()
    }

    //tvWebsite is a textview on the bottomsheet
    @OnClick(R.id.tvWebsite)
    fun onWebsiteClicked(url: TextView) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url.text.toString())
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.gotot) {
            MaterialAlertDialogBuilder(this)
                    .setTitle("Jump to Algorithm")
                    .setItems(R.array.main_menu_legacy) { _, which ->
                        Timber.d("onOptionsItemSelected: $which")
                    }.show()
        } else if (item.itemId == android.R.id.home) {
            onSupportNavigateUp()
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
}

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}