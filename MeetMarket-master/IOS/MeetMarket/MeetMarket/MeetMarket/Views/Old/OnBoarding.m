//
//  UIViewController+OnBoarding.m
//  MeetMarket
//
//  Created by Jacob Keller on 1/05/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "OnBoarding.h"

@implementation OnBoarding
int currX;


#pragma mark-View Life Cycle
-(void)viewDidLoad{
    [super viewDidLoad];
    self.OnboardingScrollView.delegate = self;
    [self.OnboardingScrollView setContentSize:CGSizeMake(960, self.OnboardingScrollView.contentSize.height)];
    

}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    
    
}
- (BOOL)prefersStatusBarHidden {
    return YES;
}

#pragma mark-View LinkedIn
- (IBAction)SignInClicked:(id)sender {
    HomeViewController * view = [[HomeViewController alloc]initWithNibName:@"HomeViewController" bundle:nil];
    [self.navigationController pushViewController:view animated:true];
}

#pragma mark-View Scroll View
-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
    currX = self.OnboardingScrollView.contentOffset.x;
}
-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    if(self.OnboardingScrollView.contentOffset.x == currX ) {
        return;//This is what I needed, If I haven't panned to another view return
    }
    int myOffset = self.OnboardingScrollView.contentOffset.x;
    if (myOffset == 0) {
        self.LeftDot.image = [UIImage imageNamed:@"mm_onboarding_icon_circlestroke.png"];
        self.MiddleDot.image = [UIImage imageNamed:@"mm_onboarding_icon_circlefull.png"];
        self.RightDot.image = [UIImage imageNamed:@"mm_onboarding_icon_circlefull.png"];
    } else if (myOffset == 320) {
        self.LeftDot.image = [UIImage imageNamed:@"mm_onboarding_icon_circlefull.png"];
        self.MiddleDot.image = [UIImage imageNamed:@"mm_onboarding_icon_circlestroke.png"];
        self.RightDot.image = [UIImage imageNamed:@"mm_onboarding_icon_circlefull.png"];
    } else {
        self.LeftDot.image = [UIImage imageNamed:@"mm_onboarding_icon_circlefull.png"];
        self.MiddleDot.image = [UIImage imageNamed:@"mm_onboarding_icon_circlefull.png"];
        self.RightDot.image = [UIImage imageNamed:@"mm_onboarding_icon_circlestroke.png"];
    }
}
@end
