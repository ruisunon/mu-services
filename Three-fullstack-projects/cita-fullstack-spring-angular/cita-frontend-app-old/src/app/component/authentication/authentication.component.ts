
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LoginRequest } from 'src/app/model/request/login-request';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { CredentialService } from 'src/app/service/credential.service';
import { ErrorHandlerService } from 'src/app/service/error-handler.service';

@Component({
  selector: 'app-authentication',
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.component.scss']
})
export class AuthenticationComponent implements OnInit {
  
  public randomImgUrl!: string;
  public msg!: string;
  public registeredUsername!: string;
  
  constructor(private authenticationService: AuthenticationService,
    private credentialService: CredentialService,
    private  activatedRoute: ActivatedRoute,
    private errorHandlerService: ErrorHandlerService) {}
  
  ngOnInit(): void {
    this.randomImgUrl = this.generateRandomImageUrl();
    this.writeRegisteredUsernameToForm();
  }
  
  private writeRegisteredUsernameToForm(): void {
    this.activatedRoute.queryParams.subscribe({
      next: (p: any) => this.registeredUsername = p.username
    });
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
    
    if (action === "login")
      button.setAttribute("data-bs-target", "#login");
    
    const mainContainer = document.getElementById("main-container");
    mainContainer?.appendChild(button);
    button.click();
  }
  
  public onLogin(loginRequest: LoginRequest): void {
    this.authenticationService.authenticate(loginRequest).subscribe({
      next: (payload: any) => {
        sessionStorage.setItem("username", payload?.responseBody?.username);
        sessionStorage.setItem("jwtToken", payload?.responseBody?.jwtToken);
        
        this.credentialService.findByUsername(payload.responseBody.username).subscribe({
          next: (credentialPayload: any) => {
            
            const userRole: string = this.credentialService.getUserRole(credentialPayload?.responseBody?.role);
            sessionStorage.setItem("userRole", userRole.toUpperCase());
            
            window.location.replace(`/workspace/${userRole}/index`);
          },
          error: (errorResponse: HttpErrorResponse) => 
              this.errorHandlerService.extractExceptionMsg(errorResponse)
        });
        
      },
      error: (errorResponse: HttpErrorResponse) => {
        const exceptionMsg = this.errorHandlerService.extractExceptionMsg(errorResponse);
        this.msg = exceptionMsg?.errorMsg;
        this.onOpenModal('login');
      }
    });
  }
  
  
  
}








