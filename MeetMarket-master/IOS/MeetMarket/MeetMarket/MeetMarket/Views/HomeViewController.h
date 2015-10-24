//
//  UIViewController+HomeViewController.h
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TopBarController.h"
#import "MeetingCell.h"
#import "CreateMeetingController.h"
#import "LoginController.h"

@interface  HomeViewController:UIViewController<UITableViewDelegate, UITableViewDataSource>
@property (strong, nonatomic) IBOutlet UIView *view;
@property (strong, nonatomic) IBOutlet UITableView *MeetingTableView;
@property (strong, nonatomic) IBOutlet UIView *NavBarView;
@property (strong, nonatomic) IBOutlet UIView *MenuView;
- (IBAction)LogOutClicked:(id)sender;
-(void)TopMenuClicked;
@end
