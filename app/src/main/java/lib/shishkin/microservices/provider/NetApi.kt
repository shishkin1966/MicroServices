package lib.shishkin.microservices.provider


import io.reactivex.Single
import lib.shishkin.microservices.data.Ticker
import retrofit2.http.GET

interface NetApi {
    @get:GET("v1/ticker/")
    val tickers: Single<List<Ticker>>
}
