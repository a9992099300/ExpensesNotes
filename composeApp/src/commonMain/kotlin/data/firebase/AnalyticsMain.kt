package data.firebase

interface AnalyticsMain {
    fun logEvent(name: String, params: Map<String, Any?>)
}