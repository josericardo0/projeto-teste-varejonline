import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovimentationService {
  private baseUrl = 'http://localhost:8081';

  constructor(private http: HttpClient) {}

  getMovements(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/movimentacoes`);
  }

  createMovement(movement: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/movimentacoes`, movement);
  }

  getMovementById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/movimentacoes/${id}`);
  }

  updateMovement(id: number, movement: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/movimentacoes/${id}`, movement);
  }

  deleteMovement(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/movimentacoes/${id}`);
  }
}
