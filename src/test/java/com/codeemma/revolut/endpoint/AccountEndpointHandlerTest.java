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
import java.util.concurrent.TimeUnit;

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
        HttpResponse response = transferOverHttp(firstAcc, secondAcc, amount);
        Account updatedFirstAcc = accountDao.get(firstAcc);
        Account updatedSecondAcc = accountDao.get(secondAcc);

        assertThat(response.statusCode(), is(200));
        assertThat(updatedFirstAcc.getAccountBalance(),is(BigDecimal.valueOf(1000)));
        assertThat(updatedSecondAcc.getAccountBalance(),is(BigDecimal.valueOf(3000)));

    }

    @Test
    public void handleTransferForMultiThreading() throws Exception{
        String firstAcc = "09812334";
        String secondAcc = "262749910";
        String thirdAcc =  "156388300";

        accountDao.create(firstAcc,"first holder", BigDecimal.valueOf(2000));
        accountDao.create(secondAcc,"second holder", BigDecimal.valueOf(2000));
        accountDao.create(thirdAcc,"third holder", BigDecimal.valueOf(2000));

        //multithreading
        Thread thread1 = new Thread(createFirstRunnable(firstAcc, secondAcc, thirdAcc));
        Thread thread2 = new Thread(createSecondRunnable(firstAcc, secondAcc, thirdAcc));
        Thread thread3 = new Thread(createThirdRunnable(firstAcc, secondAcc, thirdAcc));
        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();


        //result
        Account updatedFirstAcc = accountDao.get(firstAcc);
        Account updatedSecondAcc = accountDao.get(secondAcc);
        Account updatedThirdAcc = accountDao.get(thirdAcc);

        //verify
        assertThat(updatedFirstAcc.getAccountBalance(),is(BigDecimal.valueOf(1600)));
        assertThat(updatedSecondAcc.getAccountBalance(),is(BigDecimal.valueOf(1790)));
        assertThat(updatedThirdAcc.getAccountBalance(),is(BigDecimal.valueOf(2610)));

    }



    private HttpResponse transferOverHttp(String firstAcc, String secondAcc, BigDecimal amount) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + randomPort + "/api/transfer?from=" + firstAcc + "&to=" + secondAcc + "&amount=" + amount))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
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

    private Runnable createFirstRunnable(String firstAcc, String secondAcc, String thirdAcc) {
        return () -> {
            try {
                transferOverHttp(firstAcc, secondAcc, BigDecimal.valueOf(300));
                Thread.sleep(500);
                transferOverHttp(firstAcc, thirdAcc, BigDecimal.valueOf(500));
                Thread.sleep(500);
                transferOverHttp(firstAcc, secondAcc, BigDecimal.valueOf(200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        };
    }


    private Runnable createSecondRunnable(String firstAcc, String secondAcc, String thirdAcc) {
        return () -> {
            try {
                transferOverHttp(secondAcc, thirdAcc, BigDecimal.valueOf(450));
                Thread.sleep(500);
                transferOverHttp(secondAcc, firstAcc, BigDecimal.valueOf(100));
                Thread.sleep(500);
                transferOverHttp(secondAcc, firstAcc, BigDecimal.valueOf(200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        };
    }


    private Runnable createThirdRunnable(String firstAcc, String secondAcc, String thirdAcc) {

            return () -> {
                try {
                    transferOverHttp(thirdAcc, secondAcc, BigDecimal.valueOf(40));
                    Thread.sleep(500);
                    transferOverHttp(thirdAcc, firstAcc, BigDecimal.valueOf(100));
                    Thread.sleep(500);
                    transferOverHttp(thirdAcc, firstAcc, BigDecimal.valueOf(200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            };
        }

}