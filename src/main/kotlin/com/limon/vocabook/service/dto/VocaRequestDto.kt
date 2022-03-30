package com.limon.vocabook.service.dto

class VocaRequestDto {
    class AddVocaDto {
        lateinit var en:String
            private set
        lateinit var ko:String
            private set

        constructor()
        constructor(en:String, ko:String) {
            this.en = en
            this.ko = ko
        }
    }

    class CategoryDto {
        var start:Int = 0
            private set
        var end:Int = 0
            private set

        constructor()
        constructor(start:Int, end:Int) {
            this.start = start
            this.end = end
        }
    }
}