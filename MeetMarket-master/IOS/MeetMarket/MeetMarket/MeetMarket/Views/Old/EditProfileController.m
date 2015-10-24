//
//  UIViewController+HomeViewController.m
//  MeetMarket
//
//  Created by Jacob Keller on 30/04/2015.
//  Copyright (c) 2015 BlondGorilla. All rights reserved.
//

#import "EditProfileController.h"

@implementation EditProfileController
int TextLimit = 256;
#pragma mark-View Life Cycle
-(void)viewDidLoad{
    [super viewDidLoad];
    

    UITapGestureRecognizer *closeKeyboardTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(CloseKeyboard:)];
    [[self view] addGestureRecognizer:closeKeyboardTap];
    
    UITapGestureRecognizer *ProfileImageTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(ProfileImageTaped:)];
    [[self ProfileImage] addGestureRecognizer:ProfileImageTap];
    
    self.AboutText.delegate = self;
    self.CharacterCount.text = [NSString stringWithFormat:@"%lu Characters Remaining", TextLimit-(unsigned long)[self.AboutText.text length]];
    
    
    [self.BisInt1 sizeToFit];
    [self.BisInt1 layoutIfNeeded];
    CGRect frame = [self.BisBtn1 frame];
    frame.origin.x = self.BisInt1.frame.size.width + self.BisInt1.frame.origin.x + 8;
    self.BisBtn1.frame = frame;
    [self.BisInt2 sizeToFit];
    [self.BisInt2 layoutIfNeeded];
    frame = [self.BisBtn2 frame];
    frame.origin.x = self.BisInt2.frame.size.width + self.BisInt2.frame.origin.x + 8;
    self.BisBtn2.frame = frame;
    [self.BisInt3 sizeToFit];
    [self.BisInt3 layoutIfNeeded];
    frame = [self.BisBtn3 frame];
    frame.origin.x = self.BisInt3.frame.size.width + self.BisInt3.frame.origin.x + 8;
    self.BisBtn3.frame = frame;
    
    
    
    
    
}

- (void)textViewDidChange:(UITextView *)textView{
    self.CharacterCount.text = [NSString stringWithFormat:@"%lu Characters Remaining", TextLimit-(unsigned long)[textView.text length]];
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    return self.AboutText.text.length + text.length - range.length < TextLimit + 1;
}

- (BOOL)prefersStatusBarHidden {
    return YES;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
}

-(void)CloseKeyboard:(UIGestureRecognizer*)gesture{
    [self.AboutText resignFirstResponder];
    [self.BisInt1 becomeFirstResponder];
    [self.BisInt2 becomeFirstResponder];
    [self.BisInt3 becomeFirstResponder];
    
    self.BisInt1.userInteractionEnabled = false;
    self.BisInt2.userInteractionEnabled = false;
    self.BisInt3.userInteractionEnabled = false;
    
    [self.BisInt1 sizeToFit];
    [self.BisInt1 layoutIfNeeded];
    CGRect frame = [self.BisBtn1 frame];
    frame.origin.x = self.BisInt1.frame.size.width + self.BisInt1.frame.origin.x + 8;
    self.BisBtn1.frame = frame;
    [self.BisInt2 sizeToFit];
    [self.BisInt2 layoutIfNeeded];
    frame = [self.BisBtn2 frame];
    frame.origin.x = self.BisInt2.frame.size.width + self.BisInt2.frame.origin.x + 8;
    self.BisBtn2.frame = frame;
    [self.BisInt3 sizeToFit];
    [self.BisInt3 layoutIfNeeded];
    frame = [self.BisBtn3 frame];
    frame.origin.x = self.BisInt3.frame.size.width + self.BisInt3.frame.origin.x + 8;
    self.BisBtn3.frame = frame;

}

-(void)ProfileImageTaped:(UIGestureRecognizer*)gesture{
    UIImagePickerController *pickerLibrary = [[UIImagePickerController alloc] init];
    pickerLibrary.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    pickerLibrary.delegate = self;
    [self presentModalViewController:pickerLibrary animated:YES];
}

- (IBAction)Back:(id)sender {
    [self.navigationController popViewControllerAnimated:true];
}

- (void) imagePickerController:(UIImagePickerController *)picker didFinishPickingImage:(UIImage *)image editingInfo:(NSDictionary *)editingInfo
{
    [self.ProfileImage setImage:image];
    [picker dismissViewControllerAnimated:true completion:nil];
}
- (IBAction)BisIntClicked:(id)sender {
    UIButton * btn=(UIButton*)sender;
    self.BisInt1.userInteractionEnabled = false;
    self.BisInt2.userInteractionEnabled = false;
    self.BisInt3.userInteractionEnabled = false;

    if ([btn tag] == 1) {
        self.BisInt1.userInteractionEnabled = true;
        [self.BisInt1 becomeFirstResponder];
    } else if ([btn tag] == 2) {
        self.BisInt2.userInteractionEnabled = true;
        [self.BisInt2 becomeFirstResponder];
    } else if ([btn tag] == 3) {
        self.BisInt3.userInteractionEnabled = true;
        [self.BisInt3 becomeFirstResponder];
    }
}


@end
