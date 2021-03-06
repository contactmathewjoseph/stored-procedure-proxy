package com.github.marschall.storedprocedureproxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.marschall.storedprocedureproxy.ProcedureCallerFactory.ParameterRegistration;
import com.github.marschall.storedprocedureproxy.annotations.OutParameter;

public class ByNameAndTypeNameOutParameterRegistrationTest {

  private CallableStatement callableStatement;

  private ReturnTypeNameUser procedures;

  @BeforeEach
  public void setUp() throws SQLException {
    DataSource dataSource = mock(DataSource.class);
    this.callableStatement = mock(CallableStatement.class);
    Connection connection = mock(Connection.class);
    DatabaseMetaData metaData = mock(DatabaseMetaData.class);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.getMetaData()).thenReturn(metaData);
    when(metaData.getDatabaseProductName()).thenReturn("junit");
    when(connection.prepareCall(anyString())).thenReturn(this.callableStatement);

    this.procedures = ProcedureCallerFactory.of(ReturnTypeNameUser.class, dataSource)
            .withParameterRegistration(ParameterRegistration.NAME_ONLY)
            .build();
  }

  @Test
  public void returnTypeNameOutParameter() throws SQLException {
    this.procedures.returnTypeNameOutParameter();
    verify(this.callableStatement).registerOutParameter("out", Types.VARCHAR, "duck");
  }

  @Test
  public void noReturnTypeNameOutParameter() throws SQLException {
    this.procedures.noReturnTypeNameOutParameter();
    verify(this.callableStatement).registerOutParameter("out", Types.VARCHAR);
  }

  @Test
  public void testToString() {
    OutParameterRegistration registration = new ByNameAndTypeNameOutParameterRegistration("dog", Types.INTEGER, "duck");
    assertEquals("ByNameAndTypeNameOutParameterRegistration[name=dog, type=4, typeName=duck]", registration.toString());
  }

  interface ReturnTypeNameUser {

    @OutParameter(name = "out", typeName = "duck")
    String returnTypeNameOutParameter();

    @OutParameter(name = "out")
    String noReturnTypeNameOutParameter();

  }

}
