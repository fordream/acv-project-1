//
//  UITableViewCell+MeetingCell.m
//  MeetMarket
//
//  Created by Jacob Keller on 4/05/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "ContactsCell.h"

@implementation ContactsCell

-(void)SetupCell:(Contacts*)contact{
    self.ContactName.text = [NSString stringWithFormat:@"%@ %@", contact.FirstName, contact.LastName];
    
    NSMutableAttributedString *attributeString = [[NSMutableAttributedString alloc] initWithString:contact.Email];
    [attributeString addAttribute:NSUnderlineStyleAttributeName
                            value:[NSNumber numberWithInt:1]
                            range:(NSRange){0,[attributeString length]}];
    self.ContactEmail.attributedText = [attributeString copy];
    
    
    
    if (contact.isSelected) {
        [self.ContactEmail setTextColor:[Color colorWithHexString:@"BF303B"]];
        [self.view setBackgroundColor:[Color colorWithHexString:@"FFFFFF"]];
        self.RadioButton.image = [UIImage imageNamed:@"mm_onboarding_icon_circlefull.png"];
    } else {
        [self.ContactEmail setTextColor:[Color colorWithHexString:@"393939"]];
        [self.view setBackgroundColor:[Color colorWithHexString:@"F8F8F8"]];
        self.RadioButton.image = [UIImage imageNamed:@"mm_onboarding_icon_circlestroke.png"];
    }
    
}



@end
