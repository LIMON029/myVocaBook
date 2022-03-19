package com.limon.vocabook.controller

import com.limon.vocabook.service.SearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class MainController(
    @Autowired private val searchService: SearchService
) {
    private var nowWord = mutableMapOf<String, String>()
    @GetMapping("/main")
    fun goMainGet(model:Model):String {
        model.addAttribute("en", nowWord["en"])
        model.addAttribute("ko", nowWord["ko"])
        nowWord.clear()
        return "main"
    }

    @PostMapping("/searchWord/{en}")
    @ResponseBody
    fun searchWord(@PathVariable en: String): String {
        nowWord["en"] = en
        nowWord["ko"] = searchService.searchWord(en)
        return nowWord["ko"] ?: ""
    }
}