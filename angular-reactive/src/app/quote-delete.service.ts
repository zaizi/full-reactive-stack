import { Injectable } from '@angular/core';

import {HttpClient, HttpErrorResponse, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class QuoteDeleteService {

  url = 'http://localhost:8080/quotes-delete';

  constructor(private http: HttpClient) {}

  deleteQuote (quoteId: number): Observable<{}> {
    const url = `${this.url}`; // DELETE api/heroes/42
    const httpParams = new HttpParams().set('quoteId', quoteId.toString());
    const options = { params: httpParams };
    return this.http.delete(url, options);
  }

}
