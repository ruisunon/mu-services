
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ExceptionMsg } from 'src/app/model/exception-msg';
import { RegisterRequest } from 'src/app/model/request/register-request';
import { ErrorHandlerService } from 'src/app/service/error-handler.service';
import { RegistrationService } from 'src/app/service/registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {
  
  public randomImgUrl!: string;
  public msg!: string;
  
  constructor(private registrationService: RegistrationService,
    private router: Router,
    private errorHandlerService: ErrorHandlerService) {}
  
  ngOnInit(): void {
    this.randomImgUrl = this.generateRandomImageUrl();
  }
  
  private generateRandomImageUrl(): string {
    const numbers: number[] = [1, 2, 3, 4, 5, 6, 7, 8];
    let randomNumber: number = Math.floor(Math.random() * numbers.length);
    if (randomNumber === 0)
      ++randomNumber;
    return `https://bootdey.com/img/Content/avatar/avatar${randomNumber}.png`;
  }
  
  public onOpenModal(action: string): void {
    const button = document.createElement("button");
    button.type = "button";
    button.style.display = "none";
    button.setAttribute("data-bs-toggle", "modal");

    if (action === "register")
      button.setAttribute("data-bs-target", "#register");

    const mainContainer = document.getElementById("main-container");
    mainContainer?.appendChild(button);
    button.click();
  }
  
  public onRegister(ngForm: NgForm): void {
    const registerRequest: RegisterRequest = ngForm.value;
    this.registrationService.register(registerRequest).subscribe({
      next: (payload: any) => {
        alert(payload?.responseBody?.msg);
        ngForm.reset();
        this.router.navigateByUrl(`/authenticate?username=${registerRequest.username.toLowerCase()}`);
      },
      error: (errorResponse: HttpErrorResponse) => {
        const exceptionMsg: ExceptionMsg = this.errorHandlerService.extractExceptionMsg(errorResponse);
        this.msg = exceptionMsg?.errorMsg;
        this.onOpenModal('register');
      }
    });
  }
  
  
  
}









