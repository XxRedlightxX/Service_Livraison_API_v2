package com.GaspillageZeroAPI

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class GaspillageZeroApiApplication

fun main(args: Array<String>) {
	runApplication<GaspillageZeroApiApplication>(*args)
}