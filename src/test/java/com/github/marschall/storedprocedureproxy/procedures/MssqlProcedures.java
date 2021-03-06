package com.github.marschall.storedprocedureproxy.procedures;

import java.util.List;

import com.github.marschall.storedprocedureproxy.annotations.OutParameter;
import com.github.marschall.storedprocedureproxy.annotations.ReturnValue;

public interface MssqlProcedures {

  @OutParameter(name = "res")
  int plus1inout(int arg);

  @ReturnValue
  int plus1inret(int arg);

  List<String> fakeCursor();

}
