//
//  UIViewController+HomeViewController.m
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "CreateMeetingController.h"

@implementation CreateMeetingController
NSMutableArray *meetings;

#pragma mark-View Life Cycle
-(void)viewDidLoad{
    [super viewDidLoad];
    
    UITapGestureRecognizer *CLoseKeyboardTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(CloseKeyboard:)];
    [self.view addGestureRecognizer:CLoseKeyboardTap];
    self.OptionalTextView.delegate = self;
    self.MettingSlotsField.delegate = self;
    self.LocationField.delegate = self;
    
    UIDatePicker *datePicker = [[UIDatePicker alloc]init];
    [datePicker setDate:[NSDate date]];
    [datePicker setDatePickerMode:UIDatePickerModeDate];
    [datePicker addTarget:self action:@selector(updateDateTextField:) forControlEvents:UIControlEventValueChanged];
    [self.DateField setInputView:datePicker];
    
    UIDatePicker *datePicker2 = [[UIDatePicker alloc]init];
    [datePicker2 setDate:[NSDate date]];
    [datePicker2 setDatePickerMode:UIDatePickerModeTime];
    [datePicker2 addTarget:self action:@selector(updateStartTimeTextField:) forControlEvents:UIControlEventValueChanged];
    [self.StartTimeField setInputView:datePicker2];
    
    UIDatePicker *datePicker3 = [[UIDatePicker alloc]init];
    [datePicker3 setDate:[NSDate date]];
    [datePicker3 setDatePickerMode:UIDatePickerModeTime];
    [datePicker3 addTarget:self action:@selector(updateEndTimeTextField:) forControlEvents:UIControlEventValueChanged];
    [self.EndTimeField setInputView:datePicker3];
    
}

-(void)updateDateTextField:(id)sender
{
    UIDatePicker *picker = (UIDatePicker*)self.DateField.inputView;
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateStyle:NSDateFormatterShortStyle];
    [dateFormatter setDateFormat:@"MM'/'dd'/'yyyy"];
    
    self.DateField.text = [dateFormatter stringFromDate:picker.date];
}

-(void)updateStartTimeTextField:(id)sender
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateStyle:NSDateFormatterShortStyle];
    [dateFormatter setDateFormat:@"hh:mm a"];
    UIDatePicker *picker = (UIDatePicker*)self.StartTimeField.inputView;
    self.StartTimeField.text = [dateFormatter stringFromDate:picker.date];
}

-(void)updateEndTimeTextField:(id)sender
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateStyle:NSDateFormatterShortStyle];
    [dateFormatter setDateFormat:@"hh:mm a"];
    UIDatePicker *picker = (UIDatePicker*)self.EndTimeField.inputView;
    self.EndTimeField.text = [dateFormatter stringFromDate:picker.date];
}


- (BOOL)prefersStatusBarHidden {
    return YES;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
}

-(void)CloseKeyboard:(UIGestureRecognizer*)gesture{
    [self.MettingSlotsField resignFirstResponder];
    [self.OptionalTextView resignFirstResponder];
    [self.LocationField resignFirstResponder];
    [self.DateField resignFirstResponder];
    [self.StartTimeField resignFirstResponder];
    [self.EndTimeField resignFirstResponder];
}

- (IBAction)SubmitButtonClicked:(id)sender {
    ContactsController *view = [[ContactsController alloc]initWithNibName:@"ContactsController" bundle:nil];
    view.Date = self.DateField.text;
    view.StartTime = self.StartTimeField.text;
    view.EndTime = self.EndTimeField.text;
    view.MettingSlots = self.MettingSlotsField.text;
    view.Location = self.LocationField.text;
    view.OptionalText = self.OptionalTextView.text;
    [self.navigationController pushViewController:view animated:true];
}

- (IBAction)BackClicked:(id)sender {
    [self.navigationController popViewControllerAnimated:true];
}
#pragma mark-View UITextField
- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {
    if (theTextField == self.MettingSlotsField) {
        [self.LocationField becomeFirstResponder];
    }
    else if (theTextField == self.LocationField) {
        [self.OptionalTextView becomeFirstResponder];
    }
    return YES;
}

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    if (textField == self.LocationField) {
        [UIView setAnimationDelegate:self];
        [UIView setAnimationDidStopSelector:@selector( animationDidStop:finished:context: )];
        //    double animationDuration;
        [UIView beginAnimations:@"keyboardSlide" context:(__bridge void *)(self.view)];
        [UIView setAnimationDuration:0.3];
        
        if ([[UIScreen mainScreen]bounds].size.height<500) {
            self.view.frame=CGRectMake(0, -60, 320, 480);
        }else if ([[UIScreen mainScreen]bounds].size.height == 568){
            self.view.frame=CGRectMake(0, -60, 320, 568);
        }
        [UIView commitAnimations];
    }
    
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    if (textField == self.LocationField) {
        [UIView setAnimationDelegate:self];
        [UIView setAnimationDidStopSelector:@selector( animationDidStop:finished:context: )];
        
        [UIView beginAnimations:@"keyboardSlide" context:(__bridge void *)(self.view)];
        [UIView setAnimationDuration:0.2];
        
        if ([[UIScreen mainScreen]bounds].size.height<500) {
            self.view.frame=CGRectMake(0, 0, 320, 480);
        }else if ([[UIScreen mainScreen]bounds].size.height == 568){
            self.view.frame=CGRectMake(0, 0, 320, 568);
        }
        [UIView commitAnimations];
    }
}

#pragma mark-View UITextView
- (void)textViewDidBeginEditing:(UITextView *)textView
{
    
    [textView becomeFirstResponder];
    [UIView setAnimationDelegate:self];
    [UIView setAnimationDidStopSelector:@selector( animationDidStop:finished:context: )];
    //    double animationDuration;
    [UIView beginAnimations:@"keyboardSlide" context:(__bridge void *)(self.view)];
    [UIView setAnimationDuration:0.3];
    
    if ([[UIScreen mainScreen]bounds].size.height<500) {
        self.view.frame=CGRectMake(0, -150, 320, 480);
    }else if ([[UIScreen mainScreen]bounds].size.height == 568){
        self.view.frame=CGRectMake(0, -150, 320, 568);
    }
    [UIView commitAnimations];
}

- (void)textViewDidEndEditing:(UITextView *)textView
{
    
    [textView resignFirstResponder];
    [UIView setAnimationDelegate:self];
    [UIView setAnimationDidStopSelector:@selector( animationDidStop:finished:context: )];
    
    [UIView beginAnimations:@"keyboardSlide" context:(__bridge void *)(self.view)];
    [UIView setAnimationDuration:0.2];
    
    if ([[UIScreen mainScreen]bounds].size.height<500) {
        self.view.frame=CGRectMake(0, 0, 320, 480);
    }else if ([[UIScreen mainScreen]bounds].size.height == 568){
        self.view.frame=CGRectMake(0, 0, 320, 568);
    }
    [UIView commitAnimations];
}

@end
