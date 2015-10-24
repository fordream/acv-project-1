//
//  UIViewController+HomeViewController.h
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HomeViewController.h"
#import <Parse/Parse.h>
#import "Validation.h"
#import "Reachability.h"

@interface  SignUpController:UIViewController
@property (strong, nonatomic) IBOutlet UIView *view;
@property (strong, nonatomic) IBOutlet UITextField *PasswordField;
@property (strong, nonatomic) IBOutlet UITextField *EmailField;
@property (strong, nonatomic) IBOutlet UITextField *UsernameField;
@property (strong, nonatomic) IBOutlet UIButton *SignUpButton;

- (IBAction)SignUpClicked:(id)sender;
- (IBAction)SigninClicked:(id)sender;
@end
