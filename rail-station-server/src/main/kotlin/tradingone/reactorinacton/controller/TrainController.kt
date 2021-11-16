package tradingone.reactorinacton.controller

import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tradingone.reactorinacton.domain.TrainScheduleResponse
import tradingone.reactorinacton.service.TrainService
import java.time.Duration
import java.util.*

@Controller
class TrainController(val trainService: TrainService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @MessageMapping("train-request-response")
    fun getTrainScheduleById(@RequestParam id: UUID): Mono<TrainScheduleResponse> {
        return trainService.getTrainScheduleById(id)
    }

    @MessageMapping("persist-train")
    fun saveSchedule(@RequestParam cityName: String): Mono<Void> {
        return trainService.addCity(cityName)
    }

    @MessageMapping("fire-and-forget")
    fun fireAndForget(@RequestParam sportName: String) {
        logger.info("Received fire and forget request: {}", sportName)
    }

    @MessageMapping("train-stream")
    fun getTrainScheduleStream(scheduleNumber: List<Long>): Flux<TrainScheduleResponse>? {
        return trainService.getTrainScheduleStream(scheduleNumber);
    }

    @MessageMapping("train-channel")
    fun getTrainScheduleChannel(requests: Flux<Duration>): Flux<TrainScheduleResponse> {
        return trainService.getTrainScheduleChannel(requests)
    }
}