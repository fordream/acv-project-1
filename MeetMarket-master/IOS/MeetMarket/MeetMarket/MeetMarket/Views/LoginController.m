//
//  UIViewController+HomeViewController.m
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "LoginController.h"

@implementation LoginController

#pragma mark-View Life Cycle
-(void)viewDidLoad{
    [super viewDidLoad];
    
    UITapGestureRecognizer *CLoseKeyboardTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(CloseKeyboard:)];
    [self.view addGestureRecognizer:CLoseKeyboardTap];
    
    self.UserNameField.delegate = self;
    self.PasswordField.delegate = self;
    
}

-(void)CloseKeyboard:(UIGestureRecognizer*)gesture{
    [self.UserNameField resignFirstResponder];
    [self.PasswordField resignFirstResponder];
}

- (BOOL)prefersStatusBarHidden {
    return YES;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.PasswordField.text = @"";
    self.UserNameField.text = @"";
    [self.SigninButton setEnabled:true];

}





- (IBAction)FacebookClicked:(id)sender {
    NSMutableArray *permissions = [[NSMutableArray alloc]init];
    NSString *string1 = @"email";
    [permissions addObject:string1];
    [PFFacebookUtils logInInBackgroundWithReadPermissions:permissions block:^(PFUser *user, NSError *error) {
        if (!user) {
            NSLog(@"Uh oh. The user cancelled the Facebook login.");
        } else if (user.isNew) {
            NSLog(@"User signed up and logged in through Facebook!");
            HomeViewController *view = [[HomeViewController alloc]initWithNibName:@"HomeViewController" bundle:nil];
            [self.navigationController pushViewController:view animated:true];
        } else {
            NSLog(@"User logged in through Facebook!");
            HomeViewController *view = [[HomeViewController alloc]initWithNibName:@"HomeViewController" bundle:nil];
            [self.navigationController pushViewController:view animated:true];
        }
    }];
}

- (IBAction)SigninClicked:(id)sender {
    
    Reachability *networkReachability = [Reachability reachabilityForInternetConnection];
    NetworkStatus networkStatus = [networkReachability currentReachabilityStatus];
    if (networkStatus == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No Connection"
                                                        message:@"Please connect to the internet"
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        return;
    }
    
    if ([self.UserNameField.text isEqualToString:@""] && [self.PasswordField.text isEqualToString:@""]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Username or Password were invalid"
                                                        message:@"Please enter valid data"
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        return;
    }
    [self.SigninButton setEnabled:false];
    [self Login:self.UserNameField.text Passeord:self.PasswordField.text];
}

- (IBAction)CreateAccountClicked:(id)sender {
    SignUpController *view = [[SignUpController alloc]initWithNibName:@"SignUpController" bundle:nil];
    [self.navigationController pushViewController:view animated:true];
}

- (IBAction)ForgotClicked:(id)sender {
    ForgotPasswordController *view = [[ForgotPasswordController alloc]initWithNibName:@"ForgotPasswordController" bundle:nil];
    [self.navigationController pushViewController:view animated:true];
}

#pragma mark-View UITextField
- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {
    if (theTextField == self.UserNameField) {
        [self.PasswordField becomeFirstResponder];
    }
    else if (theTextField == self.PasswordField) {
        [self.SigninButton sendActionsForControlEvents:UIControlEventTouchUpInside];
    }
    return YES;
}


-(void)Login:(NSString*)Username Passeord:(NSString*)Password{
    [PFUser logInWithUsernameInBackground:Username password:Password
                                    block:^(PFUser *user, NSError *error) {
                                        if (user) {
                                            // Do stuff after successful login.
                                            HomeViewController *view = [[HomeViewController alloc]initWithNibName:@"HomeViewController" bundle:nil];
                                            [self.navigationController pushViewController:view animated:true];
                                        } else {
                                            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Username or Password were invalid"
                                                                                            message:@"Please enter valid data"
                                                                                           delegate:nil
                                                                                  cancelButtonTitle:@"OK"
                                                                                  otherButtonTitles:nil];
                                            [alert show];
                                            [self.SigninButton setEnabled:true];
                                        }
                                    }];
}

@end
