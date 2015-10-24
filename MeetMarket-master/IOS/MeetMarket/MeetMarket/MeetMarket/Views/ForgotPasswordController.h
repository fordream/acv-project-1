//
//  UIViewController+HomeViewController.h
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Validation.h"
#import <Parse/Parse.h>

@interface  ForgotPasswordController:UIViewController
@property (strong, nonatomic) IBOutlet UIView *view;
@property (strong, nonatomic) IBOutlet UITextField *EmailField;
@property (strong, nonatomic) IBOutlet UIButton *SendBtn;
- (IBAction)SendClicked:(id)sender;
- (IBAction)SigninClicked:(id)sender;
@end
