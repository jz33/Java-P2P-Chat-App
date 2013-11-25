package provided.util;

/**
 * Generalized lambda that takes a vararg of input parameters and has no return value.
 * @author swong
 *
 * @param <P>  The Type of the input parameters
 */
public interface IVoidLambda<P> {
	/**
	 * Run (apply) the lambda
	 * @param params vararg of input parameters
	 */
	public void apply(@SuppressWarnings("unchecked") P...params);
}
