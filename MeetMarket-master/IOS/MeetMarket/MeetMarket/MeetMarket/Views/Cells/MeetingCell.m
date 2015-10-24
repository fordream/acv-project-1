//
//  UITableViewCell+MeetingCell.m
//  MeetMarket
//
//  Created by Jacob Keller on 4/05/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "MeetingCell.h"

@implementation MeetingCell

-(void)SetupCell:(PFObject*)meeting  withInvitee:(NSDictionary*) myInvitee{
    
    [self.Location setHidden:false];
    [self.Time setHidden:false];
    
    NSDate *startTime = meeting[@"StartTime"];
    NSDate *endTime = meeting[@"EndTime"];
    NSTimeInterval secs = [endTime timeIntervalSinceDate:startTime];

    double slots = [meeting[@"NumberOfSlots"] doubleValue];
    double mySlot = [[myInvitee valueForKey:@"slot"] doubleValue];
    double secsPerSlot = secs/slots;
    double mysecsPerSlot = secsPerSlot * (mySlot - 1);
    
    startTime = [startTime dateByAddingTimeInterval:mysecsPerSlot];
    endTime = [startTime dateByAddingTimeInterval:secsPerSlot];

    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"hh:mm a"];
    
    NSString *time = [NSString stringWithFormat:@"%@ to %@",[formatter stringFromDate:startTime], [formatter stringFromDate:endTime]];
    
    
    
    if([[NSCalendar currentCalendar] isDateInToday:meeting[@"MeetingDate"]]) {
        self.Date.text = @"Today";
        //do stuff
    } else if ([[NSCalendar currentCalendar] isDateInTomorrow:meeting[@"MeetingDate"]]){
        self.Date.text = @"Tommorow";
    } else{
        [formatter setDateFormat:@"dd/MM/yyyy"];
        self.Date.text = [formatter stringFromDate:meeting[@"MeetingDate"]];
    }
    
    self.Image.image = [UIImage imageNamed:@"mm_meetinginvite_icon_thumb.png"];

    
    [formatter setDateFormat:@"dd/MM/yyyy"];
    
    self.Location.text = meeting[@"Location"];
    self.Time.text = time;
    
    NSString *firstName = [myInvitee valueForKey:@"FirstName"];
    NSString *lastName = [myInvitee valueForKey:@"LastName"];
    
    self.Name.text = [NSString stringWithFormat:@"%@ %@",firstName,lastName];
}

-(void)SetupLastCell{
    self.Name.text = @"Create New Meeting";
    self.Date.text = @"With people in your circle";
    self.Image.image = [UIImage imageNamed:@"mm_dashboard_meeting_iconcreate.png"];
    [self.Location setHidden:true];
    [self.Time setHidden:true];
}

@end
