package com.codeemma.revolut.endpoint;

import com.codeemma.revolut.account.Account;
import com.codeemma.revolut.account.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class AccountEndpointHandler {
    private Logger logger = Logger.getLogger(getClass().getName());

    private AccountService accountService;

    public AccountEndpointHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    //URI - transfer?from={origin}&to={destination}&amount={amount}
    public void handleTransfer(HttpExchange exchange) throws IOException {
        Map<String, String> queryMap = getQueryMap(exchange);

        String originatingAccountNumber = queryMap.get("from");
        String destinationAccountNumber = queryMap.get("to");
        BigDecimal amount =  new BigDecimal(queryMap.get("amount"));

        logger.info(String.format("transfer from <%s> to <%s>, amount <%s> ",originatingAccountNumber, destinationAccountNumber, amount));
        try {
            Account originator = accountService.transferFund(originatingAccountNumber, destinationAccountNumber, amount);
            String json = new ObjectMapper().writeValueAsString(originator);

            exchange.sendResponseHeaders(200,json.getBytes().length);
            writeToResponseBody(exchange,json);
        }catch (UnsupportedOperationException e){
            processBadRequest(exchange, e);
        }catch (NoSuchElementException e){
            processNotFound(exchange, e);
        }catch (Exception e){
            processInternalServerError(exchange, e);
        }

        exchange.close();

    }

    private void processInternalServerError(HttpExchange exchange, Exception e) throws IOException {
        String responseMessage = e.getClass().getName() + ": " + e.getMessage();
        exchange.sendResponseHeaders(500, responseMessage.getBytes().length);
        writeToResponseBody(exchange, responseMessage);
        return;
    }

    private void processNotFound(HttpExchange exchange, NoSuchElementException e) throws IOException {
        String responseMessage = e.getClass().getName() + ": " + e.getMessage();
        exchange.sendResponseHeaders(404, responseMessage.getBytes().length);
        writeToResponseBody(exchange, responseMessage);
    }

    private void processBadRequest(HttpExchange exchange, Exception e) throws IOException {
        String responseMessage = e.getClass().getName() + ": " + e.getMessage();
        exchange.sendResponseHeaders(400, responseMessage.getBytes().length);
        writeToResponseBody(exchange, responseMessage);
    }

    private void writeToResponseBody(HttpExchange exchange, String responseMessage) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseMessage.getBytes());
        outputStream.flush();
    }

    private Map<String, String> getQueryMap(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();

        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
