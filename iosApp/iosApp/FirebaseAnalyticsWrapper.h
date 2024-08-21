//
//  FirebaseAnalyticsWrapper.h
//  iosApp
//
//  Created by Aleksandr Smirnov on 20.08.2024.
//  Copyright © 2024 orgName. All rights reserved.
//



#ifndef FirebaseAnalyticsWrapper_h
#define FirebaseAnalyticsWrapper_h

#import <Foundation/Foundation.h>


@interface FirebaseAnalyticsWrapper : NSObject

+ (instancetype)shared;
- (void)logEventWithName:(NSString *)name parameters:(NSDictionary<NSString *, id> * _Nullable)parameters;

@end


#endif /* FirebaseAnalyticsWrapper_h */


