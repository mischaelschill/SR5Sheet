package me.schill.sr5sheet.model

import android.databinding.ObservableArrayList
import com.fasterxml.jackson.annotation.JsonProperty
import me.schill.sr5sheet.persistence.Entity
import me.schill.sr5sheet.persistence.Ref

class Database : Entity() {
	@JsonProperty
	val attributes = ObservableArrayList<Ref<AttributeType>>()

	@JsonProperty
	val skills = ObservableArrayList<Ref<SkillType>>()

	@JsonProperty
	val characterProperties = ObservableArrayList<Ref<Property>>()
}
