//
//  AppDelegate.h
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LoginController.h"
#import "HomeViewController.h"
#import <Parse/Parse.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <ParseFacebookUtilsV4/PFFacebookUtils.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (nonatomic, strong) UINavigationController    *navController;
@property (nonatomic, strong) LoginController   *viewController;
@property (nonatomic, strong) HomeViewController   *homeviewController;

@end

