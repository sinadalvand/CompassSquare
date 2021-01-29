/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 11:19 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 11:19 AM
 *
 */

package ir.sinadalvand.projects.bazaarproject.di

import ir.sinadalvand.projects.bazaarproject.data.remote.FoursquareApiCallInterface
import ir.sinadalvand.projects.bazaarproject.util.Const
import ir.sinadalvand.projects.bazaarproject.util.Utils
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single<Interceptor>(named("logger")) {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single(named("params")) {
        Interceptor {
            val orginal = it.request()
            val request = orginal.newBuilder()
            val originalHttpUrl: HttpUrl = orginal.url
            val url = originalHttpUrl.newBuilder()

            // add auth parameters to every request
            url.addQueryParameter("client_id", Const.API_CLIENT_ID_KEY)
            url.addQueryParameter("client_secret", Const.API_CLIENT_SECRET_KEY)

            // need send current date
            url.addQueryParameter("v", Utils.getDateIntFormat())



            request.url(url.build())
            request.method(orginal.method, orginal.body)
            it.proceed(request.build())
        }
    }


    single {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>(named("logger")))
            .addInterceptor(get<Interceptor>(named("params")))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }


    single {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl("https://api.foursquare.com/v2/venues/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



    single { get<Retrofit>().create(FoursquareApiCallInterface::class.java) }
}