package tradingone.reactorinacton.repository

import java.util.UUID
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import tradingone.reactorinacton.entity.Train

interface TrainRepository : ReactiveMongoRepository<Train, UUID> {

    override fun findById(id: UUID): Mono<Train>
}