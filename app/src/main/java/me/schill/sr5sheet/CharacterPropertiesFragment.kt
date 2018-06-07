package me.schill.sr5sheet

import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import me.schill.sr5sheet.databinding.CharacterPropertyRowBinding
import me.schill.sr5sheet.databinding.FragmentCharacterPropertiesBinding
import me.schill.sr5sheet.model.BuiltinDatabase
import me.schill.sr5sheet.model.Property
import me.schill.sr5sheet.model.SR5Character
import me.schill.sr5sheet.persistence.Persistence

class CharacterPropertiesFragment :
		EntityFragment<SR5Character>(SR5Character::class.java) {
	lateinit var binding: FragmentCharacterPropertiesBinding

	override val title: String
		get() {
			return "Charaktereigenschaften"
		}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_character_properties, container, false)
		binding.setVariable(BR.entity, entity)
		binding.setVariable(BR.fm, this)

		entity.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
			override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
				if (propertyId == BR.properties) {
					binding.table.removeAllViewsInLayout()
					entity.properties.forEach({
						addRow(it.get())
					})
				}
			}
		})

		entity.properties.forEach({ addRow(it.get()) })
		return binding.root;
	}

	fun editRow(property: Property): View.OnLongClickListener = View.OnLongClickListener {
		Snackbar.make(it, "Eigenschaft: " + property.name, 2).show();
		true
	}

	private fun addRow(property: Property) {
		val rowBinding = CharacterPropertyRowBinding.inflate(layoutInflater, binding.table, true)
		rowBinding.prop = property
		property.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
			override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
				if (propertyId == BR.removed) {
					binding.table.removeView(rowBinding.root)
				}
			}

		})
		Log.i(this.javaClass.simpleName, "added property " + property.name)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.character_properties_fragment, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == R.id.action_add_property) {
			addItem()
			return true
		}
		return false
	}

	companion object {
		@JvmStatic
		fun newInstance(character: SR5Character) =
				CharacterPropertiesFragment().apply {
					arguments = Bundle().apply {
						putString(ARG_ID, character.id.toString())
					}
				}
	}

	private fun addItem() {
		val builder = AlertDialog.Builder(requireContext());
		builder.setTitle(getString(R.string.add_property));

		val existingProperties = entity.properties.map { it.get().type.id }.toSet()

		val types = BuiltinDatabase.Properties
				.all
				.filterKeys {
					!existingProperties.contains(it)
				}
				.values
				.sortedBy { it.name }

		val typeNames = types.map { it.name }.toTypedArray()

		var selectedPropertyType = types[0]

		builder.setSingleChoiceItems(
				typeNames,
				0,
				{ source: DialogInterface, position: Int ->
					selectedPropertyType = types[position]
				}
		)
		builder.setPositiveButton("OK") { dialog, _ ->
			val instance = selectedPropertyType.createInstance()
			val ref = Persistence.save(instance)
			ref?.let {
				entity.properties += it
			}
		}
		builder.setNegativeButton("Cancel") { dialog, _ -> dialog?.cancel(); }

		builder.show();
	}
}
