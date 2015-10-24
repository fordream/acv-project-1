//
//  UIViewController+HomeViewController.h
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AddressBookUI/AddressBookUI.h>
#import <AddressBook/AddressBook.h>
#import "ContactsController.h"

@interface  CreateMeetingController:UIViewController<ABPeoplePickerNavigationControllerDelegate>
@property (strong, nonatomic) IBOutlet UIView *view;
@property (strong, nonatomic) IBOutlet UIView *NavBarView;
@property (strong, nonatomic) IBOutlet UIDatePicker *DatePicker;
@property (strong, nonatomic) IBOutlet UITextField *DateField;
@property (strong, nonatomic) IBOutlet UITextField *StartTimeField;
@property (strong, nonatomic) IBOutlet UITextField *EndTimeField;
@property (strong, nonatomic) IBOutlet UITextField *MettingSlotsField;
@property (strong, nonatomic) IBOutlet UITextField *LocationField;
@property (strong, nonatomic) IBOutlet UITextView *OptionalTextView;
@property (strong, nonatomic) IBOutlet UIButton *SubmitButton;
- (IBAction)SubmitButtonClicked:(id)sender;
- (IBAction)BackClicked:(id)sender;

@end
