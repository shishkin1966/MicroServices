package lib.shishkin.microservices.provider


import io.reactivex.Single
import lib.shishkin.microservices.data.ValCurs
import retrofit2.http.GET
import retrofit2.http.Query


interface NetCbApi {
    @GET("scripts/XML_daily_eng.asp")
    fun getData(@Query("date_req") dateReg: String?): Single<ValCurs>?
}
