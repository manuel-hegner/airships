package io.github.manuelhegner.airships.util;

import java.util.concurrent.atomic.AtomicLong;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor @EqualsAndHashCode @ToString
public class LUID {

	private static AtomicLong nextLUID = new AtomicLong(0L);
	
	public static LUID next() {
		return new LUID(nextLUID.getAndIncrement());
	}
	
	private final long id;
}
