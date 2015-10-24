//
//  UIViewController+HomeViewController.h
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SignUpController.h"
#import "ForgotPasswordController.h"
#import "HomeViewController.h"
#import <Parse/Parse.h>
#import <ParseFacebookUtilsV4/PFFacebookUtils.h>
#import "Reachability.h"

@interface  LoginController:UIViewController
@property (strong, nonatomic) IBOutlet UIView *view;
@property (strong, nonatomic) IBOutlet UITextField *UserNameField;
@property (strong, nonatomic) IBOutlet UITextField *PasswordField;
@property (strong, nonatomic) IBOutlet UIButton *SigninButton;
- (IBAction)FacebookClicked:(id)sender;

- (IBAction)SigninClicked:(id)sender;
- (IBAction)CreateAccountClicked:(id)sender;
- (IBAction)ForgotClicked:(id)sender;
@end
