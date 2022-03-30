package com.limon.vocabook.service.dto

import com.limon.vocabook.data.entity.Voca

class VocaResponseDto {
    class VocaListResponseDto {
        var id: Long = 0L
            private set
        lateinit var en: String
            private set
        lateinit var ko: String
            private set
        var wrong: Int = 0
            private set

        constructor()
        constructor(voca: Voca) {
            this.id = voca.id
            this.en = voca.en
            this.ko = voca.ko
            this.wrong = voca.wrong
        }

        override fun toString(): String {
            return "(id:$id en:$en ko:$ko wrong:$wrong)"
        }
    }

    class VocaQuizListResponseDto {
        var id: Long = 0L
            private set
        lateinit var en: String
            private set
        var wrong: Int = 0
            private set

        constructor()
        constructor(voca: Voca) {
            this.id = voca.id
            this.en = voca.en
            this.wrong = voca.wrong
        }

        override fun toString(): String {
            return "(id:$id en:$en wrong:$wrong)"
        }
    }

    class CategoryResponseDto {
        lateinit var text:String
            private set

        constructor()
        constructor(value:Int) {
            when(value) {
                0 -> this.text = "직접 추가"
                else -> this.text = "DAY ${value}"
            }
        }
    }
}