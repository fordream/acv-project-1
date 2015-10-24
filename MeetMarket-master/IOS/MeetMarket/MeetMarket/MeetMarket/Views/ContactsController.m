//
//  UIViewController+HomeViewController.m
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "ContactsController.h"

@implementation ContactsController
NSMutableArray *meetings;
NSArray *contacts;
#pragma mark-View Life Cycle
-(void)viewDidLoad{
    [super viewDidLoad];
    


    contacts = [self getAllContacts];
    self.ContactTable.delegate = self;
    self.ContactTable.dataSource = self;
}



- (BOOL)prefersStatusBarHidden {
    return YES;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
}


- (IBAction)BackClicked:(id)sender {
    [self.navigationController popViewControllerAnimated:true];
}

#pragma mark-View Table View

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 61;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [contacts count];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    Contacts *contact = [contacts objectAtIndex:indexPath.row];
    contact.isSelected = !contact.isSelected;
    [self.ContactTable reloadData];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *cellID = @"ContactsCellId";
    ContactsCell* cell = (ContactsCell*)[tableView dequeueReusableCellWithIdentifier:cellID];
    if (!cell) {
        
        NSArray *topLevelObjectsProducts = [[NSBundle mainBundle] loadNibNamed:@"ContactsCell" owner:self options:nil];
        
        cell = (ContactsCell *)[topLevelObjectsProducts objectAtIndex:0];
        
    }
    [cell SetupCell:[contacts objectAtIndex:indexPath.row]];
    return cell;
}


-(NSArray *)getAllContacts
{
    
    CFErrorRef *error = nil;
    
    
    ABAddressBookRef addressBook = ABAddressBookCreateWithOptions(NULL, error);
    
    __block BOOL accessGranted = NO;
    if (ABAddressBookRequestAccessWithCompletion != NULL) { // we're on iOS 6
        dispatch_semaphore_t sema = dispatch_semaphore_create(0);
        ABAddressBookRequestAccessWithCompletion(addressBook, ^(bool granted, CFErrorRef error) {
            accessGranted = granted;
            dispatch_semaphore_signal(sema);
        });
        dispatch_semaphore_wait(sema, DISPATCH_TIME_FOREVER);
        
    }
    else { // we're on iOS 5 or older
        accessGranted = YES;
    }
    
    if (accessGranted) {
        
#ifdef DEBUG
        NSLog(@"Fetching contact info ----> ");
#endif
        
        
        ABAddressBookRef addressBook = ABAddressBookCreateWithOptions(NULL, error);
        ABRecordRef source = ABAddressBookCopyDefaultSource(addressBook);
        CFArrayRef allPeople = ABAddressBookCopyArrayOfAllPeopleInSourceWithSortOrdering(addressBook, source, kABPersonSortByFirstName);
        CFIndex nPeople = CFArrayGetCount(allPeople);
        NSMutableArray* items = [[NSMutableArray alloc]init];
        
        
        for (int i = 0; i < nPeople; i++)
        {
            ABRecordRef person = CFArrayGetValueAtIndex(allPeople, i);
            
            NSAssert(person, @"Non-person detected!");

            Contacts *contacts = [[Contacts alloc] init];
            
        
            
            if((__bridge NSString*)ABRecordCopyValue(person, kABPersonFirstNameProperty) != nil)
            {
                
                
            contacts.FirstName = (__bridge NSString*)ABRecordCopyValue(person, kABPersonFirstNameProperty);
            }
            
            if((__bridge NSString*)ABRecordCopyValue(person, kABPersonLastNameProperty) != nil)
            {
            
            contacts.LastName = (__bridge NSString*)ABRecordCopyValue(person, kABPersonLastNameProperty);
            }
            
            ABMultiValueRef multiEmails = ABRecordCopyValue(person, kABPersonEmailProperty);
            
            for (CFIndex i=0; i<ABMultiValueGetCount(multiEmails); i++) {
                CFStringRef contactEmailRef = ABMultiValueCopyValueAtIndex(multiEmails, i);
                NSString *contactEmail = (__bridge NSString *)contactEmailRef;
                contacts.Email = contactEmail;

                // NSLog(@"All emails are:%@", contactEmails);
                
            }
            
            if (contacts.Email != nil)
            {
            
            [items addObject:contacts];
            }
        }
        NSLog(@"%@",items);
        return items;
        
        
        
    } else {
#ifdef DEBUG
        NSLog(@"Cannot fetch Contacts :( ");
#endif
        return NO;
        
        
    }
    
}

