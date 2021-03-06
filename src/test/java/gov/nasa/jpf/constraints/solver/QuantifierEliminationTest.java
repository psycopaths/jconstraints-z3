/*
 * Copyright (C) 2015, United States Government, as represented by the 
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * The PSYCO: A Predicate-based Symbolic Compositional Reasoning environment 
 * platform is licensed under the Apache License, Version 2.0 (the "License"); you 
 * may not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software distributed 
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 */
package gov.nasa.jpf.constraints.solver;

import gov.nasa.jpf.constraints.api.Expression;
import gov.nasa.jpf.constraints.api.Variable;
import gov.nasa.jpf.constraints.expressions.Constant;
import gov.nasa.jpf.constraints.expressions.NumericBooleanExpression;
import gov.nasa.jpf.constraints.expressions.NumericComparator;
import gov.nasa.jpf.constraints.expressions.NumericCompound;
import gov.nasa.jpf.constraints.expressions.NumericOperator;
import gov.nasa.jpf.constraints.expressions.Quantifier;
import gov.nasa.jpf.constraints.expressions.QuantifierExpression;
import gov.nasa.jpf.constraints.solvers.ConstraintSolverFactory;
import gov.nasa.jpf.constraints.solvers.nativez3.NativeZ3Solver;
import gov.nasa.jpf.constraints.types.BuiltinTypes;
import gov.nasa.jpf.constraints.util.ExpressionUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import static org.junit.Assert.*;
import org.testng.annotations.Test;

public class QuantifierEliminationTest {
  
  public QuantifierEliminationTest() {
  }
  
  @Before
  public void setUp() {
  }

  @Test
  public void eliminationTest() throws IOException {
    Properties conf = new Properties();     
    conf.setProperty("symbolic.dp", "z3");
    ConstraintSolverFactory factory = new ConstraintSolverFactory(conf);
    NativeZ3Solver solver = (NativeZ3Solver)factory.createSolver();        

    Variable x = new Variable(BuiltinTypes.INTEGER, "x");
    Variable a = new Variable(BuiltinTypes.INTEGER, "a");
    Variable b = new Variable(BuiltinTypes.INTEGER, "b");

    Constant zero = new Constant(BuiltinTypes.INTEGER, 0);

    //Expression expr = new NumericBooleanExpression(x, NumericComparator.EQ, x);

    Expression expr = new NumericBooleanExpression(
            x, 
            NumericComparator.EQ,
            new NumericCompound<>(a, NumericOperator.PLUS, b));                

    expr = ExpressionUtil.and(expr,
            new NumericBooleanExpression(a, NumericComparator.GT, zero),
            new NumericBooleanExpression(b, NumericComparator.GT, zero));

    List bound = new ArrayList<>();
    bound.add(a);
    bound.add(b);

    QuantifierExpression qe = new QuantifierExpression(Quantifier.EXISTS, bound, expr);
    System.out.println("gov.nasa.jpf.constraints.api.QuantifierEliminationTest.eliminationTest()");
    StringBuilder aa = new StringBuilder();
    qe.print(aa);
    System.out.println(aa.toString());
    assertNotNull(solver.eliminateQuantifiers(qe));
  }
}
