//
//  UIViewController+OnBoarding.h
//  MeetMarket
//
//  Created by Jacob Keller on 1/05/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HomeViewController.h"

@interface  OnBoarding:UIViewController<UIScrollViewDelegate>
@property (strong, nonatomic) IBOutlet UIView *view;
- (IBAction)SignInClicked:(id)sender;
@property (strong, nonatomic) IBOutlet UIScrollView *OnboardingScrollView;
@property (strong, nonatomic) IBOutlet UIImageView *LeftDot;
@property (strong, nonatomic) IBOutlet UIImageView *MiddleDot;
@property (strong, nonatomic) IBOutlet UIImageView *RightDot;
@end
