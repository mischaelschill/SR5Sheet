package me.schill.sr5sheet

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
import me.schill.sr5sheet.databinding.DbSkillRowBinding
import me.schill.sr5sheet.databinding.FragmentDatabaseSkillsBinding
import me.schill.sr5sheet.model.Database
import me.schill.sr5sheet.model.SkillType
import me.schill.sr5sheet.persistence.Persistence

class DatabaseSkillsFragment :
		EntityFragment<Database, FragmentDatabaseSkillsBinding>(Database::class.java, R.layout.fragment_database_skills) {
	override val title: String
		get() {
			return getString(R.string.database_skills_fragment_title)
		}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val result = super.onCreateView(inflater, container, savedInstanceState)
		entity.skills.forEach(::addSkillRow)
		entity.skills.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<SkillType>>() {
			override fun onChanged(sender: ObservableList<SkillType>?) {
				binding.table.removeAllViewsInLayout()
				entity.skills.forEach({
					addSkillRow(it)
				})
			}

			override fun onItemRangeRemoved(sender: ObservableList<SkillType>?, positionStart: Int, itemCount: Int) {
				binding.table.removeViewsInLayout(positionStart, itemCount)
			}

			override fun onItemRangeMoved(sender: ObservableList<SkillType>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
				onChanged(sender)
			}

			override fun onItemRangeInserted(sender: ObservableList<SkillType>?, positionStart: Int, itemCount: Int) {
				var index = positionStart
				sender?.subList(positionStart, positionStart + itemCount)?.forEach {
					val attrBind = DbSkillRowBinding.inflate(layoutInflater, binding.table, false)
					attrBind.row = it
					binding.table.addView(attrBind.root, index)
					index++
				}
			}

			override fun onItemRangeChanged(sender: ObservableList<SkillType>?, positionStart: Int, itemCount: Int) {
				var index = positionStart
				binding.table.removeViewsInLayout(positionStart, itemCount)
				sender?.subList(positionStart, positionStart + itemCount)?.forEach {
					val attrBind = DbSkillRowBinding.inflate(layoutInflater, binding.table, false)
					attrBind.row = it
					binding.table.addView(attrBind.root, index)
					index++
				}
			}
		})
		return result;
	}

	private fun addSkillRow(skill: SkillType) {
		val attrBind = DbSkillRowBinding.inflate(layoutInflater, binding.table, true)
		attrBind.row = skill
		Log.i(this.javaClass.simpleName, "added skill " + skill.name)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.database_skills_fragment, menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == R.id.action_add_attribute) {
			addSkill()
			return true
		}
		return false
	}

	companion object {
		@JvmStatic
		fun newInstance(database: Database) =
				DatabaseSkillsFragment().apply {
					arguments = Bundle().apply {
						putString(ARG_ID, database.id.toString())
					}
				}
	}

	private fun addSkill() {
		val builder = AlertDialog.Builder(requireContext());
		builder.setTitle(getString(R.string.database_skills_new_skill));

		val layout = LinearLayout(requireContext())
		layout.orientation = LinearLayout.VERTICAL

		val name = EditText(requireContext());
		name.setInputType(InputType::TYPE_CLASS_TEXT.get())
		name.hint = getString(R.string.skill_name)
		layout.addView(name);

		val skill = SkillType()

		var ok = false
		val watcher = object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				skill.name = name.text.toString().trim()

				ok = true
				entity.skills.forEach {
					if (skill.name.isEmpty()) {
						ok = false
					} else if (skill.name.toLowerCase() == it.name.toLowerCase()) {
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
				entity.skills.add(skill)
				Persistence.save(skill)
				Persistence.save(entity)
			}
		}
		builder.setNegativeButton("Cancel") { dialog, _ -> dialog?.cancel(); }

		builder.show();
	}
}
