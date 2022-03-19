package com.limon.vocabook.service

import com.limon.vocabook.data.builder.VocaBuilder
import com.limon.vocabook.data.entity.Voca
import com.limon.vocabook.data.entity.VocaRepository
import com.limon.vocabook.service.dto.Response
import com.limon.vocabook.service.dto.VocaRequestDto.*
import com.limon.vocabook.service.dto.VocaResponseDto
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
    @Transactional
    fun addVoca(dto:AddVocaDto): Response {
        val voca = VocaBuilder()
            .setEn(dto.en)
            .setKo(dto.ko)
            .setPart(dto.part)
            .build()
        val saved = vocaRepo.save(voca)
        return Response(200, "[${saved.en}] 저장")
    }

    @Transactional
    fun getAllVoca(): List<VocaResponseDto> {
        return vocaRepo.findAll().stream()
            .map { voca -> VocaResponseDto(voca) }
            .collect(Collectors.toList())
    }

    @Transactional
    fun deleteVoca(id:Long):Response {
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
        return Response(200, "$id 삭제")
    }

    fun updateErrorCnt(id:Long): VocaResponseDto {
        val voca = vocaRepo.findById(id).get()
        vocaRepo.updateVoca(id, voca.wrong + 1)
        return VocaResponseDto(vocaRepo.findById(id).get())
    }

    fun getQuizItem(en: String):List<String>{
        return listOf(vocaRepo.findKoByEn(en) ?: "") + getMean(en)
    }

    private fun getMean(en: String): List<String> {
        val means = vocaRepo.findByKoExceptEn(en).shuffled()
        val max = if(means.size >= 3) 3 else means.size
        return means.subList(0, max)
    }
}