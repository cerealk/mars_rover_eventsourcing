package com.lastminute.axon.marsrover

import com.lastminute.axon.marsrover.application.FakeSatelliteSystem
import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@SpringBootApplication
class RoverApplication

fun main( vals :Array<String>) {
    runApplication<RoverApplication>(*vals)
}

@Configuration
class Conf {
//    @Bean
//    fun eventStorageEngine(): EventStorageEngine? {
//        return InMemoryEventStorageEngine()
//    }

    @Bean
    fun satelliteSystem() = FakeSatelliteSystem()

}