package me.schill.sr5sheet

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.schill.sr5sheet.persistence.Entity
import me.schill.sr5sheet.persistence.Persistence
import java.util.*

abstract class EntityFragment<T : Entity, B : ViewDataBinding>(val entityType: Class<T>, @LayoutRes val layoutId: Int) : TitledFragment() {
	lateinit var entity: T
		private set
	lateinit var binding: B
		private set
	private var loadedCallbacks = ArrayList<(EntityFragment<T, B>) -> Unit>()
	private var loaded = false
		set(value) {
			if (value) {
				loadedCallbacks.forEach {
					it(this)
				}
				loadedCallbacks.clear()
			}
			field = value
		}

	companion object {
		const val ARG_ID = "entityId"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			it.getString(ARG_ID)?.let {
				entity = Persistence.load(UUID.fromString(it), entityType);
				loaded = true
			}
		}
		if (!loaded) {
			entity = entityType.newInstance()
			loaded = true
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		// Inflate the layout for this fragment
		binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
		binding.setVariable(BR.entity, entity)
		binding.setVariable(BR.fm, this)
		return binding.root
	}


	fun onLoaded(callback: (EntityFragment<T, B>) -> Unit) {
		if (loaded) {
			callback(this)
		} else {
			loadedCallbacks.add(callback)
		}
	}
}