package com.limon.vocabook.controller

import com.limon.vocabook.service.VocaService
import com.limon.vocabook.service.dto.VocaRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class MyVocaController(
    @Autowired private val vocaService: VocaService
) {
    @GetMapping("/myVoca")
    fun myVoca(model: Model):String {
        val vocaList = vocaService.getAllVoca()
        model.addAttribute("vocaList", vocaList)
        return "myVoca"
    }

    @PostMapping("/saveWord")
    @ResponseBody
    fun saveWord(@RequestBody vocaDto: VocaRequestDto.AddVocaDto): String {
        val response = vocaService.addVoca(vocaDto)
        return response.getMessage().split(" ")[1]
    }

    @PostMapping("/deleteWord/{id}")
    @ResponseBody
    fun deleteWord(@PathVariable id: String): String {
        val response = vocaService.deleteVoca(id.toLong())
        return response.getMessage().split(" ")[1]
    }
}