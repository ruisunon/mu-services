
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Category } from 'src/app/model/category';
import { CategoryRequest } from 'src/app/model/request/category-request';
import { PageResponse } from 'src/app/model/response/page/page-response';
import { ToastrMsg } from 'src/app/model/toastr-msg';
import { CategoryService } from 'src/app/service/category.service';
import { CredentialService } from 'src/app/service/credential.service';
import { EmployeeService } from 'src/app/service/employee.service';
import { ManagerCategoryService } from 'src/app/service/employee/manager/manager-category.service';
import { ErrorHandlerService } from 'src/app/service/error-handler.service';
import { NotificationService } from 'src/app/service/notification.service';

@Component({
  selector: 'app-manager-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {
  
  public accountUrl!: string;
  public categories!: PageResponse;
  public category!: Category;
  public categoryRequest: CategoryRequest = new CategoryRequest(0, "", null, 0);
  
  constructor(private credentialService: CredentialService,
    private employeeService: EmployeeService,
    private categoryService: CategoryService,
    private managerCategoryService: ManagerCategoryService,
    private notificationService: NotificationService,
    private errorHandlerService: ErrorHandlerService) {}
  
  ngOnInit(): void {
    this.accountUrl = this.credentialService.getUserRole(`${sessionStorage.getItem("userRole")}`);
    this.fetchAll();
  }
  
  private fetchAll(): void {
    this.managerCategoryService.fetchAll().subscribe({
      next: (payload: any) => {
        this.categories = payload?.responseBody;
      },
      error: (errorResponse: HttpErrorResponse) =>
        this.errorHandlerService.extractExceptionMsg(errorResponse)
    });
  }
  
  public onDisplayAdd(): void {
    this.employeeService.findByCredentialUsername(`${sessionStorage.getItem(`username`)}`).subscribe({
      next: (managerPayload: any) => {
        this.onOpenModal('addCategory');
        this.categoryService.findAllBySaloonId(managerPayload?.responseBody?.saloon?.id).subscribe({
          next: (allSaloonCategoriesPayload: any) => {
            this.categories = allSaloonCategoriesPayload?.responseBody;
          },
          error: (errorResponse: HttpErrorResponse) =>
            this.errorHandlerService.extractExceptionMsg(errorResponse)
        });
      },
      error: (errorResponse: HttpErrorResponse) =>
        this.errorHandlerService.extractExceptionMsg(errorResponse)
    });
  }

  public onAdd(ngForm: NgForm): void {
    this.categoryRequest.name = ngForm?.value?.name;
    this.employeeService.findByCredentialUsername(`${sessionStorage.getItem(`username`)}`).subscribe({
      next: (managerPayload: any) => {
        this.categoryRequest.parentCategoryId = ngForm?.value?.parentCategoryId;
        this.categoryRequest.saloonId = managerPayload?.responseBody?.saloon?.id;
        this.managerCategoryService.saveCategory(this.categoryRequest).subscribe({
          next: (savedCategoryPayload: any) => {
            document.getElementById('addCategory')?.click();
            ngForm.reset();
            this.fetchAll();
            this.notificationService.showSuccess(new ToastrMsg(`Category has been added successfully..`, `Added`));
          },
          error: (errorResponse: HttpErrorResponse) =>
            this.errorHandlerService.extractExceptionMsg(errorResponse)
        });
      },
      error: (errorResponse: HttpErrorResponse) =>
        this.errorHandlerService.extractExceptionMsg(errorResponse)
    });
  }
  
  public onDisplayUpdate(category: Category): void {
    this.employeeService.findByCredentialUsername(`${sessionStorage.getItem(`username`)}`).subscribe({
      next: (managerPayload: any) => {
        // this.categoryRequest.saloonId = parseInt(managerPayload?.responseBody?.saloon?.id);
        this.categoryService.findAllBySaloonId(managerPayload?.responseBody?.saloon?.id).subscribe({
          next: (allSaloonCategoriesPayload: any) => {
            this.onOpenModal('updateCategory');
            this.category = category;
            this.categories = allSaloonCategoriesPayload?.responseBody;
          },
          error: (errorResponse: HttpErrorResponse) =>
            this.errorHandlerService.extractExceptionMsg(errorResponse)
        });
      },
      error: (errorResponse: HttpErrorResponse) =>
        this.errorHandlerService.extractExceptionMsg(errorResponse)
    });
  }

  public onUpdate(ngForm: NgForm): void {
    this.categoryRequest.categoryId = parseInt(ngForm?.value?.categoryId);
    this.categoryRequest.name = ngForm?.value?.name;
    this.categoryRequest.parentCategoryId = parseInt(ngForm?.value?.parentCategoryId);
    this.employeeService.findByCredentialUsername(`${sessionStorage.getItem(`username`)}`).subscribe({
      next: (managerPayload: any) => {
        this.categoryRequest.saloonId = parseInt(managerPayload?.responseBody?.saloon?.id);
        this.managerCategoryService.updateCategory(this.categoryRequest).subscribe({
          next: (updatedCategoryPayload: any) => {
            this.fetchAll();
            this.notificationService.showSuccess(new ToastrMsg(`Category has been updated successfully..`, `Updated`));
          },
          error: (errorResponse: HttpErrorResponse) =>
            this.errorHandlerService.extractExceptionMsg(errorResponse)
        });
      },
      error: (errorResponse: HttpErrorResponse) =>
        this.errorHandlerService.extractExceptionMsg(errorResponse)
    });
    
    /*
    this.managerCategoryService.updateCategory(this.categoryRequest).subscribe({
      next: (updatedCategoryPayload: any) => {
        this.categoryRequest = new CategoryRequest(0, "", null, 0);
        this.fetchAll();
        this.notificationService.showSuccess(new ToastrMsg(`Category updated successfully..`, `Updated!`));
      },
      error: (errorResponse: HttpErrorResponse) =>
        this.errorHandlerService.extractExceptionMsg(errorResponse)
    });
    */
  }
  
  public onDelete(categoryId: number): void {
    if (confirm(`Deleting category will delete all sub categories & services!\nDo you confirm deletion ?`))
      this.managerCategoryService.deleteCategory(categoryId).subscribe({
        next: (payload: any) => {
          if (payload?.responseBody && payload?.responseBody === false)
            this.notificationService.showInfo(new ToastrMsg(`Not able to delete this category`, `Not Successfull!`));
          else {
            this.fetchAll();
            this.notificationService.showSuccess(new ToastrMsg(`Category has been deleted successfully..`, `Removed!`));
          }
        },
        error: (errorResponse: HttpErrorResponse) =>
          this.errorHandlerService.extractExceptionMsg(errorResponse)
      });
  }
  
  public onOpenModal(action: string): void {
    const button = document.createElement("button");
    button.type = "button";
    button.style.display = "none";
    button.setAttribute("data-bs-toggle", "modal");
    
    if (action === "addCategory")
      button.setAttribute("data-bs-target", "#addCategory");
    else if (action === "updateCategory")
      button.setAttribute("data-bs-target", "#updateCategory");

    const mainContainer = document.getElementById("main-container");
    mainContainer?.appendChild(button);
    button.click();
  }
  
  public searchBy(key: string): void {
    const res: Category[] = [];
    this.categories?.content.forEach((c: Category) => {
      if (c?.name?.toLowerCase()?.indexOf(key?.toLowerCase()) !== -1
          // || c?.parentCategory?.name?.toLowerCase()?.indexOf(key?.toLowerCase()) !== -1
          || c?.saloon?.name?.toLowerCase()?.indexOf(key?.toLowerCase()) !== -1
          || c?.saloon?.code?.toLowerCase()?.indexOf(key?.toLowerCase()) !== -1)
        res.push(c);
    });
    this.categories.content = res;
    if (!key)
      this.fetchAll();
  }
  
  
  
}











