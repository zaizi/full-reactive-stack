package com.thepracticaldeveloper.reactiveweb.controller;

import com.thepracticaldeveloper.reactiveweb.configuration.QuijoteDataLoader;
import com.thepracticaldeveloper.reactiveweb.domain.Quote;
import com.thepracticaldeveloper.reactiveweb.repository.QuoteMongoBlockingRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuoteBlockingControllerIntegrationTest {

    @MockBean
    private QuoteMongoBlockingRepository quoteMongoBlockingRepository;

    // This one is not needed, but we need to override the real one to prevent the default behavior
    @MockBean
    private QuijoteDataLoader quijoteDataLoader;

    @LocalServerPort
    private int serverPort;

    private RestTemplate restTemplate;

    private String serverBaseUrl;

    private List<Quote> quoteList;

    @Before
    public void setUp() {
        serverBaseUrl = "http://localhost:" + serverPort;
        restTemplate = new RestTemplate();
        quoteList = Lists.newArrayList(new Quote("1", "mock-book", "Quote 1"),
                new Quote("2", "mock-book", "Quote 2"),
                new Quote("3", "mock-book", "Quote 3"),
                new Quote("4", "mock-book", "Quote 4"));

        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            // NO-OP error handler so that we can assert client errors such as 404
            @Override
            public boolean hasError(final ClientHttpResponse clientHttpResponse) throws IOException {
                return false;
            }

            @Override
            public void handleError(final ClientHttpResponse clientHttpResponse) throws IOException {

            }
        });
    }

    @Test
    public void simpleGetRequest() {
        // given
        given(quoteMongoBlockingRepository.findAll()).willReturn(quoteList);

        // when
        ResponseEntity<List<Quote>> receivedQuoteList = restTemplate.exchange(serverBaseUrl + "/quotes-blocking",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Quote>>() {
                });

        // then
        assertThat(receivedQuoteList.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(receivedQuoteList.getBody()).isEqualTo(
                Lists.newArrayList(new Quote("1", "mock-book", "Quote 1"),
                        new Quote("2", "mock-book", "Quote 2"),
                        new Quote("3", "mock-book", "Quote 3"),
                        new Quote("4", "mock-book", "Quote 4")));
    }

    @Test
    public void pagedGetRequest() {
        // given
        given(quoteMongoBlockingRepository.retrieveAllQuotesPaged(PageRequest.of(1, 2)))
                .willReturn(quoteList.subList(0, 2));

        // when
        ResponseEntity<List<Quote>> receivedQuoteList = restTemplate.exchange(
                serverBaseUrl + "/quotes-blocking-paged?page=1&size=2",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Quote>>() {
                });

        // then
        assertThat(receivedQuoteList.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(receivedQuoteList.getBody()).isEqualTo(
                Lists.newArrayList(new Quote("1", "mock-book", "Quote 1"),
                        new Quote("2", "mock-book", "Quote 2")));
    }

    @Test
    public void shouldDeleteQuoteGivenValidId() {
        // Given
        String id = "3";
        Quote quoteToDelete = quoteList.stream().filter(quote -> quote.getId().equals(id)).findFirst().get();

        given(quoteMongoBlockingRepository.findById(id))
            .willReturn(Optional.of(quoteToDelete));

        String url = serverBaseUrl + "/quotes-blocking/" + id;

        // When
        ResponseEntity<Void> response = restTemplate.exchange(
            url, HttpMethod.DELETE, null, Void.class);

        // Then
        assertThat(response.getStatusCode())
            .as("HTTP status is 204")
            .isEqualTo(HttpStatus.NO_CONTENT);
        then(quoteMongoBlockingRepository).should().delete(quoteToDelete);
    }

    @Test
    public void shouldDeleteQuoteGivenInvalidId() {
        // Given
        String id = "13";
        given(quoteMongoBlockingRepository.findById(id))
            .willReturn(Optional.empty());

        String url = serverBaseUrl + "/quotes-blocking/" + id;

        // When
        ResponseEntity<Void> response = restTemplate.exchange(
            url, HttpMethod.DELETE, null, Void.class);

        // Then
        assertThat(response.getStatusCode())
            .as("HTTP status is 404")
            .isEqualTo(HttpStatus.NOT_FOUND);
        then(quoteMongoBlockingRepository).should(never()).delete(any(Quote.class));
    }
}
