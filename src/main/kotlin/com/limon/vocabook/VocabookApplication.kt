package com.limon.vocabook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VocabookApplication

fun main(args: Array<String>) {
	runApplication<VocabookApplication>(*args)
}
