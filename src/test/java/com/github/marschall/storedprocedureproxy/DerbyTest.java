package com.github.marschall.storedprocedureproxy;

import static com.github.marschall.storedprocedureproxy.ProcedureCallerFactory.ParameterRegistration.INDEX_AND_TYPE;
import static com.github.marschall.storedprocedureproxy.ProcedureCallerFactory.ParameterRegistration.INDEX_ONLY;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.test.context.ContextConfiguration;

import com.github.marschall.storedprocedureproxy.ProcedureCallerFactory.ParameterRegistration;
import com.github.marschall.storedprocedureproxy.configuration.DerbyConfiguration;
import com.github.marschall.storedprocedureproxy.procedures.DerbyProcedures;

@RunWith(Parameterized.class)
@ContextConfiguration(classes = DerbyConfiguration.class)
public class DerbyTest extends AbstractDataSourceTest {

  private DerbyProcedures functions;

  private ParameterRegistration parameterRegistration;

  public DerbyTest(ParameterRegistration parameterRegistration) {
    this.parameterRegistration = parameterRegistration;
  }

  @Before
  public void setUp() {
    this.functions = ProcedureCallerFactory.of(DerbyProcedures.class, this.getDataSource())
            .withParameterRegistration(this.parameterRegistration)
            .withNamespace()
            .build();
  }

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
            new Object[] {INDEX_ONLY},
            new Object[] {INDEX_AND_TYPE}
    );
  }

  @Test
  public void outParameter() {
    assertThat(functions.calculateRevenueByMonth(9, 2016), comparesEqualTo(new BigDecimal(201609)));
  }

  @Test
  public void inOutParameter() {
    assertThat(functions.raisePrice(new BigDecimal("10.1")), comparesEqualTo(new BigDecimal("20.2")));
  }

  @Test
  public void returnValue() {
    assertEquals(0.01d, 6.0d, functions.salesTax(100.0d));
  }

}
