package com.huawei.olympic_ranking.testsupport;

import java.util.concurrent.atomic.AtomicLong;


public abstract class TestDataBuilder<T> {

    private static final AtomicLong SEQUENCE = new AtomicLong();

    public abstract T build();

    public static long nextId() {
        return SEQUENCE.incrementAndGet();
    }

    public static String uniqueName(final String prefix) {
        return prefix + " " + nextId();
    }

    public static String uniqueCode() {
        long n = nextId() % (26 * 26 * 26);
        final char[] code = new char[3];
        for (int i = 2; i >= 0; i--) {
            code[i] = (char) ('A' + n % 26);
            n /= 26;
        }
        return new String(code);
    }

}
