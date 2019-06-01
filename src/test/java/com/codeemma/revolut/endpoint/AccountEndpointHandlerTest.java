package com.codeemma.revolut.endpoint;

import com.codeemma.revolut.MainApp;
import com.codeemma.revolut.account.Account;
import com.codeemma.revolut.account.AccountDao;
import org.junit.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class AccountEndpointHandlerTest {

    private static int randomPort;
    private  static MainApp mainApp;
    private AccountDao accountDao = new AccountDao();

    @BeforeClass
    public static void setUp() throws Exception {
        startServer();
    }


    @AfterClass
    public static void tearDown() throws Exception {
        stopServer();
    }

    @Test
    public void handleTransfer() throws Exception{
        String firstAcc = "123";
        String secondAcc = "765";
        BigDecimal amount = BigDecimal.valueOf(1000);
        accountDao.create(firstAcc,"first holder", BigDecimal.valueOf(2000));
        accountDao.create(secondAcc,"second holder", BigDecimal.valueOf(2000));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:"+randomPort+"/api/transfer?from="+firstAcc+"&to="+secondAcc+"&amount="+amount))
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Account updatedFirstAcc = accountDao.get(firstAcc);
        Account updatedSecondAcc = accountDao.get(secondAcc);

        assertThat(response.statusCode(), is(200));
        assertThat(updatedFirstAcc.getAccountBalance(),is(BigDecimal.valueOf(1000)));
        assertThat(updatedSecondAcc.getAccountBalance(),is(BigDecimal.valueOf(3000)));

    }

    private static void startServer() throws IOException {
        mainApp = new MainApp();
        Random random = new Random();
        randomPort  = random.nextInt(2000) + 1000;
        mainApp.setServerPort(randomPort);
        mainApp.startServer();
    }

    private static void stopServer() {
        mainApp.stopServer();
    }


}