- (IBAction)SendClicked:(id)sender {
    Reachability *networkReachability = [Reachability reachabilityForInternetConnection];
    NetworkStatus networkStatus = [networkReachability currentReachabilityStatus];
    if (networkStatus == NotReachable) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No Connection"
                                                        message:@"Please connect to the internet"
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        return;
    }
    NSPredicate *pred = [NSPredicate predicateWithFormat:@"isSelected == YES"];
    NSArray *SelectedContacts = [contacts filteredArrayUsingPredicate:pred];
    if ([SelectedContacts count] == 0) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"No Contacts Selected"
                                                        message:@"Please select some contacts"
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
        return;
    }
    
    // Convert string to date object
    NSDateFormatter *timeFormat = [[NSDateFormatter alloc] init];
    [timeFormat setDateFormat:@"HH:mm a"];
    
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"dd/MM/yyyy"];
    
    //add meeting and all invities to db
    Meeting *myMeeting = [[Meeting alloc]init];
    myMeeting.MeetingDescription = self.OptionalText;
    myMeeting.StartTime = [timeFormat dateFromString:self.StartTime];
    myMeeting.EndTime = [timeFormat dateFromString:self.EndTime];
    myMeeting.MeetingDate = [dateFormat dateFromString:self.Date];
    NSNumberFormatter *f = [[NSNumberFormatter alloc] init];
    f.numberStyle = NSNumberFormatterDecimalStyle;
    myMeeting.NumberOfSlots = [f numberFromString:self.MettingSlots];
    
    myMeeting.Invitees = [[NSMutableArray alloc]init];
    
    for (int i = 0; i < [SelectedContacts count]; i++) {
        Contacts *contact = [SelectedContacts objectAtIndex:i];
        NSString *InviteUUID = [[NSUUID UUID] UUIDString];
        
        
        Invitee *invitee = [[Invitee alloc]init];
        invitee.ID = InviteUUID;
        invitee.FirstName = contact.FirstName;
        invitee.LastName = contact.LastName;
        invitee.Email = contact.Email;
        
        NSDictionary *inventory = @{
                                    @"ID" : InviteUUID,
                                    @"FirstName" : contact.FirstName,
                                    @"LastName" : contact.LastName,
                                    @"Email" : contact.Email,
                                    };
        
        [myMeeting.Invitees addObject:inventory];
        
        //send invites out to all selected contacts

        
        NSMutableArray *ToEmails = [[NSMutableArray alloc]init];
        NSString *toEmail = contact.Email;
        [ToEmails addObject:toEmail];
        
        NSMutableArray *ToNames = [[NSMutableArray alloc]init];
        NSString *ToName = [NSString stringWithFormat:@"%@ %@",contact.FirstName,contact.LastName];
        [ToNames addObject:ToName];
        
        NSString *userName = @"";
        NSString *meetingTime = self.Date;
        NSString *meetingArea = self.Location;
        
        NSString *subject = [NSString stringWithFormat:@"%@ is free for a meeting %@!", userName, meetingTime];
        NSString *body = [NSString stringWithFormat:@"Hey There! </br></br> %@ is looking to meet someone in %@ between %@ and %@ today and we think you might be a great match! </br></br> Are you interested? </br></br></br> <form action='http://google.com'><button type='submit' style='height:50px; width:50px'>Yes</button></form>", userName, meetingArea, self.StartTime, self.EndTime];
        
        
        //send emails to each invitee
        [PFCloud callFunctionInBackground:@"sendMail"
                           withParameters:@{@"toEmails": ToEmails,
                                            @"toNames": ToNames,
                                            @"body": body,
                                            @"subject": subject,
                                            @"from_email": @"noreply@meetmarket.com",
                                            @"from_name": @"Meet Market"}
                                    block:^(NSArray *results, NSError *error) {
                                        if (!error) {
                                            // this is where you handle the results and change the UI.
                                            
                                            
                                        }
                                    }];
    }
    PFUser *currentUser = [PFUser currentUser];
    
    PFObject *meetingObject = [PFObject objectWithClassName:@"meeting"];
    meetingObject[@"UserId"] = currentUser.objectId;
    meetingObject[@"MeetingDescription"] = myMeeting.MeetingDescription;
    meetingObject[@"StartTime"] = myMeeting.StartTime;
    meetingObject[@"EndTime"] = myMeeting.EndTime;
    meetingObject[@"MeetingDate"] = myMeeting.MeetingDate;
    meetingObject[@"NumberOfSlots"] = myMeeting.NumberOfSlots;
    meetingObject[@"Location"] = self.Location;
    meetingObject[@"Invitees"] = myMeeting.Invitees;
    [meetingObject saveInBackground];
    
    
    
    
    int count = [self.navigationController.viewControllers count];
    [self.navigationController popToViewController:[self.navigationController.viewControllers objectAtIndex:count-3] animated:true];
}
@end
