package tradingone.reactorinacton.entity

import java.time.Instant
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Train (
    @Id val id: UUID,
    val departureCity: String,
    val destinationCity: String,
    val date: Instant
    )