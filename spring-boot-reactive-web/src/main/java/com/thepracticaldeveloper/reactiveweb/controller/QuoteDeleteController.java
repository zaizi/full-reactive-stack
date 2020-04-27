package com.thepracticaldeveloper.reactiveweb.controller;

import com.thepracticaldeveloper.reactiveweb.repository.QuoteMongoBlockingRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteDeleteController {

    private QuoteMongoBlockingRepository quoteMongoBlockingRepository;

    public QuoteDeleteController(final QuoteMongoBlockingRepository quoteMongoBlockingRepository) {
        this.quoteMongoBlockingRepository = quoteMongoBlockingRepository;
    }

    @DeleteMapping("/quotes-delete")
    public void deleteQuote(final @RequestParam(name = "quoteId") String quoteId) {
        quoteMongoBlockingRepository.deleteById(quoteId);
    }

}
