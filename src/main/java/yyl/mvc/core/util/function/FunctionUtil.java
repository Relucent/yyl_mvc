package yyl.mvc.core.util.function;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionUtil {

	public static <T, U, R> Function<T, R> toFunction(BiFunction<T, U, R> function, U second) {
		return first -> function.apply(first, second);
	}

	public static <T, U> Consumer<T> toConsumer(BiConsumer<T, U> consumer, U second) {
		return new Consumer<T>() {
			@Override
			public void accept(T first) {
				consumer.accept(first, second);
			}
		};
	}
}
