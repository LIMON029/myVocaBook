package com.limon.vocabook.service.dto

import com.limon.vocabook.data.entity.Voca

class VocaResponseDto {
    var id:Long = 0L
        private set
    lateinit var en:String
        private set
    lateinit var ko:String
        private set
    lateinit var part: String
        private set
    var wrong: Int = 0
        private set

    constructor()
    constructor(voca: Voca) {
        this.id = voca.id
        this.en = voca.en
        this.ko = voca.ko
        this.part = voca.part.koValue
        this.wrong = voca.wrong
    }

    override fun toString(): String {
        return "(id:$id en:$en ko:$ko part:$part wrong:$wrong)"
    }
}