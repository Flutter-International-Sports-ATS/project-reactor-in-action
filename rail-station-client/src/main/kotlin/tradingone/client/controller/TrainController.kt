package tradingone.client.controller

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tradingone.client.domain.TrainScheduleResponse
import tradingone.client.domain.TrainScheduleStreamRequest
import tradingone.client.service.RSocketService

@RestController
class TrainController(val rSocketRequester: RSocketRequester, private val rSocketService: RSocketService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/train-schedule/{id}")
    fun getTrainSchedule(@PathVariable id: Long): Mono<TrainScheduleResponse> {
        return rSocketService.rSocketMono(
            rSocketRequester,
            "train-request-response",
            id,
            TrainScheduleResponse::class.java
        )
    }

    @GetMapping("/add-train-schedule/{cityName}")
    fun saveSchedule(@PathVariable cityName: String): Mono<Void> {
        return rSocketService.rSocketMono(
            rSocketRequester,
            "persist-train",
            cityName,
            Void::class.java
        )
    }

    @GetMapping("/train-fire-and-forget/{id}")
    fun getTrainFireAndForget(@PathVariable id: Long): Mono<Void> {
        return rSocketService.rSocketMono(
            rSocketRequester,
            "fire-and-forget",
            id,
            Void::class.java
        )
    }

    @GetMapping("/train-stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getTrainStream(): Flux<TrainScheduleResponse> {
        return rSocketService.rSocketFlux(
            rSocketRequester,
            "train-stream",
            TrainScheduleStreamRequest(listOf(23, 12, 3, 5, 10)),
            TrainScheduleResponse::class.java,
            null
        )
    }

    @GetMapping(value = ["/train-channel"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getTrainChannel(): Flux<TrainScheduleResponse> {
        return rSocketService.rSocketChannel(rSocketRequester, "train-channel", TrainScheduleResponse::class.java)
    }
}