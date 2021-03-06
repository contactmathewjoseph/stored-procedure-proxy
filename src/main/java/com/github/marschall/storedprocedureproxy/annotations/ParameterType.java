package com.github.marschall.storedprocedureproxy.annotations;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.marschall.storedprocedureproxy.spi.TypeMapper;

/**
 * Defines the SQL type of an in parameter.
 *
 * @see <a href="https://github.com/marschall/stored-procedure-proxy/wiki/Binding-Parameters">Binding Parameters</a>
 */
@Documented
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface ParameterType {

  /**
   * Defines the SQL type of an in parameter. If nothing is specified the default
   * from {@link TypeMapper} is used.
   *
   * @return the parameter SQL type, can be a vendor type
   * @see java.sql.Types
   */
  int value();

}
