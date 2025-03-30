package com.expresso.ast;

import com.expresso.context.Context;
import com.expresso.exception.EvaluationException;
import com.expresso.exception.PropertyNotFoundException;
import java.util.List;
import java.util.function.Function;

/** AST node for function calls */
public class FunctionExpression implements Expression {
  private final String name;
  private final List<Expression> arguments;

  public FunctionExpression(String name, List<Expression> arguments) {
    this.name = name;
    this.arguments = arguments;
  }

  @Override
  public Object evaluate(Context context) {
    // Special handling for isNull function to avoid PropertyNotFoundException
    if ("isNull".equals(name)) {
      // We want isNull to return true for nonexistent variables
      if (arguments.size() == 1 && arguments.get(0) instanceof VariableExpression) {
        VariableExpression varExpr = (VariableExpression) arguments.get(0);
        try {
          Object value = arguments.get(0).evaluate(context);
          return value == null;
        } catch (PropertyNotFoundException e) {
          // If the variable doesn't exist, it's effectively null
          return true;
        }
      }
    }
    
    Function<Object[], Object> function = context.getFunction(name);
    if (function == null) {
      throw new EvaluationException("Function not found: " + name);
    }

    Object[] evaluatedArgs = new Object[arguments.size()];
    for (int i = 0; i < arguments.size(); i++) {
      evaluatedArgs[i] = arguments.get(i).evaluate(context);
    }

    try {
      return function.apply(evaluatedArgs);
    } catch (Exception e) {
      throw new EvaluationException("Error evaluating function: " + name, e);
    }
  }

  public String getName() {
    return name;
  }

  public List<Expression> getArguments() {
    return arguments;
  }
} 