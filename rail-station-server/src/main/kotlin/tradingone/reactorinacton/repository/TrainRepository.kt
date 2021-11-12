package tradingone.reactorinacton.repository

import java.util.UUID
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import tradingone.reactorinacton.entity.Train

interface TrainRepository : ReactiveMongoRepository<Train, UUID> {
}