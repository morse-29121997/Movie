package com.morse.data.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.morse.data.database.MoviesDao
import com.morse.data.paginations.NowPlayingMoviesPagingSource
import com.morse.data.remote.MoviesApis
import com.morse.data.repositories.MoviesRepository
import com.morse.datasource.local.database.MoviesDataBase
import com.morse.domain.paging.IMoviesPagingSource
import com.morse.domain.repositories.IMoviesRepository
import com.morse.mylibrary.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesDispatchersModule {

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesScopesModule {

    @Singleton
    @Provides
    fun providesCoroutineScope(
        @IoDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {


    @Singleton
    @Provides
    fun provideMoviesRepository(
        apis: MoviesApis,
        db: MoviesDao,
        paginationSource: IMoviesPagingSource,
        scope: CoroutineScope
    ) : IMoviesRepository =
        MoviesRepository(apis, db, paginationSource, scope)


    @Singleton
    @Provides
    fun provideMoviesPaging(
        apis: MoviesApis,
    ): IMoviesPagingSource =
        NowPlayingMoviesPagingSource(apis)

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClientBuilder: OkHttpClient.Builder,
    ): Retrofit {

        val okHttpClient = okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).writeTimeout(
                5, TimeUnit.MINUTES
            ).build()

        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .enableComplexMapKeySerialization().serializeNulls()
                        .setPrettyPrinting().setVersion(1.0).setLenient().create()
                )
            )
            .baseUrl(BuildConfig.BASE_URL).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpBuilder(): OkHttpClient.Builder {
        val client = OkHttpClient.Builder()
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val authorizationInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
            request.addHeader(
                "Authorization", "Bearer ${BuildConfig.TOKEN}"
            )
            request.addHeader("Accept", "application/json")
            request.addHeader("Content-Type", "application/json")
            val response = chain.proceed(request.build())
            return@Interceptor response
        }
        return client
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(logInterceptor)

    }

    @Provides
    @Singleton
    fun provideMoviesAPIs(retrofit: Retrofit): MoviesApis = retrofit.create(MoviesApis::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): MoviesDataBase {
        return Room.databaseBuilder(
            context,
            MoviesDataBase::class.java,
            "movies_database"
        ).fallbackToDestructiveMigration()
            .setQueryCallback(
                { sqlQuery, bindArgs -> println("SQL Query: $sqlQuery SQL Args: $bindArgs") },
                Executors.newSingleThreadExecutor()
            )
            .build()
    }

    @Provides
    fun provideNewsDao(appDatabase: MoviesDataBase): MoviesDao {
        return appDatabase.moviesDao()
    }

}