package com.limon.vocabook.data.entity

import com.limon.vocabook.data.Part
import javax.persistence.*

@Entity
class Voca(en:String, ko:String, part: Part) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
        protected set

    @Column(nullable = false)
    var en: String = en
        protected set

    @Column(nullable = false)
    var ko: String = ko
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var part: Part = part
        protected set

    @Column(nullable = false)
    var wrong: Int = 0
        protected set

    override fun toString(): String {
        return "Voca(ID:$id, EN:$en, KO:$ko, PART:$part, Wrong:$wrong)"
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Voca

        if(other.id != id)  return false

        return true
    }
}