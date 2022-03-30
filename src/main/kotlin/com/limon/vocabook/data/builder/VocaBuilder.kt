package com.limon.vocabook.data.builder

import com.limon.vocabook.data.entity.Voca

class VocaBuilder {
    private lateinit var en: String
    private lateinit var ko: String
    private var category:Int = 0

    fun setEn(en: String): VocaBuilder {
        this.en = en
        return this
    }

    fun setKo(ko: String): VocaBuilder {
        this.ko = ko
        return this
    }

    fun setCategory(category:Int): VocaBuilder {
        this.category = category
        return this
    }

    fun build() = Voca(en = en, ko = ko, category = category)
}