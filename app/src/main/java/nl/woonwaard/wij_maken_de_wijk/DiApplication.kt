package nl.woonwaard.wij_maken_de_wijk

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

@Suppress("unused")
class DiApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DiApplication)
            modules(weMakeTheDistrictModule)
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}
