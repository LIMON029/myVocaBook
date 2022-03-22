package com.limon.vocabook.data.entity

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface VocaRepository: JpaRepository<Voca, Long> {
    fun findByEn(en:String):Voca?
    fun findByKo(ko:String):Voca?
    fun existsByEn(en:String):Boolean

    @Query("SELECT v FROM Voca v WHERE v.wrong >= :wrong")
    fun findByWrong(wrong:Int):List<Voca>

    @Query("SELECT v.ko FROM Voca v WHERE v.en != :en")
    fun findByKoExceptEn(en:String):List<String>

    @Query("SELECT v.ko FROM Voca v WHERE v.en = :en")
    fun findKoByEn(en:String):String?

    @Transactional
    @Modifying
    @Query("UPDATE Voca v SET v.wrong = :wrong WHERE v.id = :id")
    fun updateVoca(id:Long, wrong:Int):Int
}