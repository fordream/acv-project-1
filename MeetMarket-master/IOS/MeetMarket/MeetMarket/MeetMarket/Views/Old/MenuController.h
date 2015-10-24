//
//  UIViewController+HomeViewController.h
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Color.h"
#import "EditProfileController.h"
#import "HomeViewController.h"

@interface  MenuController:UIViewController
@property (strong, nonatomic) IBOutlet UIView *view;
@property (strong, nonatomic) UIViewController *ParentView;
@property (strong, nonatomic) IBOutlet UIView *ProfileView;
@property (strong, nonatomic) IBOutlet UIView *ScheduleView;
@property (strong, nonatomic) IBOutlet UIView *NewMeetingView;
@property (strong, nonatomic) IBOutlet UIView *NewContactsView;
@property (strong, nonatomic) IBOutlet UIView *SettingsView;
@property (strong, nonatomic) IBOutlet UIImageView *ScheduleImage;
@property (strong, nonatomic) IBOutlet UILabel *ScheduleText;
@property (strong, nonatomic) IBOutlet UIImageView *MeetingImage;
@property (strong, nonatomic) IBOutlet UILabel *MeetingText;
@property (strong, nonatomic) IBOutlet UIImageView *ContactsImage;
@property (strong, nonatomic) IBOutlet UILabel *ContactsText;
@property (strong, nonatomic) IBOutlet UIImageView *SettingsImage;
@property (strong, nonatomic) IBOutlet UILabel *SettingsText;

@end
