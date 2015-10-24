//
//  UIViewController+HomeViewController.m
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "ForgotPasswordController.h"

@implementation ForgotPasswordController
NSMutableArray *meetings;

#pragma mark-View Life Cycle
-(void)viewDidLoad{
    [super viewDidLoad];
    
    UITapGestureRecognizer *CLoseKeyboardTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(CloseKeyboard:)];
    [self.view addGestureRecognizer:CLoseKeyboardTap];
    
    self.EmailField.delegate = self;
}

-(void)ResetPassword:(NSString*)email{
    [PFUser requestPasswordResetForEmailInBackground:email];
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Email Semt"
                                                    message:@""
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
}

- (BOOL)prefersStatusBarHidden {
    return YES;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    
}

- (IBAction)SendClicked:(id)sender {
    if (![Validation NSStringIsValidEmail:self.EmailField.text]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Email is invalid"
                                                        message:@"Please enter a valid email"
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        return;
    }
    [self ResetPassword:self.EmailField.text];
}

- (IBAction)SigninClicked:(id)sender {
    [self.navigationController popViewControllerAnimated:true];
}

-(void)CloseKeyboard:(UIGestureRecognizer*)gesture{
    [self.EmailField resignFirstResponder];
}


#pragma mark-View UITextField
- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {
    if (theTextField == self.EmailField) {
        [self.SendBtn sendActionsForControlEvents:UIControlEventTouchUpInside];
    }
    
    return YES;
}

@end
