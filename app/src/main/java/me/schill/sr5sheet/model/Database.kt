package me.schill.sr5sheet.model

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.fasterxml.jackson.annotation.JsonProperty
import me.schill.sr5sheet.persistence.Entity

class Database : Entity() {
	@JsonProperty
	val attributes: ObservableList<Attribute> = ObservableArrayList()
}
