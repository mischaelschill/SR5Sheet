package me.schill.sr5sheet

import android.content.Context
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import me.schill.sr5sheet.databinding.DbAttributeRowBinding
import me.schill.sr5sheet.databinding.FragmentDatabaseBinding
import me.schill.sr5sheet.model.Attribute
import me.schill.sr5sheet.model.Database
import me.schill.sr5sheet.persistence.Persistence

class DatabaseFragment :
		EntityFragment<Database, FragmentDatabaseBinding>(Database::class.java, R.layout.fragment_database) {
	override val title: String
		get() {
			return getString(R.string.database_attributes_fragment_title)
		}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val result = super.onCreateView(inflater, container, savedInstanceState)
		entity.attributes.forEach({
			addAttributeRow(it)
		})
		entity.attributes.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<Attribute>>() {
			override fun onChanged(sender: ObservableList<Attribute>?) {
				binding.attributes.removeAllViewsInLayout()
				entity.attributes.forEach({
					addAttributeRow(it)
				})
			}

			override fun onItemRangeRemoved(sender: ObservableList<Attribute>?, positionStart: Int, itemCount: Int) {
				binding.attributes.removeViewsInLayout(positionStart, itemCount)
			}

			override fun onItemRangeMoved(sender: ObservableList<Attribute>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
				onChanged(sender)
			}

			override fun onItemRangeInserted(sender: ObservableList<Attribute>?, positionStart: Int, itemCount: Int) {
				var index = positionStart
				sender?.subList(positionStart, positionStart + itemCount)?.forEach {
					val attrBind = DbAttributeRowBinding.inflate(layoutInflater, binding.attributes, false)
					attrBind.attr = it
					binding.attributes.addView(attrBind.root, index)
					index++
				}
			}

			override fun onItemRangeChanged(sender: ObservableList<Attribute>?, positionStart: Int, itemCount: Int) {
				var index = positionStart
				binding.attributes.removeViewsInLayout(positionStart, itemCount)
				sender?.subList(positionStart, positionStart + itemCount)?.forEach {
					val attrBind = DbAttributeRowBinding.inflate(layoutInflater, binding.attributes, false)
					attrBind.attr = it
					binding.attributes.addView(attrBind.root, index)
					index++
				}
			}
		})
		return result;
	}

	private fun addAttributeRow(attribute: Attribute) {
		val attrBind = DbAttributeRowBinding.inflate(layoutInflater, binding.attributes, true)
		attrBind.attr = attribute
		Log.i("EditDatabase", "added attribute " + attribute.name)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		//super.onCreateOptionsMenu(menu, inflater)
		inflater?.inflate(R.menu.database_attributes_fragment, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == R.id.action_add_attribute) {
			addAttribute()
			return true
		}
		return false
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
	}

	override fun onDetach() {
		super.onDetach()
	}

	companion object {
		@JvmStatic
		fun newInstance(database: Database) =
				DatabaseFragment().apply {
					arguments = Bundle().apply {
						putString(ARG_ID, database.id.toString())
					}
				}
	}

	fun addAttribute() {
		val builder = AlertDialog.Builder(requireContext());
		builder.setTitle("Neues Attribut");

		val layout = LinearLayout(requireContext())
		layout.orientation = LinearLayout.VERTICAL

		val name = EditText(requireContext());
		name.setInputType(InputType::TYPE_CLASS_TEXT.get())
		name.hint = "Name"
		layout.addView(name);

		val code = EditText(requireContext());
		code.setInputType(InputType::TYPE_CLASS_TEXT.get())
		code.hint = "Kürzel"

		val attr = Attribute()

		var ok = false
		val watcher = object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				attr.code = code.text.toString().trim().toUpperCase()
				attr.name = name.text.toString().trim()

				ok = true
				entity.attributes.forEach {
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
				entity.attributes.add(attr)
				Persistence.save(entity)
			}
		}
		builder.setNegativeButton("Cancel") { dialog, _ -> dialog?.cancel(); }

		builder.show();
	}
}
