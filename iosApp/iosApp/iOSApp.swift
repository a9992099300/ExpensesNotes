import SwiftUI
import FirebaseCore
import Foundation
import FirebaseAnalytics


class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    FirebaseApp.configure()
    return true
  }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
//    init {
//        Analytics.
//    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}

//class IosAnalytics: Pla {
//  func lo
//}
