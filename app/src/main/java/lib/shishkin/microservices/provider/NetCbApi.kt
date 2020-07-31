package lib.shishkin.microservices.provider


import io.reactivex.Single
import lib.shishkin.microservices.data.ValCurs
import retrofit2.http.GET

interface NetCbApi {
    @get:GET("scripts/XML_daily_eng.asp")
    val valCurs: Single<List<ValCurs>>
}
