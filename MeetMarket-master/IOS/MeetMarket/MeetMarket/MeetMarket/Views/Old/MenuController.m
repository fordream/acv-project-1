//
//  UIViewController+HomeViewController.m
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "MenuController.h"

@implementation MenuController

#pragma mark-View Life Cycle
-(void)viewDidLoad{
    [super viewDidLoad];
    
    UITapGestureRecognizer *tap1 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(MenuClicked:)];
    [self.ProfileView addGestureRecognizer:tap1];
    
    UITapGestureRecognizer *tap2 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(MenuClicked:)];
    [self.ScheduleView addGestureRecognizer:tap2];
    
    UITapGestureRecognizer *tap3 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(MenuClicked:)];
    [self.NewMeetingView addGestureRecognizer:tap3];
    
    UITapGestureRecognizer *tap4 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(MenuClicked:)];
    [self.NewContactsView addGestureRecognizer:tap4];
    
    UITapGestureRecognizer *tap5 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(MenuClicked:)];
    [self.SettingsView addGestureRecognizer:tap5];
    
    if ([self.ParentView isKindOfClass:[HomeViewController class]]) {
        [self.ScheduleImage setHighlighted:true];
        [self.ScheduleText setTextColor:[Color colorWithHexString:@"D6402B"]];
    }else{
        [self.ScheduleImage setHighlighted:false];
        [self.ScheduleText setTextColor:[Color colorWithHexString:@"484646"]];
    }
    
    
}


-(void)MenuClicked:(UITapGestureRecognizer *)recognizer {
    [recognizer.view setBackgroundColor:[Color colorWithHexString:@"E0E0E0"]];
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, 0.2 * NSEC_PER_SEC);
    dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
        [recognizer.view setBackgroundColor:[Color colorWithHexString:@"FFFFFF"]];
    });
    if (recognizer.view.tag == 1) {//Profile
        
        EditProfileController * view = [[EditProfileController alloc]initWithNibName:@"EditProfileController" bundle:nil];
        [self.ParentView.navigationController pushViewController:view animated:true];
    } else if (recognizer.view.tag == 2){//Schedule
        
    } else if (recognizer.view.tag == 3){//New Meeting
        
    } else if (recognizer.view.tag == 4){//New Contacts
        
    } else if (recognizer.view.tag == 5){//Settings
        
    }
    
}



@end
