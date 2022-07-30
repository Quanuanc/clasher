package org.example.clasher.conf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UpstreamTest {

    @Autowired
    private Upstream upstream;

    @Test
    public void assertNotNull() {
        upstream.getProxyProviders().forEach(Assertions::assertNotNull);
        upstream.getRuleProviders().forEach(Assertions::assertNotNull);
        upstream.getMmdbProviders().forEach(Assertions::assertNotNull);
    }
}