package tradingone.client.service

import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.function.Consumer

@Service
class RSocketClient {

    fun <T> rSocketFlux(
        rSocketRequester: RSocketRequester,
        apiMethod: String,
        data: Any,
        returnClass: Class<out Any?>,
        metadata: Map<String, Any>?
    ): Flux<T> {
        return getRequestSpec(
            rSocketRequester,
            apiMethod,
        ).data(data)
            .retrieveFlux(returnClass)
            .doOnNext { response -> print(response) }
            .doOnError { error -> print(error.message) } as Flux<T>
    }

    fun <T> rSocketChannel(
        rSocketRequester: RSocketRequester,
        method: String,
        data: Flux<out Any>,
        returnClass: Class<out Any?>,
        metadata: Map<String, Any>?
    ): Flux<T> {
        return getRequestSpec(
            rSocketRequester,
            method,
        ).data(data)
            .retrieveFlux(returnClass)
            .doOnNext { response -> print(response) }
            .doOnError { error -> print(error.message) } as Flux<T>
    }

    fun <T> rSocketMono(
        rSocketRequester: RSocketRequester,
        apiMethod: String,
        data: Any,
        returnClass: Class<out Any?>,
        metadata: Map<String, Any>?
    ): Mono<T> {
        return getRequestSpec(
            rSocketRequester,
            apiMethod,
        ).data(data)
            .retrieveMono(returnClass)
            .doOnNext { response -> print(response) }
            .doOnError { error -> print(error.message) } as Mono<T>
    }

    fun <T> rSocketMono(
        rSocketRequester: RSocketRequester,
        apiMethod: String,
        data: Any,
        returnClass: Class<out Any>
    ): Mono<T> = rSocketMono(rSocketRequester, apiMethod, data, returnClass, null)

    fun <T> rSocketChannel(
        rSocketRequester: RSocketRequester,
        rSocketMapping: String,
        returnClass: Class<out Any?>
    ): Flux<T> {
        val setting1 = Mono.just(Duration.ofSeconds(1))
        val setting2 = Mono.just(Duration.ofSeconds(2)).delayElement(Duration.ofSeconds(5))
        val setting3 = Mono.just(Duration.ofSeconds(3)).delayElement(Duration.ofSeconds(10))

        val settings = Flux.concat(setting1, setting2, setting3)
        return rSocketChannel(rSocketRequester, rSocketMapping, settings, returnClass, null)
    }

    private fun getRequestSpec(
        rSocketRequester: RSocketRequester,
        route: String
    ): RSocketRequester.RequestSpec {

        return rSocketRequester.route(route)
    }
}