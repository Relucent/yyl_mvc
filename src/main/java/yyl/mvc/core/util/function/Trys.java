package yyl.mvc.core.util.function;

import java.util.function.Consumer;
import java.util.function.Function;

public class Trys {

	public static <T> Consumer<T> of(UncheckedConsumer<? super T> action) {
		return t -> {
			try {
				action.apply(t);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	public static <T, R> Function<T, R> of(UncheckedFunction<T, R> function) {
		return t -> {
			try {
				return function.apply(t);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}

	@FunctionalInterface
	public static interface UncheckedConsumer<T> {
		void apply(T t) throws Exception;
	}

	@FunctionalInterface
	public static interface UncheckedFunction<T, R> {
		R apply(T t) throws Exception;
	}
}
