package com.limon.vocabook.controller

import com.limon.vocabook.service.VocaService
import com.limon.vocabook.service.dto.Response.AnswerResponse
import com.limon.vocabook.service.dto.VocaRequestDto.AddVocaDto
import com.limon.vocabook.service.dto.VocaResponseDto.VocaListResponseDto
import com.limon.vocabook.service.dto.VocaResponseDto.VocaQuizListResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class MyVocaController(
    @Autowired private val vocaService: VocaService
) {
    private val nowAnswer = mutableMapOf(ANSWER_CODE to "", ANSWER_EN to "")
    private val wrongList = mutableListOf<VocaListResponseDto>()
    // Go Page
    @GetMapping("/myVoca")
    fun myVoca(model: Model):String {
        val vocaList = vocaService.getAllVoca()
        model.addAttribute("vocaList", vocaList)
        return "myVoca"
    }

    @GetMapping("/vocaQuiz")
    fun vocaQuiz(model:Model):String {
        wrongList.clear()
        return "vocaQuiz"
    }

    @GetMapping("/viewResult")
    fun viewResult(model: Model):String {
        model.addAttribute("wrongList", wrongList)
        return "viewResult"
    }

    // Process
    @PostMapping("/saveWord")
    @ResponseBody
    fun saveWord(@RequestBody vocaDto: AddVocaDto): String {
        val response = vocaService.addVoca(vocaDto)
        return response.getMessage().split(" ")[1]
    }

    @PostMapping("/deleteWord/{id}")
    @ResponseBody
    fun deleteWord(@PathVariable id: String): String {
        val response = vocaService.deleteVoca(id.toLong())
        return response.getMessage().split(" ")[1]
    }

    @PostMapping("/getQuizItemAll")
    @ResponseBody
    fun getQuizItemAll():List<VocaQuizListResponseDto> {
        return vocaService.getAllVocaOrderRandom()
    }

    @PostMapping("/getQuizItemWrong")
    @ResponseBody
    fun getQuizItemWrong():List<VocaQuizListResponseDto> {
        return vocaService.getWrongVocaOrderRandom()
    }

    @PostMapping("/getQuizItem/{en}")
    @ResponseBody
    fun getQuizItem(@PathVariable en: String): List<String> {
        val result = vocaService.getQuizItem(en)
        nowAnswer[ANSWER_CODE] = result.last()
        nowAnswer[ANSWER_EN] = en
        println(nowAnswer)
        return result.dropLast(1)
    }

    @PostMapping("/checkAnswer/{answer}")
    @ResponseBody
    fun checkAnswer(@PathVariable answer: String):AnswerResponse {
        val intAnswer = answer.toInt()
        return if(answer == nowAnswer[ANSWER_CODE]){
            AnswerResponse(intAnswer, intAnswer)
        } else {
            wrongList.add(vocaService.getVocaByEn(nowAnswer[ANSWER_EN]?:""))
            vocaService.updateErrorCnt(nowAnswer[ANSWER_EN])
            AnswerResponse(nowAnswer[ANSWER_CODE]?.toInt() ?: -1, intAnswer)
        }
    }

    companion object {
        private const val ANSWER_CODE = "answer_code"
        private const val ANSWER_EN = "answer_en"
    }
}