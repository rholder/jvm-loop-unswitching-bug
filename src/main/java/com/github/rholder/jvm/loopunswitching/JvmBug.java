package com.github.rholder.jvm.loopunswitching;

import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.params.SyncBasicHttpParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang.RandomStringUtils.random;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

public class JvmBug {

    /**
     * This appears to exhibit the same behavior as the bug reported here:
     * {@code https://code.google.com/p/crawler4j/issues/detail?id=136}. Adding
     * the {@code -XX:-LoopUnswitching} JVM argument fixes this issue, as
     * referenced in a similar bug reported in
     * {@code https://issues.apache.org/jira/browse/HTTPCLIENT-1173}.
     *
     * @throws InterruptedException
     */
    public static void bug() throws InterruptedException {
        final BestMatchSpecFactory factory = new BestMatchSpecFactory();
        final CookieSpec cookieSpec = factory.newInstance(new SyncBasicHttpParams());

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 50000; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    List<Cookie> someCookies = new ArrayList<Cookie>();
                    for (int j = 0; j < 5; j++) {
                        BasicClientCookie c = new BasicClientCookie(random(20), randomAlphanumeric(300));
                        someCookies.add(c);
                    }
                    cookieSpec.formatCookies(someCookies);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
    }

    public static void main(String... args) throws InterruptedException {
        bug();
    }
}
