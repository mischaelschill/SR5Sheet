package me.schill.sr5sheet

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import me.schill.sr5sheet.databinding.FragmentCharacterBinding
import me.schill.sr5sheet.model.SR5Character
import me.schill.sr5sheet.persistence.Persistence

class CharacterFragment : EntityFragment<SR5Character, FragmentCharacterBinding>(SR5Character::class.java, R.layout.fragment_character) {
	override val title: String
		get() {
			return entity.name
		}

	override fun onAttach(context: Context) {
		super.onAttach(context)
	}

	override fun onDetach() {
		super.onDetach()
	}


	companion object {
		@JvmStatic
		fun newInstance(character: SR5Character) =
				CharacterFragment().apply {
					arguments = Bundle().apply {
						putString(ARG_ID, character.id.toString())
					}
				}
	}

	fun showDialog(title: String, current: String, setter: (String) -> Unit) {
		val builder = AlertDialog.Builder(requireContext());
		builder.setTitle(title);

		// Set up the input
		val input = EditText(requireContext());
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType::TYPE_CLASS_TEXT.get())
		input.setText(current)
		builder.setView(input)

		// Set up the buttons
		builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
			override fun onClick(dialog: DialogInterface?, which: Int) {
				setter(input.getText().toString().replace("/", "-"));
				Persistence.save(entity)
			}
		})
		builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
			override fun onClick(dialog: DialogInterface?, which: Int) {
				dialog?.cancel();
			}
		})

		builder.show();
	}

	fun changeValueListener(view: View): Unit {
		var result = true
		when (view.tag) {
			"name" -> {
				showDialog("Name", entity.name, { value -> entity.name = value })
			}
			"metatype" -> {
				showDialog("Metatyp", entity.metatype, { value -> entity.metatype = value })
			}
			"ethnicity" -> {
				showDialog("Ethnie", entity.ethnicity, { value -> entity.ethnicity = value })
			}
			"sex" -> {
				showDialog("Geschlecht", entity.sex, { value -> entity.sex = value })
			}
			else -> {
				result = false
			}
		}

	}

	fun editIntegerAttribute(view: View) {
		val title: String
		val current: Int
		val maxValue: Int
		val minValue: Int
		val setter: (Int) -> Unit
		when (view.tag) {
			"age" -> {
				title = "Alter"
				current = entity.age
				minValue = 0
				maxValue = 200
				setter = { value -> entity.age = value }
			}
			"height" -> {
				title = "Grösse"
				current = entity.height
				minValue = 20
				maxValue = 500
				setter = { value -> entity.height = value }
			}
			"weight" -> {
				title = "Gewicht"
				current = entity.weight
				minValue = 1
				maxValue = 500
				setter = { value -> entity.weight = value }
			}
			"streetCred" -> {
				title = "Strassenruf"
				current = entity.streetCred
				minValue = 0
				maxValue = 100
				setter = { value -> entity.streetCred = value }
			}
			"notoriety" -> {
				title = "Schlechter Ruf"
				current = entity.notoriety
				minValue = 0
				maxValue = 100
				setter = { value -> entity.notoriety = value }
			}
			"publicAwareness" -> {
				title = "Prominenz"
				current = entity.publicAwareness
				minValue = 0
				maxValue = 100
				setter = { value -> entity.publicAwareness = value }
			}
			"karma" -> {
				title = "Karma"
				current = entity.karma
				minValue = 0
				maxValue = 1000
				setter = { value -> entity.karma = value }
			}
			"totalKarma" -> {
				title = "Total Karma"
				current = entity.totalKarma
				minValue = 0
				maxValue = 2000
				setter = { value -> entity.totalKarma = value }
			}
			else -> {
				return;
			}
		}
		val builder = AlertDialog.Builder(requireContext());
		builder.setTitle(title);

		// Set up the input
		val input = NumberPicker(requireContext());
		input.minValue = minValue
		input.maxValue = maxValue
		input.value = current
		builder.setView(input)

		// Set up the buttons
		builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
			override fun onClick(dialog: DialogInterface?, which: Int) {
				setter(input.value);
				Persistence.save(entity)
			}
		})
		builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
			override fun onClick(dialog: DialogInterface?, which: Int) {
				dialog?.cancel();
			}
		})

		builder.show();
	}
}
