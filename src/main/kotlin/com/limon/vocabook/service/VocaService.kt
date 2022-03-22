package com.limon.vocabook.service

import com.limon.vocabook.data.builder.VocaBuilder
import com.limon.vocabook.data.entity.Voca
import com.limon.vocabook.data.entity.VocaRepository
import com.limon.vocabook.service.dto.Response.CodeResponse
import com.limon.vocabook.service.dto.VocaRequestDto.*
import com.limon.vocabook.service.dto.VocaResponseDto.VocaQuizListResponseDto
import com.limon.vocabook.service.dto.VocaResponseDto.VocaListResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class VocaService(
    @Autowired private val vocaRepo: VocaRepository,
    @Autowired private val jdbcTemplate: JdbcTemplate
) {
    // 기본적인 단어 저장/열람/삭제
    @Transactional
    fun addVoca(dto:AddVocaDto): CodeResponse {
        val voca = VocaBuilder()
            .setEn(dto.en)
            .setKo(dto.ko)
            .setPart(dto.part)
            .build()
        val saved = vocaRepo.save(voca)
        return CodeResponse(200, "[${saved.en}] 저장")
    }

    @Transactional
    fun getVocaByEn(en:String): VocaListResponseDto {
        val voca = vocaRepo.findByEn(en)
            ?: throw Exception("There is no voca")
        return VocaListResponseDto(voca)
    }

    @Transactional
    fun getAllVoca(): List<VocaListResponseDto> {
        return vocaRepo.findAll().stream()
            .map { voca -> VocaListResponseDto(voca) }
            .collect(Collectors.toList())
    }

    @Transactional
    fun deleteVoca(id:Long):CodeResponse {
        vocaRepo.deleteById(id)
        val vocaList:List<Voca> = vocaRepo.findAll()
        val max = vocaList.size
        for(i:Int in 1..max) {
            if(vocaList[i-1].id != i.toLong()){
                jdbcTemplate.update("update voca set id=? where id=?", i.toLong(), vocaList[i-1].id)
            }
        }
        val sql = "ALTER TABLE voca auto_increment=${max+1}"
        jdbcTemplate.execute(sql)
        return CodeResponse(200, "$id 삭제")
    }


    // 단어 퀴즈
    @Transactional
    fun getAllVocaOrderRandom():List<VocaQuizListResponseDto> {
        val vocaList = vocaRepo.findAll().stream()
            .map { voca -> VocaQuizListResponseDto(voca) }
            .collect(Collectors.toList())
        return vocaList.shuffled()
    }

    @Transactional
    fun getWrongVocaOrderRandom():List<VocaQuizListResponseDto> {
        val vocaList = vocaRepo.findByWrong(1).stream()
            .map { voca -> VocaQuizListResponseDto(voca) }
            .collect(Collectors.toList())
        return vocaList.shuffled()
    }

    fun getQuizItem(en: String):List<String>{
        val ko = vocaRepo.findKoByEn(en) ?: ""
        val result = (listOf(ko) + getMean(en)).shuffled()
        return result + listOf(result.indexOf(ko).toString())
    }

    @Transactional
    fun updateErrorCnt(en:String?): VocaListResponseDto {
        val voca = vocaRepo.findByEn(en?:"")
            ?: throw Exception("There is no voca")
        vocaRepo.updateVoca(voca.id, voca.wrong + 1)
        return VocaListResponseDto(vocaRepo.findById(voca.id).get())
    }

    private fun getMean(en: String): List<String> {
        val means = vocaRepo.findByKoExceptEn(en).shuffled()
        val max = if(means.size >= 3) 3 else means.size
        return means.subList(0, max)
    }
}