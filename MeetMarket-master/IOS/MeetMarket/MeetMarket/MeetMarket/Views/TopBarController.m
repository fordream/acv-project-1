//
//  UIViewController+HomeViewController.m
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "TopBarController.h"

@implementation TopBarController
#pragma mark-View Life Cycle


//- (IBAction)MenuClicked:(id)sender{
//    
//    
//    if (menu == nil) {
//        menu = [[MenuController alloc]initWithNibName:@"MenuController" bundle:nil];
//        menu.ParentView = self.ParentView;
//
//    }
//    CGRect frame = menu.view.frame;
//    
//    if([[self.ParentView.view viewWithTag:1].subviews count] == 0) {
//        [self.ParentView.view bringSubviewToFront:[self.ParentView.view viewWithTag:1]];
//
//        [self.RightBtn setImage:[UIImage imageNamed:@"mm_header_icon_menuclose.png"] forState:UIControlStateNormal];
//        frame.origin.x = 320;
//        menu.view.frame = frame;
//        [[self.ParentView.view viewWithTag:1] addSubview:menu.view];
//        
//        
//        frame.origin.x = 0;
//
//        [UIView animateWithDuration:0.2f
//                              delay:0.0f
//                            options:UIViewAnimationCurveEaseInOut
//                         animations:^{
//                             menu.view.frame = frame;
//                         }
//                         completion:^(BOOL finished) {
//                         }];
//    } else {
//        [self.RightBtn setImage:[UIImage imageNamed:@"mm_header_icon_menu.png"] forState:UIControlStateNormal];
//
//        frame.origin.x = 320;
//
//        [UIView animateWithDuration:0.2f
//                              delay:0.0f
//                            options:UIViewAnimationCurveEaseInOut
//                         animations:^{
//                             menu.view.frame = frame;
//                         }
//                         completion:^(BOOL finished) {
//                             [self.ParentView.view bringSubviewToFront:[self.ParentView.view viewWithTag:2]];
//                             [[self.ParentView.view viewWithTag:1].subviews makeObjectsPerformSelector: @selector(removeFromSuperview)];
//                         }];
//        
//        
//
//    }
//    
//}


@end
