import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StockMovementListService {
  private baseUrl = 'http://localhost:8081';

  constructor(private http: HttpClient) {}

  getAllMovements(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/movimentos`);
  }

  getMovementById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/movimentos/${id}`);
  }

  // Adicione métodos para atualização, exclusão ou outras operações conforme necessário
}
