package com.limon.vocabook.data.entity

import javax.persistence.*

@Entity
class Voca(en:String, ko:String, category:Int) {
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

    @Column(nullable = false)
    var category: Int = category
        protected set

    @Column(nullable = false)
    var wrong: Int = 0
        protected set

    override fun toString(): String {
        return "Voca(ID:$id, EN:$en, KO:$ko, Wrong:$wrong)"
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