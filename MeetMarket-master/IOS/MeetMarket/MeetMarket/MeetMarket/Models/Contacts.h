//
//  NSObject+Contacts.h
//  MeetMarket
//
//  Created by Jacob Keller on 6/05/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Contacts:NSObject

@property (nonatomic, strong) NSString  *FirstName;
@property (nonatomic, strong) NSString  *LastName;
@property (nonatomic, strong) NSString  *Email;
@property (nonatomic) BOOL  isSelected;

@end
