package me.schill.sr5sheet

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import me.schill.sr5sheet.databinding.CharacterPropertyRowBinding
import me.schill.sr5sheet.databinding.FragmentCharacterPropertiesBinding
import me.schill.sr5sheet.model.Property
import me.schill.sr5sheet.model.SR5Character

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
		val bind = CharacterPropertyRowBinding.inflate(layoutInflater, binding.table, true)
		bind.prop = property
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
		builder.setTitle("Eigenschaft hinzufügen");

		val layout = LinearLayout(requireContext())
		layout.orientation = LinearLayout.VERTICAL

		val name = EditText(requireContext());
		name.setInputType(InputType::TYPE_CLASS_TEXT.get())
		name.hint = getString(R.string.attribute_name)
		layout.addView(name);

		throw NotImplementedError()
		/*
		val attr = AttributeType()

		var ok = false
		val watcher = object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				attr.name = name.text.toString().trim()

				ok = true
				entity.attributes.forEach {
					if (attr.name.isEmpty()) {
						ok = false
					} else if (attr.name.toLowerCase() == it.name.toLowerCase()) {
						ok = false
					}
				}
			}

			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
			}

			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
			}
		}

		name.addTextChangedListener(watcher)

		builder.setView(layout)

		// Set up the buttons
		builder.setPositiveButton("OK") { dialog, _ ->
			if (ok) {
				entity.attributes.add(attr)
				Persistence.save(attr)
				Persistence.save(entity)
			}
		}
		builder.setNegativeButton("Cancel") { dialog, _ -> dialog?.cancel(); }

		builder.show();
		*/
	}
}
