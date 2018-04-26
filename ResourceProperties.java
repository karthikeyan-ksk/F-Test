package com.allergan.coral.dfc.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This is used for reading the properties files.
 * 
 * @author sakthivel_karthikeya
 */
public class ResourceProperties {

	/**
	 * The resource bundle.
	 */
	private ResourceBundle rb;

	/**
	 * The constructor method.
	 * 
	 * @param baseName
	 *            The base name.
	 */
	public ResourceProperties(String baseName) {
		rb = ResourceBundle.getBundle(baseName);
	}

	/**
	 * Returns the value for the particular key.
	 * 
	 * @param key
	 *            The key value.
	 * @return a String value.
	 */
	public String getKeyValue(String key) {
		String value = "";
		try {
			if (rb != null) {
				value = rb.getString(key);
			}
		} catch (MissingResourceException mre) {
			return mre.getMessage();
		}
		return value;
	}
}
