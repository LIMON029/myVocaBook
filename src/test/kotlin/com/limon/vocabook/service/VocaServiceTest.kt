package com.limon.vocabook.service

import com.limon.vocabook.data.Part
import com.limon.vocabook.data.entity.VocaRepository
import com.limon.vocabook.service.dto.VocaRequestDto.AddVocaDto
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest
internal class VocaServiceTest(
    @Autowired private val vocaRepository: VocaRepository,
    @Autowired private val jdbcTemplate: JdbcTemplate
) {
    private lateinit var vocaService:VocaService

    @BeforeEach
    fun before() {
        vocaService = VocaService(vocaRepository, jdbcTemplate)
    }

    @AfterEach
    fun after() {
        val last = vocaRepository.count()
        vocaRepository.deleteById(last)
        val sql = "ALTER TABLE voca auto_increment=${last}"
        jdbcTemplate.execute(sql)
    }

    @Test
    fun addVoca() {
        val dto = AddVocaDto("add", "추가하다", Part.VERB)
        val response = vocaService.addVoca(dto)
        assertThat(response.getCode()).isEqualTo(200)
    }

    @Test
    fun updateVoca() {
        val dto = AddVocaDto("add", "추가하다", Part.VERB)
        vocaService.addVoca(dto)
        val result = vocaService.updateErrorCnt(1)
        assertThat(result.wrong).isEqualTo(1)
    }

    @Test
    fun getQuizItem() {
        vocaService.addVoca(AddVocaDto("add", "추가하다", Part.VERB))
        vocaService.addVoca(AddVocaDto("test", "시험", Part.NOUN))
        vocaService.addVoca(AddVocaDto("computer", "컴퓨터", Part.NOUN))
        vocaService.addVoca(AddVocaDto("clock", "시계", Part.NOUN))
        vocaService.addVoca(AddVocaDto("one", "일, 하나", Part.NOUN))
        vocaService.addVoca(AddVocaDto("in", "~안에", Part.PREPOSITION))
        val result = vocaService.getQuizItem("test")
        println(result.toString())
    }

    @Test
    fun deleteVoca() {
        val dto1 = AddVocaDto("add1", "추가하다", Part.VERB)
        val dto2 = AddVocaDto("add2", "추가하다", Part.VERB)
        vocaService.addVoca(dto1)
        vocaService.addVoca(dto2)
        vocaService.deleteVoca(1)
        val dto3 = AddVocaDto("add3", "추가하다", Part.VERB)
        val response = vocaService.addVoca(dto3)
        val voca = vocaService.getAllVoca()
        assertThat(response.getCode()).isEqualTo(200)
    }
}