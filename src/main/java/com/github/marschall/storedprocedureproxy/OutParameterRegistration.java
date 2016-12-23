package com.github.marschall.storedprocedureproxy;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * Strategy on how to register out parameters.
 */
interface OutParameterRegistration {

  void bindOutParamter(CallableStatement statement) throws SQLException;

  <T> T getOutParamter(CallableStatement statement, Class<T> type) throws SQLException;

}


/**
 * Register out parameters by index and type.
 */
final class ByIndexOutParameterRegistration implements OutParameterRegistration {

  // an interface method can not have more than 254 parameters
  private final int outParameterIndex;
  private final int outParameterType;

  ByIndexOutParameterRegistration(int outParameterIndex, int outParameterType) {
    this.outParameterIndex = outParameterIndex;
    this.outParameterType = outParameterType;
  }

  @Override
  public void bindOutParamter(CallableStatement statement) throws SQLException {
    statement.registerOutParameter(this.outParameterIndex, this.outParameterType);
  }

  @Override
  public <T> T getOutParamter(CallableStatement statement, Class<T> type) throws SQLException {
    try {
      return statement.getObject(this.outParameterIndex, type);
    } catch (SQLFeatureNotSupportedException e) {
      // Postgres hack
      return type.cast(statement.getObject(this.outParameterIndex));
    }
  }

}

/**
 * Register out parameters by name and type.
 */
final class ByNameOutParameterRegistration implements OutParameterRegistration {

  private final String outParameterName;
  private final int outParameterType;

  ByNameOutParameterRegistration(String outParameterName, int outParameterType) {
    this.outParameterName = outParameterName;
    this.outParameterType = outParameterType;
  }

  @Override
  public void bindOutParamter(CallableStatement statement) throws SQLException {
    statement.registerOutParameter(this.outParameterName, this.outParameterType);
  }

  @Override
  public <T> T getOutParamter(CallableStatement statement, Class<T> type) throws SQLException {
    return statement.getObject(this.outParameterName, type);
  }

}

/**
 * Register out parameters by index, type and type name.
 *
 * <blockquote>
 * should be used for a user-defined or <code>REF</code> output parameter.  Examples
 * of user-defined types include: <code>STRUCT</code>, <code>DISTINCT</code>,
 * <code>JAVA_OBJECT</code>, and named array types.
 * </blockquote>
 *
 * @see CallableStatement#registerOutParameter(int, int, String)
 */
final class ByIndexAndTypeNameOutParameterRegistration implements OutParameterRegistration {

  // an interface method can not have more than 254 parameters
  private final int outParameterIndex;
  private final int outParameterType;
  private final String typeName;

  ByIndexAndTypeNameOutParameterRegistration(int outParameterIndex, int outParameterType, String typeName) {
    this.outParameterIndex = outParameterIndex;
    this.outParameterType = outParameterType;
    this.typeName = typeName;
  }

  @Override
  public void bindOutParamter(CallableStatement statement) throws SQLException {
    statement.registerOutParameter(this.outParameterIndex, this.outParameterType, this.typeName);

  }

  @Override
  public <T> T getOutParamter(CallableStatement statement, Class<T> type) throws SQLException {
    return statement.getObject(this.outParameterIndex, type);
  }

}



/**
 * Register out parameters by name, type and type name.
 *
 * <blockquote>
 * should be used for a user-named or REF output parameter.  Examples
 * of user-named types include: STRUCT, DISTINCT, JAVA_OBJECT, and
 * named array types.
 * </blockquote>
 *
 * @see CallableStatement#registerOutParameter(String, int, String)
 */
final class ByNameAndTypeNameOutParameterRegistration implements OutParameterRegistration {

  private final String outParameterName;
  private final int outParameterType;
  private final String typeName;

  ByNameAndTypeNameOutParameterRegistration(String outParameterName, int outParameterType, String typeName) {
    this.outParameterName = outParameterName;
    this.outParameterType = outParameterType;
    this.typeName = typeName;
  }

  @Override
  public void bindOutParamter(CallableStatement statement) throws SQLException {
    statement.registerOutParameter(this.outParameterName, this.outParameterType, this.typeName);
  }

  @Override
  public <T> T getOutParamter(CallableStatement statement, Class<T> type) throws SQLException {
    return statement.getObject(this.outParameterName, type);
  }

}

/**
 * No out parameters are registered. Either because the procedure
 * doesn't return any results or returns the result by means of a
 * {@link ResultSet}.
 */
final class  NoOutParameterRegistration implements OutParameterRegistration {

  static final OutParameterRegistration INSTANCE = new NoOutParameterRegistration();

  private NoOutParameterRegistration() {
    super();
  }

  @Override
  public void bindOutParamter(CallableStatement statement) {
    // nothing
  }

  @Override
  public <T> T getOutParamter(CallableStatement statement, Class<T> type) {
    if (type != void.class) {
      throw new IllegalArgumentException("no out parameter registered");
    }
    return null;
  }

}
