//
//  UIViewController+HomeViewController.h
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface  EditProfileController:UIViewController<UIImagePickerControllerDelegate, UITextViewDelegate>
@property (strong, nonatomic) IBOutlet UIView *view;
- (IBAction)Back:(id)sender;
@property (strong, nonatomic) IBOutlet UILabel *CharacterCount;
@property (strong, nonatomic) IBOutlet UITextView *AboutText;
@property (strong, nonatomic) IBOutlet UIImageView *ProfileImage;
@property (strong, nonatomic) IBOutlet UITextField *BisInt1;
@property (strong, nonatomic) IBOutlet UITextField *BisInt2;
@property (strong, nonatomic) IBOutlet UITextField *BisInt3;
- (IBAction)BisIntClicked:(id)sender;
- (IBAction)BisIntChanged:(id)sender;
@property (strong, nonatomic) IBOutlet UIButton *BisBtn1;
@property (strong, nonatomic) IBOutlet UIButton *BisBtn2;
@property (strong, nonatomic) IBOutlet UIButton *BisBtn3;

@end
