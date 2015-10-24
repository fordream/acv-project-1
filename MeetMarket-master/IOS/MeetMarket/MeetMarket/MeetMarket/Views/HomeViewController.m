//
//  UIViewController+HomeViewController.m
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "HomeViewController.h"

@implementation HomeViewController
NSMutableArray *meetings;
NSMutableArray *invitees;
TopBarController *topBar;
#pragma mark-View Life Cycle
-(void)viewDidLoad{
    [super viewDidLoad];
    
    
    topBar = [[TopBarController alloc]initWithNibName:@"TopBarController" bundle:nil];
//    topBar.ParentView = self;
    [self.NavBarView addSubview:topBar.view];
    self.MeetingTableView.delegate = self;
    self.MeetingTableView.dataSource = self;
    meetings = [[NSMutableArray alloc]init];
    invitees = [[NSMutableArray alloc]init];
}



- (BOOL)prefersStatusBarHidden {
    return YES;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    meetings = [[NSMutableArray alloc]init];
    invitees = [[NSMutableArray alloc]init];
    PFUser *user = [PFUser currentUser];
    PFQuery *query = [PFQuery queryWithClassName:@"meeting"];
    [query whereKey:@"UserId" equalTo:user.objectId];
    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        if (!error) {
            // The find succeeded.
            NSLog(@"Successfully retrieved %lu scores.", (unsigned long)objects.count);
            // Do something with the found objects
            for (PFObject *object in objects) {
                NSComparisonResult result = [[NSDate date] compare:[self endOfDay:object[@"MeetingDate"]]];
                if (![meetings containsObject:object] && result==NSOrderedAscending) {
                    [meetings addObject:object];
                    for (NSMutableDictionary *dict in [object valueForKey:@"Invitees"]){
                        if ([dict valueForKey:@"slot"] > 0) {
                            if (![invitees containsObject:dict]) {
                                [invitees addObject:dict];
                            }
                        }
                        
                    }
                }
                
            }
            [self.MeetingTableView reloadData];
        } else {
            // Log details of the failure
            NSLog(@"Error: %@ %@", error, [error userInfo]);
        }
    }];
    
}

-(NSDate *)endOfDay:(NSDate *)date
{
    NSCalendar *cal = [NSCalendar currentCalendar];
    NSDateComponents *components = [cal components:( NSMonthCalendarUnit | NSYearCalendarUnit | NSDayCalendarUnit | NSHourCalendarUnit | NSMinuteCalendarUnit | NSSecondCalendarUnit ) fromDate:date];
    
    [components setHour:23];
    [components setMinute:59];
    [components setSecond:59];
    
    return [cal dateFromComponents:components];
    
}


#pragma mark-View Table View

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 96;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [invitees count] + 1;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.row == [invitees count]) {
        CreateMeetingController *view = [[CreateMeetingController alloc]initWithNibName:@"CreateMeetingController" bundle:nil];
        [self.navigationController pushViewController:view animated:true];
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *cellID = @"MeetingCellId";
    MeetingCell* cell = (MeetingCell*)[tableView dequeueReusableCellWithIdentifier:cellID];
    if (!cell) {
        
        NSArray *topLevelObjectsProducts = [[NSBundle mainBundle] loadNibNamed:@"MeetingCell" owner:self options:nil];
        
        cell = (MeetingCell *)[topLevelObjectsProducts objectAtIndex:0];
        
    }
    if (indexPath.row == [invitees count])
        [cell SetupLastCell];
    else{
        NSDictionary *myInvitee = [invitees objectAtIndex:indexPath.row];
        [cell SetupCell:[meetings objectAtIndex:indexPath.row] withInvitee:myInvitee];
    }
    return cell;
}


- (IBAction)LogOutClicked:(id)sender {
    [PFUser logOut];
    PFUser *currentUser = [PFUser currentUser]; // this will now be nil
    LoginController *view = [[LoginController alloc]initWithNibName:@"LoginController" bundle:nil];
    [self.navigationController pushViewController:view animated:true];
//    [self.navigationController popToRootViewControllerAnimated:true];
}
@end
