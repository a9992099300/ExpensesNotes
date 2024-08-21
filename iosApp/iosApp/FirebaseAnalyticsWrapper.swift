//
//  FirebaseAnalyticsWrapper.swift
//  iosApp
//
//  Created by Aleksandr Smirnov on 20.08.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import FirebaseAnalytics

@objc(FirebaseAnalyticsWrapper)
class FirebaseAnalyticsWrapper: NSObject {
    @objc static let shared = FirebaseAnalyticsWrapper()

    @objc func logEvent(name: String, parameters: [String: Any]?) {
        Analytics.logEvent(name, parameters: parameters)
    }
}
