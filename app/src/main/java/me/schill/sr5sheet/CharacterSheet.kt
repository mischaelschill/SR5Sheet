package me.schill.sr5sheet

import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_character_sheet.*
import kotlinx.android.synthetic.main.app_bar_character_sheet.*
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.View
import android.widget.EditText
import me.schill.sr5sheet.databinding.ActivityCharacterSheetBinding
import me.schill.sr5sheet.databinding.ContentCharacterSheetBinding


class CharacterSheet : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val cm = CharacterManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cm.init(applicationContext.filesDir);
        val binding = DataBindingUtil.setContentView(this, R.layout.activity_character_sheet) as ActivityCharacterSheetBinding
        binding.setJoeInContent(cm.current)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    fun onLongClickOnField(view: View) {
        var title: String
        var current:  String
        var setter: (String) -> Unit
        when (view.tag) {
            "name" -> {
                title = "Name"
                current = cm.current.name
                setter = {value -> cm.current.name = value}
            }
            "metatype" -> {
                title = "Metatyp"
                current = cm.current.metatype
                setter = {value -> cm.current.metatype = value}
            }
            "ethnicity" -> {
                title = "Ethnie"
                current = cm.current.ethnicity
                setter = {value -> cm.current.ethnicity = value}
            }
            else -> {
                return;
            }
        }
        val builder = AlertDialog.Builder(this);
        builder.setTitle(title);

        // Set up the input
        val input = EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType::TYPE_CLASS_TEXT.get())
        input.setText(current)
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                setter(input.getText().toString().replace("/", "-"));
                cm.save()
            }
        })
        builder.setNegativeButton("Cancel", object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.cancel();
            }
        })

        builder.show();
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
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
