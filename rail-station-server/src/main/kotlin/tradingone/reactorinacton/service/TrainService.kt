package tradingone.reactorinacton.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tradingone.reactorinacton.domain.TrainScheduleRequest
import tradingone.reactorinacton.domain.TrainScheduleResponse
import java.time.Duration
import java.time.Instant
import kotlin.random.Random

@Service
class TrainService() {

    val departureCities: MutableList<String> = mutableListOf("Berlin", "Madrid", "Kiev")
    val destinationCities: MutableList<String> = mutableListOf("Paris", "Rome", "Vienna")
    private val logger = LoggerFactory.getLogger(javaClass)

    fun getTrainScheduleById(id: Long): Mono<TrainScheduleResponse> {
        logger.info("Getting train schedule by ID: {}", id)
        return Mono.just(
            TrainScheduleResponse(
                getRandomDepartureCity(),
                getRandomDestinationCity(),
                Instant.now()
            )
        )
    }

    fun addCity(cityName: String): Mono<Void> {
        logger.info("adding {} to list", cityName)
        if ((1..100).random() % 2 == 0) {
            departureCities.add(cityName)
            logger.info("List size is {}", departureCities.size)
        } else {
            destinationCities.add(cityName)
            logger.info("List size is {}", destinationCities.size)
        }
        return Mono.empty()
    }

    fun getTrainScheduleStream(scheduleNumber: TrainScheduleRequest): Flux<TrainScheduleResponse>? {
        logger.info("Streaming responses for {} train schedules", scheduleNumber.entries.size)
        return Flux.range(0, scheduleNumber.entries.size)
            .delayElements(Duration.ofSeconds(2)).map {
                TrainScheduleResponse(
                    getRandomDepartureCity(),
                    getRandomDestinationCity(),
                    Instant.now()
                )
            }
    }

    fun getTrainScheduleChannel(requests: Flux<Duration>): Flux<TrainScheduleResponse> {
        return requests
            .doOnNext { setting -> logger.info("Channel frequency setting is {} second(s).", setting.seconds) }
            .doOnCancel { logger.warn("The client cancelled the channel.") }
            .switchMap { setting ->
                Flux.interval(setting)
                    .map {
                        TrainScheduleResponse(
                            getRandomDepartureCity(),
                            getRandomDestinationCity(),
                            Instant.now()
                        )
                    }
            }
    }

    private fun getRandomDestinationCity(): String {
        logger.info("ListSize {}", destinationCities.size)
        return destinationCities[Random.nextInt(0, destinationCities.size)]
    }

    private fun getRandomDepartureCity(): String {
        logger.info("ListSize {}", departureCities.size)
        return departureCities[Random.nextInt(0, departureCities.size)]
    }
}