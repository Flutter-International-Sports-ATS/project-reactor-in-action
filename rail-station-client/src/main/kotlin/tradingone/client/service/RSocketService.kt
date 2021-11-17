package tradingone.client.service

import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Service
import org.springframework.util.MimeType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import tradingone.client.domain.TrainScheduleResponse
import java.time.Duration
import java.time.Instant
import java.util.function.Consumer

@Service
class RSocketService {

    /**
     * Create RSocket request to specific RSocket server for Flux response
     * @param rSocketRequester -> Configured requester for specific API
     * @param service -> service route
     * @param method -> service RSocket method
     * @param data -> Protocol buffer request
     * @param returnClass -> Response class
     * @param metadata -> Map with metadata mimetypes and their values
     */
    fun <T> rSocketFlux(
        rSocketRequester: RSocketRequester,
        service: String?,
        method: String?,
        data: Any,
        returnClass: Class<out Any?>,
        metadata: Map<String, Any>?
    ): Flux<T> {
        return getRequestSpec(
            rSocketRequester,
            buildRoute(service!!, method!!),
            metadata
        ).data(data)
            .retrieveFlux(returnClass)
            .doOnNext(Consumer { response -> print(response) })
            .doOnError(Consumer { error -> print(error.message) }) as Flux<T>
    }

    /**
     * Create RSocket request to specific RSocket server for Flux response
     * @param rSocketRequester -> Configured requester for specific API
     * @param apiMethod -> service RSocket method
     * @param data -> Protocol buffer request
     * @param returnClass -> Response class
     * @param metadata -> Map with metadata mimetypes and their values
     */
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
            metadata
        ).data(data)
            .retrieveFlux(returnClass)
            .doOnNext(Consumer { response -> print(response) })
            .doOnError(Consumer { error -> print(error.message) }) as Flux<T>
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
            metadata
        ).data(data)
            .retrieveFlux(returnClass)
            .doOnNext(Consumer { response -> print(response) })
            .doOnError(Consumer { error -> print(error.message) }) as Flux<T>
    }

    /**
     * Create RSocket request to specific RSocket server for Mono response
     * @param rSocketRequester -> Configured requester for specific API
     * @param apiMethod -> service rsocket method
     * @param data -> Protocol buffer request
     * @param returnClass -> Response class
     * @param metadata -> Map with metadata mimetypes and their values
     */
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
            metadata
        ).data(data)
            .retrieveMono(returnClass)
            .doOnNext(Consumer { response -> print(response) })
            .doOnError(Consumer { error -> print(error.message) }) as Mono<T>
    }

    /**
     * Create RSocket request to specific RSocket server for Mono response
     * @param rSocketRequester -> Configured requester for specific API
     * @param apiMethod -> service rsocket method
     * @param data -> Protocol buffer request
     * @param returnClass -> Response class
     */
    fun <T> rSocketMono(
        rSocketRequester: RSocketRequester,
        apiMethod: String,
        data: Any,
        returnClass: Class<out Any>
    ): Mono<T> = rSocketMono(rSocketRequester, apiMethod, data, returnClass, null)

    /**
     * Create RSocket request to specific RSocket server for Mono response
     * @param rSocketRequester -> Configured requester for specific API
     * @param service -> service route
     * @param method -> service rsocket method
     * @param data -> Protocol buffer request
     * @param returnClass -> Response class
     * @param metadata -> Map with metadata mimetypes and their values
     */
    fun <T> rSocketMono(
        rSocketRequester: RSocketRequester,
        service: String?,
        method: String?,
        data: Any,
        returnClass: Class<out Any?>,
        metadata: Map<String, Any>?
    ): Mono<T> {
        return getRequestSpec(
            rSocketRequester,
            buildRoute(service!!, method!!),
            metadata
        ).data(data)
            .retrieveMono(returnClass)
            .doOnNext(Consumer { response -> print(response) })
            .doOnError(Consumer { error -> print(error.message) }) as Mono<T>
    }

    /**
     * Create RSocket request to specific RSocket server for Flux response
     * @param rSocketRequester -> Configured requester for specific API
     * @param service -> service route
     * @param method -> service RSocket method
     * @param data -> Protocol buffer request
     * @param returnClass -> Response class
     */
    fun <T> rSocketFlux(
        rSocketRequester: RSocketRequester,
        service: String?,
        method: String?,
        data: Any,
        returnClass: Class<out Any?>
    ): Flux<T> = rSocketFlux(rSocketRequester, service, method, data, returnClass, null)

    fun <T> rSocketChannel(
        rSocketRequester: RSocketRequester,
        rSocketMapping: String,
        returnClass: Class<out Any?>
    ): Flux<T> {
        val setting1 = Mono.just(Duration.ofSeconds(1))
        val setting2 = Mono.just(Duration.ofSeconds(3)).delayElement(Duration.ofSeconds(5))
        val setting3 = Mono.just(Duration.ofSeconds(5)).delayElement(Duration.ofSeconds(15))

        val settings = Flux.concat(setting1, setting2, setting3)
        return rSocketChannel(rSocketRequester, rSocketMapping, settings, returnClass, null)
    }

    /**
     * Create RSocket request to specific RSocket server for Mono response
     * @param rSocketRequester -> Configured requester for specific API
     * @param service -> service route
     * @param method -> service rsocket method
     * @param data -> Protocol buffer request
     * @param returnClass -> Response class
     */
    fun <T> rSocketMono(
        rSocketRequester: RSocketRequester,
        service: String?,
        method: String?,
        data: Any,
        returnClass: Class<out Any?>
    ): Mono<T> = rSocketMono(rSocketRequester, service, method, data, returnClass, null)

    private fun getRequestSpec(
        rSocketRequester: RSocketRequester,
        route: String,
        metadata: Map<String, Any>?
    ): RSocketRequester.RequestSpec {
        val requestSpec: RSocketRequester.RequestSpec = rSocketRequester
            .route(route)
        metadata?.forEach { (mimeType, value) ->
            requestSpec.metadata(value, MimeType.valueOf(mimeType))
        }
        return requestSpec
    }

    private fun buildRoute(service: String, method: String): String {
        return "$service.$method"
    }
}