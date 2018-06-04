package me.schill.sr5sheet

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.schill.sr5sheet.databinding.FragmentCharacterBinding
import me.schill.sr5sheet.model.SR5Character

class CharacterFragment : EntityFragment<SR5Character>(SR5Character::class.java) {
	lateinit var binding: FragmentCharacterBinding

	override val title: String
		get() {
			return entity.name
		}

	private class PagerAdapter(fm: FragmentManager?, val entity: SR5Character) : FragmentPagerAdapter(fm) {
		override fun getItem(position: Int): Fragment {
			when (position) {
				0 -> {
					return CharacterPropertiesFragment.newInstance(entity)
				}
			}
			throw IllegalArgumentException("position: " + position)
		}

		override fun getCount() = 1

		override fun getPageTitle(position: Int): CharSequence? {
			when (position) {
				0 -> {
					return "Eigenschaften"
				}
			}
			throw IllegalArgumentException("position: " + position)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_character, container, false)
		binding.setVariable(BR.entity, entity)
		binding.setVariable(BR.fm, this)

		binding.pager.adapter = PagerAdapter(childFragmentManager, entity)
		return binding.root
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
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
}
