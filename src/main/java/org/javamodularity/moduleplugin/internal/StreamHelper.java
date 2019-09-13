package org.javamodularity.moduleplugin.internal;

import java.util.stream.Stream;
import static java.util.stream.Stream.of;

public final class StreamHelper {

    @SafeVarargs
    public static <T> Stream<T> concat(Stream<T>... streams) {
        return of(streams).reduce(Stream::concat).orElseThrow(IllegalArgumentException::new);
    }
}
