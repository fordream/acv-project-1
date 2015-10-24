//
//  UITableViewCell+MeetingCell.h
//  MeetMarket
//
//  Created by Jacob Keller on 4/05/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Contacts.h"
#import "Color.h"

@interface  ContactsCell:UITableViewCell
@property (strong, nonatomic) IBOutlet UILabel *ContactName;
@property (strong, nonatomic) IBOutlet UILabel *ContactEmail;
@property (strong, nonatomic) IBOutlet UIImageView *RadioButton;
@property (strong, nonatomic) IBOutlet UIView *view;

-(void)SetupCell:(Contacts*)contact;

@end
