package com.limon.vocabook.service.dto

import com.limon.vocabook.data.Part

class VocaRequestDto {
    class AddVocaDto {
        lateinit var en:String
            private set
        lateinit var ko:String
            private set
        lateinit var part:Part
            private set

        constructor()
        constructor(en:String, ko:String, part:Part) {
            this.en = en
            this.ko = ko
            this.part = part
        }
    }
}