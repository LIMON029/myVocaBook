package com.limon.vocabook.service

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
        val dto = AddVocaDto("add", "추가하다")
        val response = vocaService.addVoca(dto)
        assertThat(response.getCode()).isEqualTo(200)
    }

    @Test
    fun updateVoca() {
        val dto = AddVocaDto("add", "추가하다")
        vocaService.addVoca(dto)
        val result = vocaService.updateErrorCnt("add")
        assertThat(result.wrong).isEqualTo(1)
    }

    @Test
    fun getQuizItem() {
        vocaService.addVoca(AddVocaDto("add", "추가하다"))
        vocaService.addVoca(AddVocaDto("test", "시험"))
        vocaService.addVoca(AddVocaDto("computer", "컴퓨터"))
        vocaService.addVoca(AddVocaDto("clock", "시계"))
        vocaService.addVoca(AddVocaDto("one", "일, 하나"))
        vocaService.addVoca(AddVocaDto("in", "~안에"))
        val result = vocaService.getQuizItem("test")
        println(result.toString())
    }

    @Test
    fun deleteVoca() {
        val dto1 = AddVocaDto("add1", "추가하다")
        val dto2 = AddVocaDto("add2", "추가하다")
        vocaService.addVoca(dto1)
        vocaService.addVoca(dto2)
        vocaService.deleteVoca(1)
        val dto3 = AddVocaDto("add3", "추가하다")
        val response = vocaService.addVoca(dto3)
        val voca = vocaService.getAllVoca()
        assertThat(response.getCode()).isEqualTo(200)
    }
}