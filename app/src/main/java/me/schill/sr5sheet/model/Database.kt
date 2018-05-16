package me.schill.sr5sheet.model

import android.databinding.ObservableArrayList
import com.fasterxml.jackson.annotation.JsonProperty
import me.schill.sr5sheet.persistence.Entity

class Database : Entity() {
	@JsonProperty
	val attributes: ObservableArrayList<AttributeType> = ObservableArrayList()

	@JsonProperty
	val skills: ObservableArrayList<SkillType> = ObservableArrayList()
}
