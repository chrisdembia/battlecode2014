/**
 * 
 */
package team139.utils;

import team139.model.Model;

/**
 * TODO might be creating circular references, since most CacheVariables will
 * be member variables in Model.
 * Guidance comes from these webpages:
 * http://www.codeproject.com/Articles/484598/Keyword-mutable-and-data-caching-in-Cplusplus
 * https://en.wikipedia.org/wiki/Generics_in_Java
 */
public abstract class CacheVariable<T> {
	protected final Model m;
	private T value;

	/**
	 * 
	 * @param rc The cache variable's calculations can use information
	 * 			 available in the Model.
	 */
	public CacheVariable(Model m) {
		this.m = m;
		this.value = null;
	}
	
	/**
	 * For whatever reason, the caller believes the value stored in this
	 * variable is no longer valid. The next time it's needed,
	 * it'll be recomputed.
	 */
	public void invalidate() {
		value = null;
	}
	
	/**
	 * If we have a valid value, we'll return that. Otherwise, we'll recompute
	 * the variable.
	 * @return The value of this cache variable.
	 */
	public T get() {
		if (value == null) {
			value = calculate();
		}
		return value;
	}
	
	/**
	 * This is the meat of the cache variable. How do we compute the variable?
	 */
	protected abstract T calculate();

}
