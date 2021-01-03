package nl.woonwaard.wij_maken_de_wijk

import com.google.firebase.crashlytics.FirebaseCrashlytics
import nl.woonwaard.wij_maken_de_wijk.domain.services.CrashReporter

object FirebaseCrashReporter : CrashReporter {
    override fun setUserId(id: String) {
        FirebaseCrashlytics.getInstance().setUserId(id)
    }

    override fun clearUserId() {
        FirebaseCrashlytics.getInstance().setUserId("")
    }

    override fun logSoftError(error: Exception) {
        FirebaseCrashlytics.getInstance().recordException(error)
    }
}
