package com.limon.vocabook.data.builder

import com.limon.vocabook.data.Part
import com.limon.vocabook.data.entity.Voca

class VocaBuilder {
    private lateinit var en: String
    private lateinit var ko: String
    private lateinit var part: Part

    fun setEn(en: String): VocaBuilder {
        this.en = en
        return this
    }

    fun setKo(ko: String): VocaBuilder {
        this.ko = ko
        return this
    }

    fun setPart(part: Part): VocaBuilder {
        this.part = part
        return this
    }

    fun build() = Voca(en = en, ko = ko, part = part)
}