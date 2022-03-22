package com.limon.vocabook.service.dto

class Response {
    class CodeResponse(code:Int, message:String) {
        private val code:Int
        private val message:String

        init {
            this.code = code
            this.message = message
        }

        fun getCode():Int = code
        fun getMessage():String = message
    }

    class AnswerResponse(answer:Int, wrong:Int) {
        val answer:Int
        val wrong:Int

        init {
            this.answer = answer
            this.wrong = wrong
        }
    }
}