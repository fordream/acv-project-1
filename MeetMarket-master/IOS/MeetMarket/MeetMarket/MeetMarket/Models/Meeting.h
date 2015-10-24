//
//  NSObject+Contacts.h
//  MeetMarket
//
//  Created by Jacob Keller on 6/05/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Meeting:NSObject
@property (nonatomic, strong) NSString  *objectId;
@property (nonatomic, strong) NSDate  *createdAt;
@property (nonatomic, strong) NSDate  *updatedAt;
@property (nonatomic, strong) NSString  *MeetingDescription;
@property (nonatomic, strong) NSDate  *StartTime;
@property (nonatomic, strong) NSDate  *EndTime;
@property (nonatomic, strong) NSDate  *MeetingDate;
@property (nonatomic, strong) NSNumber  *NumberOfSlots;
@property (nonatomic, strong) NSMutableArray  *Invitees;//Id, FirstName, LastName, Email, SlotNumber (optional)

@end
