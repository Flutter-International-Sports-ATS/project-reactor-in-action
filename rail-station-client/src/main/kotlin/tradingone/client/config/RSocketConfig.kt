package tradingone.client.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies


@Configuration
class RSocketConfig {
    
    @Bean
    fun rSocketRequester(
        rSocketRequesterBuilder: RSocketRequester.Builder
    ): RSocketRequester {
        return rSocketRequesterBuilder
            .rsocketStrategies(rSocketStrategies())
            .tcp("localhost", 7000)
    }

    @Bean
    fun rSocketStrategies(): RSocketStrategies {
        return RSocketStrategies.builder()
            .encoders { encoders -> encoders.add(Jackson2JsonEncoder()) }
            .decoders { decoders -> decoders.add(Jackson2JsonDecoder()) }
            .build()
    }

}