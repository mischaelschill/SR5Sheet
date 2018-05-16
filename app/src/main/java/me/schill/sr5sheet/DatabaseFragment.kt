package me.schill.sr5sheet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.schill.sr5sheet.databinding.FragmentDatabaseBinding
import me.schill.sr5sheet.model.Database

class DatabaseFragment :
		EntityFragment<Database, FragmentDatabaseBinding>(Database::class.java, R.layout.fragment_database) {

	override val title: String
		get() {
			return getString(R.string.database_fragment_title)
		}

	private class PagerAdapter(fm: FragmentManager?, val entity: Database) : FragmentPagerAdapter(fm) {
		override fun getItem(position: Int): Fragment {
			when (position) {
				0 -> {
					return DatabaseAttributesFragment.newInstance(entity)
				}
				1 -> {
					return DatabaseSkillsFragment.newInstance(entity)
				}
			}
			throw IllegalArgumentException("position: " + position)
		}

		override fun getCount() = 2

		override fun getPageTitle(position: Int): CharSequence? {
			when (position) {
				0 -> {
					return "Attribute"
				}
				1 -> {
					return "Fertigkeiten"
				}
			}
			throw IllegalArgumentException("position: " + position)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val result = super.onCreateView(inflater, container, savedInstanceState)
		binding.pager.adapter = PagerAdapter(childFragmentManager, entity)
		return result
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
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
}
