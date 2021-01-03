package nl.woonwaard.wij_maken_de_wijk.domain.services

interface CrashReporter {
    fun setUserId(id: String)

    fun clearUserId()

    fun logSoftError(error: Exception)
}
