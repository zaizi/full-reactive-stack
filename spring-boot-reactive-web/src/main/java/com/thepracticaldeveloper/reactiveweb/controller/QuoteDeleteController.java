package com.thepracticaldeveloper.reactiveweb.controller;

import com.thepracticaldeveloper.reactiveweb.repository.QuoteMongoReactiveRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuoteDeleteController {

    private QuoteMongoReactiveRepository quoteMongoReactiveRepository;

    public QuoteDeleteController(final QuoteMongoReactiveRepository quoteMongoReactiveRepository) {
        this.quoteMongoReactiveRepository = quoteMongoReactiveRepository;
    }

    @DeleteMapping("/quotes-delete")
    public void deleteQuote(final @RequestParam(name = "quoteId") String quoteId) throws Exception {
        quoteMongoReactiveRepository.deleteById(quoteId);
    }

}
