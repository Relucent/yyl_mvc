package yyl.mvc.util.function;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FunctionUtil {

	public static <P, R> Supplier<R> toSupplier(BiSupplier<P, R> supplier, P parameter) {
		return () -> supplier.get(parameter);
	}

	public static <T, U, R> Function<T, R> toFunction(BiFunction<T, U, R> function, U second) {
		return first -> function.apply(first, second);
	}

	public static <T, U> Consumer<T> toConsumer(BiConsumer<T, U> consumer, U second) {
		return (first) -> consumer.accept(first, second);
	}

	@FunctionalInterface
	public static interface BiSupplier<P, T> {
		T get(P p);
	}
}
