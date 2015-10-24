//
//  UIViewController+HomeViewController.h
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ContactsCell.h"
#import <AddressBook/AddressBook.h>
#import <AddressBook/ABAddressBook.h>
#import <AddressBook/ABPerson.h>
#import "Contacts.h"
#import <Parse/Parse.h>
#import "Meeting.h"
#import "Invitee.h"
#import "Reachability.h"

@interface  ContactsController:UIViewController<UITableViewDataSource, UITableViewDelegate>
@property (strong, nonatomic) IBOutlet UIView *view;
@property (strong, nonatomic) IBOutlet UIView *NavBarView;

- (IBAction)BackClicked:(id)sender;
@property (strong, nonatomic) IBOutlet UITableView *ContactTable;

@property (nonatomic, strong) NSString  *Date;
@property (nonatomic, strong) NSString  *StartTime;
@property (nonatomic, strong) NSString  *EndTime;
@property (nonatomic, strong) NSString  *MettingSlots;
@property (nonatomic, strong) NSString  *Location;
@property (nonatomic, strong) NSString  *OptionalText;

- (IBAction)SendClicked:(id)sender;

@end
