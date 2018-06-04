package me.schill.sr5sheet

import android.databinding.DataBindingUtil
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
import me.schill.sr5sheet.databinding.FragmentDatabaseAttributesBinding
import me.schill.sr5sheet.model.AttributeType
import me.schill.sr5sheet.model.Database
import me.schill.sr5sheet.persistence.Persistence
import me.schill.sr5sheet.persistence.Ref

class DatabaseAttributesFragment :
		EntityFragment<Database>(Database::class.java) {
	override val title: String
		get() {
			return getString(R.string.database_attributes_fragment_title)
		}

	lateinit var binding: FragmentDatabaseAttributesBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_database_attributes, container, false)
		binding.setVariable(BR.entity, entity)
		binding.setVariable(BR.fm, this)

		entity.attributes.forEach({ addAttributeRow(it.get()) })
		entity.attributes.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<AttributeType>>() {
			override fun onChanged(sender: ObservableList<AttributeType>?) {
				binding.table.removeAllViewsInLayout()
				entity.attributes.forEach({
					addAttributeRow(it.get())
				})
			}

			override fun onItemRangeRemoved(sender: ObservableList<AttributeType>?, positionStart: Int, itemCount: Int) {
				binding.table.removeViewsInLayout(positionStart, itemCount)
			}

			override fun onItemRangeMoved(sender: ObservableList<AttributeType>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
				onChanged(sender)
			}

			override fun onItemRangeInserted(sender: ObservableList<AttributeType>?, positionStart: Int, itemCount: Int) {
				var index = positionStart
				sender?.subList(positionStart, positionStart + itemCount)?.forEach {
					val attrBind = DbAttributeRowBinding.inflate(layoutInflater, binding.table, false)
					attrBind.attr = it
					binding.table.addView(attrBind.root, index)
					index++
				}
			}

			override fun onItemRangeChanged(sender: ObservableList<AttributeType>?, positionStart: Int, itemCount: Int) {
				var index = positionStart
				binding.table.removeViewsInLayout(positionStart, itemCount)
				sender?.subList(positionStart, positionStart + itemCount)?.forEach {
					val attrBind = DbAttributeRowBinding.inflate(layoutInflater, binding.table, false)
					attrBind.attr = it
					binding.table.addView(attrBind.root, index)
					index++
				}
			}
		})
		return binding.root
	}

	private fun addAttributeRow(attribute: AttributeType) {
		val attrBind = DbAttributeRowBinding.inflate(layoutInflater, binding.table, true)
		attrBind.attr = attribute
		Log.i(this.javaClass.simpleName, "added attribute " + attribute.name)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.database_attributes_fragment, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == R.id.action_add_attribute) {
			addAttribute()
			return true
		}
		return false
	}

	companion object {
		@JvmStatic
		fun newInstance(database: Database) =
				DatabaseAttributesFragment().apply {
					arguments = Bundle().apply {
						putString(ARG_ID, database.id.toString())
					}
				}
	}

	private fun addAttribute() {
		val builder = AlertDialog.Builder(requireContext());
		builder.setTitle(getString(R.string.database_attributes_new_attribute));

		val layout = LinearLayout(requireContext())
		layout.orientation = LinearLayout.VERTICAL

		val name = EditText(requireContext());
		name.setInputType(InputType::TYPE_CLASS_TEXT.get())
		name.hint = getString(R.string.attribute_name)
		layout.addView(name);

		val attr = AttributeType()

		var ok = false
		val watcher = object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				attr.name = name.text.toString().trim()

				ok = true
				entity.attributes.forEach {
					if (attr.name.isEmpty()) {
						ok = false
					} else if (attr.name.toLowerCase() == it.get().name.toLowerCase()) {
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
				entity.attributes.add(Ref(attr))
				Persistence.save(attr)
				Persistence.save(entity)
			}
		}
		builder.setNegativeButton("Cancel") { dialog, _ -> dialog?.cancel(); }

		builder.show();
	}
}
