package com.github.rholder.jvm.loopunswitching;

import org.junit.Test;

import static com.github.rholder.jvm.loopunswitching.JvmBug.bug;

public class JvmBugTest {

    @Test
    public void segfault() throws InterruptedException {
        bug();
    }
}
