//import data.firebase.Analytics
//import platform.Foundation.NSDictionary

//class FirebaseAnalytics: Analytics {
//     override fun logEvent(name: String, params: Map<String, Any?>) {
//        val nsParams = params.mapValues { it.value ?: NSNull() }
//        FirebaseAnalyticsWrapper.shared.logEvent(name = name, parameters = nsParams as NSDictionary)
//    }
//}