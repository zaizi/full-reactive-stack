import { Injectable } from '@angular/core';

import {HttpClient} from "@angular/common/http";

@Injectable()
export class QuoteDeleteService {

  url = 'http://localhost:8080/quotes-delete';

  constructor(private http: HttpClient) {}

  deleteQuote(quoteId?: number) {
    return this.http.delete(this.url + '?quoteId=' + quoteId);
  }

}
