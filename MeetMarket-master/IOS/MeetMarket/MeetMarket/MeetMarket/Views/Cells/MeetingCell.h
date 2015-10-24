//
//  UITableViewCell+MeetingCell.h
//  MeetMarket
//
//  Created by Jacob Keller on 4/05/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Meeting.h"
#import <Parse/Parse.h>

@interface  MeetingCell:UITableViewCell
@property (strong, nonatomic) IBOutlet UIImageView *Image;
@property (strong, nonatomic) IBOutlet UILabel *Name;
@property (strong, nonatomic) IBOutlet UILabel *Date;
@property (strong, nonatomic) IBOutlet UILabel *Time;
@property (strong, nonatomic) IBOutlet UILabel *Location;

-(void)SetupCell:(PFObject*)contact withInvitee:(NSDictionary*) myInvitee;
-(void)SetupLastCell;

@end
