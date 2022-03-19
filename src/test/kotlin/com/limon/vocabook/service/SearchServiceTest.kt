package com.limon.vocabook.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class SearchServiceTest(
    @Autowired private val searchService: SearchService
) {

    @Test
    fun searchWord() {
        val result = searchService.searchWord("test")
        assertThat(result).isEqualTo("시험")
    }
}