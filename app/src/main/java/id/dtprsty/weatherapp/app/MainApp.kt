package id.dtprsty.weatherapp.app

import android.app.Application
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jacksonandroidnetworking.JacksonParserFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainApp : Application(){

    companion object {
        lateinit var mInstance: MainApp
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainApp)
        }

        mInstance = this
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        AndroidNetworking.setParserFactory(JacksonParserFactory())
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY)
        AndroidNetworking.setConnectionQualityChangeListener { currentConnectionQuality, currentBandwidth ->
            Log.d(
                "App",
                "onChange: currentConnectionQuality : \$currentConnectionQuality currentBandwidth : \$currentBandwidth"
            )
        }
        AndroidNetworking.initialize(applicationContext, okHttpClient)
    }
}