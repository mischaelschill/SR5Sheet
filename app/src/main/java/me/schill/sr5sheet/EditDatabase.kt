package me.schill.sr5sheet

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_edit_database.*
import kotlinx.android.synthetic.main.app_bar_edit_database.*
import kotlinx.android.synthetic.main.app_bar_edit_database.view.*
import kotlinx.android.synthetic.main.content_edit_database.view.*
import me.schill.sr5sheet.databinding.ActivityEditDatabaseBinding
import me.schill.sr5sheet.databinding.DbAttributeRowBinding
import me.schill.sr5sheet.databinding.NavHeaderEditDatabaseBinding
import me.schill.sr5sheet.model.Attribute

class EditDatabase : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
	val dbm = DatabaseManager()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		dbm.init(applicationContext.filesDir);
		val binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_database) as ActivityEditDatabaseBinding
		binding.setDb(dbm.database)

		fab.setOnClickListener { view ->
			Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show()
		}

		val toggle = ActionBarDrawerToggle(
				this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
		drawer_layout.addDrawerListener(toggle)
		toggle.syncState()

		val bind = DataBindingUtil.inflate<NavHeaderEditDatabaseBinding>(layoutInflater, R.layout.nav_header_edit_database, binding.navView, false)
		binding.navView.addHeaderView(bind.getRoot())

		binding.navView.setNavigationItemSelectedListener(this)

		dbm.database.attributes.forEach({
			val attrBind = DataBindingUtil.inflate<DbAttributeRowBinding>(layoutInflater, R.layout.db_attribute_row, binding.drawerLayout.content.attributes, true)
			attrBind.attr = it
			Log.i("EditDatabase", "added attribute " + it.name)
		})
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
		menuInflater.inflate(R.menu.edit_database, menu)
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
		when (item.itemId) {
			R.id.nav_character -> {
				val intent = Intent(this, CharacterSheet::class.java);
				startActivity(intent)
			}
			R.id.nav_share -> {

			}
			R.id.nav_send -> {

			}
		}

		drawer_layout.closeDrawer(GravityCompat.START)
		return true
	}

	fun addAttribute(view: View) {
		val builder = AlertDialog.Builder(this);
		builder.setTitle("Neues Attribut");

		val layout = LinearLayout(this)
		layout.orientation = LinearLayout.VERTICAL

		val name = EditText(this);
		name.setInputType(InputType::TYPE_CLASS_TEXT.get())
		name.hint = "Name"
		layout.addView(name);

		val code = EditText(this);
		code.setInputType(InputType::TYPE_CLASS_TEXT.get())
		code.hint = "Kürzel"

		val attr = Attribute()

		var ok = false
		val watcher = object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				attr.code = code.text.toString().trim().toUpperCase()
				attr.name = name.text.toString().trim()

				ok = true
				dbm.database.attributes.forEach {
					if (attr.code.isEmpty()) {
						code.hint = "Kürzel"
						ok = false
					} else if (attr.code.toLowerCase() == it.code.toLowerCase()) {
						code.text.clear()
						code.hint = "Kürzel schon vergeben"
						ok = false
					}
					if (attr.name.isEmpty()) {
						code.hint = "Name"
						ok = false
					} else if (attr.name.toLowerCase() == it.name.toLowerCase()) {
						code.text.clear()
						code.hint = "Name schon vergeben"
						ok = false
					}
				}
			}

			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			}
		}

		code.addTextChangedListener(watcher)
		name.addTextChangedListener(watcher)
		layout.addView(code);

		builder.setView(layout)

		// Set up the buttons
		builder.setPositiveButton("OK") { dialog, _ ->
			if (ok) {
				dbm.database.attributes.add(attr)
				dbm.save()
			}
		}
		builder.setNegativeButton("Cancel") { dialog, _ -> dialog?.cancel(); }

		builder.show();
	}
}
