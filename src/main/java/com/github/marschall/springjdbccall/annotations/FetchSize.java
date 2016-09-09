package com.github.marschall.springjdbccall.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows manual control over the fetch size.
 *
 * <p>This is only really useful for methods .</p>
 *
 * <p>When applied to a class applies to all methods in the class
 * unless also applied to a method.</p>
 */
@Documented
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface FetchSize {

  /**
   * Sets the fetch size to be used.
   *
   * <p>The usual disclaimers for setting the fetch size apply:</p>
   * <ul>
   *  <li>it's just a hint</li>
   *  <li>the driver may ignore it</li>
   *  <li>the driver may round it to a value more convenient</li>
   * </ul>
   *
   * @return the number of rows to fetch in one round trip
   * @see java.sql.Statement#setFetchSize(int)
   */
  int value();

}
