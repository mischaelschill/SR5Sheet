package me.schill.sr5sheet

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_character_sheet.*
import kotlinx.android.synthetic.main.app_bar_character_sheet.*
import me.schill.sr5sheet.databinding.ActivityCharacterSheetBinding
import me.schill.sr5sheet.databinding.NavHeaderCharacterSheetBinding


class CharacterSheet :
		AppCompatActivity(),
		NavigationView.OnNavigationItemSelectedListener {

    val cm = CharacterManager()
	val dm = DatabaseManager()
	var currentNavItem = R.id.nav_character
	lateinit var binding: ActivityCharacterSheetBinding
	var databaseFragment: DatabaseFragment? = null
	var characterFragment: CharacterFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
	    cm.init(applicationContext.filesDir)
	    dm.init(applicationContext.filesDir)

	    binding = DataBindingUtil.setContentView(this, R.layout.activity_character_sheet) as ActivityCharacterSheetBinding
//	    binding = ActivityCharacterSheetBinding.inflate(layoutInflater)
        binding.joeInContent = cm.current
//	    setContentView(binding.root)

	    fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
		        this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
	    drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

	    val bind = NavHeaderCharacterSheetBinding.inflate(layoutInflater, binding.navView, false)
        binding.navView.addHeaderView(bind.getRoot())
	    binding.navView.menu
        bind.joe = cm.current

        binding.navView.setNavigationItemSelectedListener(this)

	    navigate(R.id.nav_character)
    }

    override fun onBackPressed() {
	    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
		    drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.character_sheet, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
	    navigate(item.itemId)

	    drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

	private fun navigate(where: Int) {
		when (where) {
			R.id.nav_database -> {
				val fragmentManager = supportFragmentManager
				val fragmentTransaction = fragmentManager.beginTransaction()
				databaseFragment = DatabaseFragment.newInstance(dm.database)
				fragmentTransaction.replace(R.id.app_bar, databaseFragment)
				fragmentTransaction.commit()
				currentNavItem = R.id.nav_database
				binding.navView.menu.findItem(R.id.nav_character).isEnabled = true
				binding.navView.menu.findItem(R.id.nav_database).isEnabled = false
			}
			R.id.nav_character -> {
				val fragmentManager = supportFragmentManager
				val fragmentTransaction = fragmentManager.beginTransaction()
				characterFragment = CharacterFragment.newInstance(cm.current)
				fragmentTransaction.replace(R.id.app_bar, characterFragment)
				fragmentTransaction.commit()
				currentNavItem = R.id.nav_character
				binding.navView.menu.findItem(R.id.nav_database).isEnabled = true
				binding.navView.menu.findItem(R.id.nav_character).isEnabled = false
			}
			R.id.nav_share -> {

			}
			R.id.nav_send -> {

			}
		}
	}
}
