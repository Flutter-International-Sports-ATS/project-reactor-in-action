package tradingone.client.controller

import org.springframework.http.MediaType
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tradingone.client.domain.TrainScheduleResponse
import tradingone.client.domain.TrainScheduleStreamRequest
import tradingone.client.service.RSocketClient

@RestController
class TrainController(val rSocketRequester: RSocketRequester, private val rSocketClient: RSocketClient) {

    @GetMapping("/train-schedule/{id}")
    fun getTrainSchedule(@PathVariable id: Long): Mono<TrainScheduleResponse> {
        return rSocketClient.rSocketMono(
            rSocketRequester,
            "train-request-response",
            id,
            TrainScheduleResponse::class.java
        )
    }

    @PostMapping("/train-fire-and-forget/{id}")
    fun getTrainFireAndForget(@PathVariable id: Long): Mono<Void> {
        return rSocketClient.rSocketMono(
            rSocketRequester,
            "fire-and-forget",
            id,
            Void::class.java
        )
    }

    @GetMapping("/train-stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getTrainStream(): Flux<TrainScheduleResponse> {
        return rSocketClient.rSocketFlux(
            rSocketRequester,
            "train-stream",
            TrainScheduleStreamRequest(listOf(23, 12, 3, 5, 10)),
            TrainScheduleResponse::class.java,
            null
        )
    }

    @GetMapping(value = ["/train-channel"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getTrainChannel(): Flux<TrainScheduleResponse> {
        return rSocketClient.rSocketChannel(rSocketRequester, "train-channel", TrainScheduleResponse::class.java)
    }

    @PostMapping("/add-train-schedule/{cityName}")
    fun saveSchedule(@PathVariable cityName: String): Mono<Void> {
        return rSocketClient.rSocketMono(
            rSocketRequester,
            "persist-train",
            cityName,
            Void::class.java
        )
    }
}