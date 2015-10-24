//
//  UIViewController+HomeViewController.m
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "SignUpController.h"

@implementation SignUpController


#pragma mark-View Life Cycle
-(void)viewDidLoad{
    [super viewDidLoad];
    
    UITapGestureRecognizer *CLoseKeyboardTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(CloseKeyboard:)];
    [self.view addGestureRecognizer:CLoseKeyboardTap];
    self.UsernameField.delegate = self;
    self.EmailField.delegate = self;
    self.PasswordField.delegate = self;
}

-(void)CloseKeyboard:(UIGestureRecognizer*)gesture{
    [self.PasswordField resignFirstResponder];
    [self.UsernameField resignFirstResponder];
    [self.EmailField resignFirstResponder];
}

- (BOOL)prefersStatusBarHidden {
    return YES;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
}



- (IBAction)SignUpClicked:(id)sender {
    if (![self.UsernameField.text isEqualToString:@""] && ![self.PasswordField.text isEqualToString:@""] && ![Validation NSStringIsValidEmail:self.EmailField.text]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Username, Email or Password were invalid"
                                                        message:@"Please enter valid data"
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        return;
    }
    
    [self CreateAccount:self.UsernameField.text Password:self.PasswordField.text Email:self.EmailField.text];
    
}

- (IBAction)SigninClicked:(id)sender {
    [self.navigationController popViewControllerAnimated:true];
}

#pragma mark-View UITextField
- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {
    if (theTextField == self.UsernameField) {
        [self.EmailField becomeFirstResponder];
    } else if (theTextField == self.EmailField) {
        [self.PasswordField becomeFirstResponder];
    } else if (theTextField == self.PasswordField) {
        [self.SignUpButton sendActionsForControlEvents:UIControlEventTouchUpInside];
    }
    
    return YES;
}



- (void)CreateAccount:(NSString*)Username Password:(NSString*)Password Email:(NSString*)email {
    PFUser *user = [PFUser user];
    user.username = Username;
    user.password = Password;
    user.email = email;
    
    [user signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
        if (!error) {
            HomeViewController *view = [[HomeViewController alloc]initWithNibName:@"HomeViewController" bundle:nil];
            [self.navigationController pushViewController:view animated:true];
        } else {
            
            
        }
    }];
}

@end
