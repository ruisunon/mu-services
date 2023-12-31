
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { map, Observable } from 'rxjs';
import { DateBackendFormat } from 'src/app/model/date-backend-format';
import { ClientPageRequest } from 'src/app/model/request/client-page-request';
import { ReservationRequest } from 'src/app/model/request/reservation-request';
import { Reservation } from 'src/app/model/reservation';
import { ReservationStatus } from 'src/app/model/reservation-status';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerReservationService {
  
  private apiUrl: string = environment.API_URL;
  
  constructor(private http: HttpClient) {
    this.apiUrl = `${this.apiUrl}/customers/reservations`;
  }
  
  public fetchAllReservations(clientPageRequest: ClientPageRequest): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}`, {
      params: {
        offset: `${clientPageRequest.offset}`,
        size: `${clientPageRequest.size}`,
        sortBy: `${clientPageRequest?.sortBy?.join(`,`)}`,
        sortDirection: `${clientPageRequest?.sortDirection}`
      },
      headers: {
        UsernameAuth: `${sessionStorage.getItem(`username`)}`,
        Authorization: `Bearer ${sessionStorage.getItem(`jwtToken`)}`,
      }
    }).pipe(map((payload: any) => {
      payload.responseBody.customer.birthdate = new Date(payload?.responseBody?.customer?.birthdate);
      payload?.responseBody?.reservations?.content?.map((r: Reservation) => {
        r.startDate = moment(r?.startDate, DateBackendFormat.LOCAL_DATE_TIME).toDate();
        r.cancelDate = moment(r?.cancelDate, DateBackendFormat.LOCAL_DATE_TIME).toDate();
        r.completeDate = moment(r?.completeDate, DateBackendFormat.LOCAL_DATE_TIME).toDate();
      });
      return payload;
    }));
  }
  
  public searchAllByKey(key: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/search/${key.toLowerCase()}`, {
      headers: {
        UsernameAuth: `${sessionStorage.getItem(`username`)}`,
        Authorization: `Bearer ${sessionStorage.getItem(`jwtToken`)}`,
      }
    }).pipe(map((payload: any) => {
      payload.responseBody.customer.birthdate = moment(payload?.responseBody?.customer?.birthdate, DateBackendFormat.LOCAL_DATE).toDate();
      payload.responseBody.reservations?.content?.map((r: Reservation) => {
        r.startDate = moment(r?.startDate, DateBackendFormat.LOCAL_DATE_TIME).toDate();
        r.cancelDate = moment(r?.cancelDate, DateBackendFormat.LOCAL_DATE_TIME).toDate();
        r.completeDate = moment(r?.completeDate, DateBackendFormat.LOCAL_DATE_TIME).toDate();
      });
      return payload;
    }));
  }
  
  public cancelReservation(reservationId: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/cancel/${reservationId}`, null, {
      headers: {
        UsernameAuth: `${sessionStorage.getItem(`username`)}`,
        Authorization: `Bearer ${sessionStorage.getItem(`jwtToken`)}`,
      }
    })
    .pipe(map((payload: any) => {
      payload.responseBody.startDate = moment(payload?.responseBody?.startDate, DateBackendFormat.LOCAL_DATE_TIME).toDate();
      payload.responseBody.cancelDate = moment(payload?.responseBody?.cancelDate, DateBackendFormat.LOCAL_DATE_TIME).toDate();
      payload.responseBody.completeDate = moment(payload?.responseBody?.completeDate, DateBackendFormat.LOCAL_DATE_TIME).toDate();
    }));
  }
  
  public createReservation(reservationRequest: ReservationRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}`, reservationRequest, {
      headers: {
        UsernameAuth: `${sessionStorage.getItem(`username`)}`,
        Authorization: `Bearer ${sessionStorage.getItem(`jwtToken`)}`,
      }
    });
  }
  
  public getCompletedReservations(reservations: Reservation[]): Reservation[] {
    return reservations?.filter(r => r?.status === ReservationStatus.COMPLETED);
  }

  public getPendingReservations(reservations: Reservation[]): Reservation[] {
    return reservations?.filter(r => r?.status === ReservationStatus.NOT_STARTED
      || r?.status === ReservationStatus.IN_PROGRESS);
  }
  
  
  
}













