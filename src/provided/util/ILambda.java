package provided.util;

/**
 * Generalized lambda that takes inputs and has a return value
 * @author swong
 *
 * @param <R>  Type of the return value
 * @param <P>  Type of the input parameters
 */
public interface ILambda<R, P> {
	/**
	 * Run (apply) the lambda
	 * @param params vararg of input parameters
	 * @return The result of running the lambda with the given input parameters.
	 */
	public R apply(@SuppressWarnings("unchecked") P...params);
}
