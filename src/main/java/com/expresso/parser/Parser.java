package com.expresso.parser;

import com.expresso.ast.*;
import com.expresso.exception.SyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for converting expression strings into AST nodes
 */
public class Parser {
    private String input;
    private int position;

    public Expression parse(String expression) {
        this.input = expression.trim();
        this.position = 0;
        return parseExpression();
    }

    private Expression parseExpression() {
        return parseConditional();
    }

    private Expression parseConditional() {
        Expression condition = parseLogicalOr();
        skipWhitespace();
        
        if (position < input.length() && input.charAt(position) == '?') {
            position++; // Skip ?
            skipWhitespace();
            
            Expression trueExpr = parseLogicalOr(); // Parse the 'then' part
            skipWhitespace();
            
            if (position >= input.length() || input.charAt(position) != ':') {
                throw new SyntaxException("Expected ':' in conditional expression", position);
            }
            
            position++; // Skip :
            skipWhitespace();
            
            Expression falseExpr = parseLogicalOr(); // Parse the 'else' part
            return new ConditionalExpression(condition, trueExpr, falseExpr);
        }
        
        return condition;
    }

    private Expression parseLogicalOr() {
        Expression left = parseLogicalAnd();
        skipWhitespace();

        while (position < input.length() - 1) {
            // Check for OR operator ||
            if (input.charAt(position) == '|' && input.charAt(position + 1) == '|') {
                position += 2; // Skip ||
                skipWhitespace();
                Expression right = parseLogicalAnd();
                left = new BinaryExpression(left, right, BinaryExpression.Operator.OR);
                skipWhitespace();
            } else {
                break;
            }
        }

        return left;
    }

    private Expression parseLogicalAnd() {
        Expression left = parseEquality();
        skipWhitespace();

        while (position < input.length() - 1) {
            // Check for AND operator &&
            if (input.charAt(position) == '&' && input.charAt(position + 1) == '&') {
                position += 2; // Skip &&
                skipWhitespace();
                Expression right = parseEquality();
                left = new BinaryExpression(left, right, BinaryExpression.Operator.AND);
                skipWhitespace();
            } else {
                break;
            }
        }

        return left;
    }

    private Expression parseEquality() {
        Expression left = parseRelational();
        skipWhitespace();

        while (position < input.length() - 1) {
            // Check for equality operators == and !=
            if (input.charAt(position) == '=' && input.charAt(position + 1) == '=') {
                position += 2; // Skip ==
                skipWhitespace();
                Expression right = parseRelational();
                left = new BinaryExpression(left, right, BinaryExpression.Operator.EQUALS);
                skipWhitespace();
            } else if (input.charAt(position) == '!' && input.charAt(position + 1) == '=') {
                position += 2; // Skip !=
                skipWhitespace();
                Expression right = parseRelational();
                left = new BinaryExpression(left, right, BinaryExpression.Operator.NOT_EQUALS);
                skipWhitespace();
            } else {
                break;
            }
        }

        return left;
    }

    private Expression parseRelational() {
        Expression left = parseAdditive();
        skipWhitespace();

        while (position < input.length()) {
            // Check for relational operators: >, <, >=, <=
            if (position < input.length() - 1 && input.charAt(position) == '>' && input.charAt(position + 1) == '=') {
                position += 2; // Skip >=
                skipWhitespace();
                Expression right = parseAdditive();
                left = new BinaryExpression(left, right, BinaryExpression.Operator.GREATER_THAN_OR_EQUAL);
                skipWhitespace();
            } else if (position < input.length() - 1 && input.charAt(position) == '<' && input.charAt(position + 1) == '=') {
                position += 2; // Skip <=
                skipWhitespace();
                Expression right = parseAdditive();
                left = new BinaryExpression(left, right, BinaryExpression.Operator.LESS_THAN_OR_EQUAL);
                skipWhitespace();
            } else if (input.charAt(position) == '>') {
                position++; // Skip >
                skipWhitespace();
                Expression right = parseAdditive();
                left = new BinaryExpression(left, right, BinaryExpression.Operator.GREATER_THAN);
                skipWhitespace();
            } else if (input.charAt(position) == '<') {
                position++; // Skip <
                skipWhitespace();
                Expression right = parseAdditive();
                left = new BinaryExpression(left, right, BinaryExpression.Operator.LESS_THAN);
                skipWhitespace();
            } else {
                break;
            }
        }

        return left;
    }

    private Expression parseAdditive() {
        Expression left = parseMultiplicative();
        skipWhitespace();

        while (position < input.length()) {
            char operator = input.charAt(position);
            if (operator != '+' && operator != '-') {
                break;
            }
            position++; // Skip the operator
            skipWhitespace();

            Expression right = parseMultiplicative();
            left = new BinaryExpression(left, right, operator == '+' ? BinaryExpression.Operator.ADD : BinaryExpression.Operator.SUBTRACT);
            skipWhitespace();
        }

        return left;
    }

    private Expression parseMultiplicative() {
        Expression left = parseUnary();
        skipWhitespace();

        while (position < input.length()) {
            char operator = input.charAt(position);
            if (operator != '*' && operator != '/' && operator != '%') {
                break;
            }
            position++; // Skip the operator
            skipWhitespace();

            Expression right = parseUnary();
            if (operator == '*') {
                left = new BinaryExpression(left, right, BinaryExpression.Operator.MULTIPLY);
            } else if (operator == '/') {
                left = new BinaryExpression(left, right, BinaryExpression.Operator.DIVIDE);
            } else {
                left = new BinaryExpression(left, right, BinaryExpression.Operator.MODULO);
            }
            skipWhitespace();
        }

        return left;
    }

    private Expression parseUnary() {
        skipWhitespace();
        if (position < input.length()) {
            char c = input.charAt(position);
            if (c == '-') {
                position++; // Skip -
                skipWhitespace();
                return new UnaryExpression(parseUnary(), UnaryExpression.Operator.NEGATE);
            }
            if (c == '!') {
                position++; // Skip !
                skipWhitespace();
                return new UnaryExpression(parseUnary(), UnaryExpression.Operator.NOT);
            }
        }
        return parsePrimary();
    }

    private Expression parsePrimary() {
        skipWhitespace();

        if (position >= input.length()) {
            throw new SyntaxException("Unexpected end of expression", position);
        }

        char c = input.charAt(position);

        // Handle literals
        if (c == '"' || c == '\'') {
            return parseStringLiteral();
        }

        if (Character.isDigit(c) || c == '-') {
            return parseNumberLiteral();
        }

        // Handle variables
        if (c == '$') {
            return parseVariable();
        }

        // Handle function calls and various literals (null, true, false)
        if (Character.isLetter(c)) {
            int start = position;
            while (position < input.length()) {
                c = input.charAt(position);
                if (Character.isLetterOrDigit(c) || c == '_') {
                    position++;
                } else {
                    break;
                }
            }

            String value = input.substring(start, position);
            skipWhitespace();

            // Check if it's a function call
            if (position < input.length() && input.charAt(position) == '(') {
                position = start; // Reset position to start of function name
                return parseFunctionCall();
            }

            // Check if it's a boolean literal
            if (value.equals("true")) {
                return new LiteralExpression(true);
            } else if (value.equals("false")) {
                return new LiteralExpression(false);
            } else if (value.equals("null")) {
                return new LiteralExpression(null);
            }

            throw new SyntaxException("Invalid identifier: " + value, start);
        }

        // Handle parenthesized expressions
        if (c == '(') {
            position++; // Skip (
            skipWhitespace();
            Expression expr = parseExpression();
            skipWhitespace();
            
            if (position >= input.length() || input.charAt(position) != ')') {
                throw new SyntaxException("Expected ')' after expression", position);
            }
            position++; // Skip )
            
            return expr;
        }

        throw new SyntaxException("Unexpected character: " + c, position);
    }

    private Expression parseStringLiteral() {
        char quote = input.charAt(position++);
        StringBuilder sb = new StringBuilder();

        while (position < input.length()) {
            char c = input.charAt(position);
            if (c == quote) {
                position++;
                return new LiteralExpression(sb.toString());
            }
            if (c == '\\') {
                position++;
                if (position < input.length()) {
                    c = input.charAt(position++);
                    switch (c) {
                        case 'n':
                            sb.append('\n');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        default:
                            sb.append(c);
                    }
                }
            } else {
                sb.append(c);
                position++;
            }
        }

        throw new SyntaxException("Unterminated string literal", position);
    }

    private Expression parseNumberLiteral() {
        int start = position;
        boolean isDecimal = false;

        if (input.charAt(position) == '-') {
            position++;
        }

        while (position < input.length()) {
            char c = input.charAt(position);
            if (c == '.') {
                if (isDecimal) {
                    throw new SyntaxException("Invalid number format", position);
                }
                isDecimal = true;
                position++;
            } else if (Character.isDigit(c)) {
                position++;
            } else {
                break;
            }
        }

        String numberStr = input.substring(start, position);
        try {
            if (isDecimal) {
                return new LiteralExpression(Double.parseDouble(numberStr));
            } else {
                return new LiteralExpression(Long.parseLong(numberStr));
            }
        } catch (NumberFormatException e) {
            throw new SyntaxException("Invalid number format: " + numberStr, position);
        }
    }

    private Expression parseVariable() {
        position++; // Skip $
        StringBuilder nameBuilder = new StringBuilder();
        boolean isNullSafe = false;

        // Parse the variable name
        while (position < input.length()) {
            char c = input.charAt(position);
            if (Character.isLetterOrDigit(c) || c == '_') {
                nameBuilder.append(c);
                position++;
            } else {
                break;
            }
        }

        String name = nameBuilder.toString();
        StringBuilder propertyPathBuilder = new StringBuilder();

        // Check for property path or direct array access
        skipWhitespace();
        if (position < input.length()) {
            char c = input.charAt(position);

            // Handle null-safe operator directly after variable name
            if (c == '?' && position + 1 < input.length() && input.charAt(position + 1) == '.') {
                isNullSafe = true;
                position += 2; // Skip ?.

                // Get the next property name
                skipWhitespace();
                int start = position;
                while (position < input.length()) {
                    c = input.charAt(position);
                    if (Character.isLetterOrDigit(c) || c == '_') {
                        position++;
                    } else {
                        break;
                    }
                }
                if (position > start) {
                    propertyPathBuilder.append(input, start, position);
                }

                // Continue parsing the rest of the property path
                while (position < input.length()) {
                    skipWhitespace();
                    c = input.charAt(position);

                    // Null-safe operator in the middle of property path
                    if (c == '?' && position + 1 < input.length() && input.charAt(position + 1) == '.') {
                        isNullSafe = true; // This is redundant but makes the code clearer
                        position += 2; // Skip ?.
                        propertyPathBuilder.append('.');

                        // Parse the next property name
                        start = position;
                        while (position < input.length()) {
                            c = input.charAt(position);
                            if (Character.isLetterOrDigit(c) || c == '_') {
                                position++;
                            } else {
                                break;
                            }
                        }
                        if (position > start) {
                            propertyPathBuilder.append(input, start, position);
                        }
                    }
                    // Regular dot access
                    else if (c == '.') {
                        position++; // Skip dot
                        propertyPathBuilder.append('.');

                        // Parse the next property name
                        start = position;
                        while (position < input.length()) {
                            c = input.charAt(position);
                            if (Character.isLetterOrDigit(c) || c == '_') {
                                position++;
                            } else {
                                break;
                            }
                        }
                        if (position > start) {
                            propertyPathBuilder.append(input, start, position);
                        }
                    }
                    // Null-safe array access ?[
                    else if (c == '?' && position + 1 < input.length() && input.charAt(position + 1) == '[') {
                        isNullSafe = true; // Ensure null-safe flag is set
                        position++; // Skip ?
                        propertyPathBuilder.append('?'); // Add the ? to the property path

                        // Now handle the array access
                        propertyPathBuilder.append('[');
                        position++; // Skip [
                        skipWhitespace();

                        // Parse the array index
                        if (!Character.isDigit(input.charAt(position)) && input.charAt(position) != '-') {
                            throw new SyntaxException("Array index must be a number", position);
                        }
                        start = position;
                        while (position < input.length() && Character.isDigit(input.charAt(position))) {
                            position++;
                        }
                        propertyPathBuilder.append(input, start, position);
                        skipWhitespace();

                        if (position >= input.length() || input.charAt(position) != ']') {
                            throw new SyntaxException("Expected ']' after array index", position);
                        }
                        propertyPathBuilder.append(']');
                        position++; // Skip ]
                    }
                    // Regular array access
                    else if (c == '[') {
                        propertyPathBuilder.append('[');
                        position++; // Skip [
                        skipWhitespace();

                        // Parse the array index
                        if (!Character.isDigit(input.charAt(position)) && input.charAt(position) != '-') {
                            throw new SyntaxException("Array index must be a number", position);
                        }
                        start = position;
                        while (position < input.length() && Character.isDigit(input.charAt(position))) {
                            position++;
                        }
                        propertyPathBuilder.append(input, start, position);
                        skipWhitespace();

                        if (position >= input.length() || input.charAt(position) != ']') {
                            throw new SyntaxException("Expected ']' after array index", position);
                        }
                        propertyPathBuilder.append(']');
                        position++; // Skip ]
                    }
                    // Null coalescing operator
                    else if (c == '?' && position + 1 < input.length() && input.charAt(position + 1) == '?') {
                        position += 2; // Skip ??
                        skipWhitespace();
                        Expression defaultValue = parseExpression();
                        return new NullCoalescingExpression(
                                new VariableExpression(name, propertyPathBuilder.toString(), isNullSafe),
                                defaultValue
                        );
                    } else {
                        break;
                    }
                }
            }
            // Null-safe array access directly after variable
            else if (c == '?' && position + 1 < input.length() && input.charAt(position + 1) == '[') {
                isNullSafe = true;
                position++; // Skip ?
                propertyPathBuilder.append('?');

                // Now handle the array access
                propertyPathBuilder.append('[');
                position++; // Skip [
                skipWhitespace();

                // Parse the array index
                if (!Character.isDigit(input.charAt(position)) && input.charAt(position) != '-') {
                    throw new SyntaxException("Array index must be a number", position);
                }
                int start = position;
                while (position < input.length() && Character.isDigit(input.charAt(position))) {
                    position++;
                }
                propertyPathBuilder.append(input, start, position);
                skipWhitespace();

                if (position >= input.length() || input.charAt(position) != ']') {
                    throw new SyntaxException("Expected ']' after array index", position);
                }
                propertyPathBuilder.append(']');
                position++; // Skip ]
            }
            // Direct array access
            else if (c == '[') {
                propertyPathBuilder.append('[');
                position++; // Skip [
                skipWhitespace();

                // Parse the array index
                if (!Character.isDigit(input.charAt(position)) && input.charAt(position) != '-') {
                    throw new SyntaxException("Array index must be a number", position);
                }
                int start = position;
                while (position < input.length() && Character.isDigit(input.charAt(position))) {
                    position++;
                }
                propertyPathBuilder.append(input, start, position);
                skipWhitespace();

                if (position >= input.length() || input.charAt(position) != ']') {
                    throw new SyntaxException("Expected ']' after array index", position);
                }
                propertyPathBuilder.append(']');
                position++; // Skip ]
            }
            // Regular property access
            else if (c == '.') {
                position++; // Skip .

                // Parse the next property name
                int start = position;
                while (position < input.length()) {
                    c = input.charAt(position);
                    if (Character.isLetterOrDigit(c) || c == '_') {
                        position++;
                    } else {
                        break;
                    }
                }
                if (position > start) {
                    propertyPathBuilder.append(input, start, position);
                }

                // Continue parsing the rest of the property path
                while (position < input.length()) {
                    skipWhitespace();
                    c = input.charAt(position);

                    // Null-safe operator in the middle of property path
                    if (c == '?' && position + 1 < input.length() && input.charAt(position + 1) == '.') {
                        isNullSafe = true;
                        position += 2; // Skip ?.
                        propertyPathBuilder.append('.');

                        // Parse the next property name
                        start = position;
                        while (position < input.length()) {
                            c = input.charAt(position);
                            if (Character.isLetterOrDigit(c) || c == '_') {
                                position++;
                            } else {
                                break;
                            }
                        }
                        if (position > start) {
                            propertyPathBuilder.append(input, start, position);
                        }
                    }
                    // Regular dot access
                    else if (c == '.') {
                        position++; // Skip dot
                        propertyPathBuilder.append('.');

                        // Parse the next property name
                        start = position;
                        while (position < input.length()) {
                            c = input.charAt(position);
                            if (Character.isLetterOrDigit(c) || c == '_') {
                                position++;
                            } else {
                                break;
                            }
                        }
                        if (position > start) {
                            propertyPathBuilder.append(input, start, position);
                        }
                    }
                    // Null-safe array access ?[
                    else if (c == '?' && position + 1 < input.length() && input.charAt(position + 1) == '[') {
                        isNullSafe = true;
                        position++; // Skip ?
                        propertyPathBuilder.append('?');

                        // Now handle the array access
                        propertyPathBuilder.append('[');
                        position++; // Skip [
                        skipWhitespace();

                        // Parse the array index
                        if (!Character.isDigit(input.charAt(position)) && input.charAt(position) != '-') {
                            throw new SyntaxException("Array index must be a number", position);
                        }
                        start = position;
                        while (position < input.length() && Character.isDigit(input.charAt(position))) {
                            position++;
                        }
                        propertyPathBuilder.append(input, start, position);
                        skipWhitespace();

                        if (position >= input.length() || input.charAt(position) != ']') {
                            throw new SyntaxException("Expected ']' after array index", position);
                        }
                        propertyPathBuilder.append(']');
                        position++; // Skip ]
                    }
                    // Regular array access
                    else if (c == '[') {
                        propertyPathBuilder.append('[');
                        position++; // Skip [
                        skipWhitespace();

                        // Parse the array index
                        if (!Character.isDigit(input.charAt(position)) && input.charAt(position) != '-') {
                            throw new SyntaxException("Array index must be a number", position);
                        }
                        start = position;
                        while (position < input.length() && Character.isDigit(input.charAt(position))) {
                            position++;
                        }
                        propertyPathBuilder.append(input, start, position);
                        skipWhitespace();

                        if (position >= input.length() || input.charAt(position) != ']') {
                            throw new SyntaxException("Expected ']' after array index", position);
                        }
                        propertyPathBuilder.append(']');
                        position++; // Skip ]
                    }
                    // Null coalescing operator
                    else if (c == '?' && position + 1 < input.length() && input.charAt(position + 1) == '?') {
                        position += 2; // Skip ??
                        skipWhitespace();
                        Expression defaultValue = parseExpression();
                        return new NullCoalescingExpression(
                                new VariableExpression(name, propertyPathBuilder.toString(), isNullSafe),
                                defaultValue
                        );
                    } else {
                        break;
                    }
                }
            }
        }

        // Check for null coalescing operator at the end
        skipWhitespace();
        if (position + 1 < input.length() &&
                input.charAt(position) == '?' &&
                input.charAt(position + 1) == '?') {
            position += 2; // Skip ??
            skipWhitespace();
            Expression defaultValue = parseExpression();
            return new NullCoalescingExpression(
                    new VariableExpression(name, !propertyPathBuilder.isEmpty() ? propertyPathBuilder.toString() : null, isNullSafe),
                    defaultValue
            );
        }

        return new VariableExpression(name, !propertyPathBuilder.isEmpty() ? propertyPathBuilder.toString() : null, isNullSafe);
    }

    private Expression parseFunctionCall() {
        int start = position;
        while (position < input.length()) {
            char c = input.charAt(position);
            if (Character.isLetterOrDigit(c) || c == '_') {
                position++;
            } else {
                break;
            }
        }

        String functionName = input.substring(start, position);
        skipWhitespace();

        if (position >= input.length() || input.charAt(position) != '(') {
            throw new SyntaxException("Expected '(' after function name", position);
        }
        position++; // Skip (

        List<Expression> arguments = new ArrayList<>();
        skipWhitespace();

        while (position < input.length()) {
            if (input.charAt(position) == ')') {
                position++; // Skip )
                return new FunctionCallExpression(functionName, arguments);
            }

            arguments.add(parseExpression());
            skipWhitespace();

            if (position < input.length() && input.charAt(position) == ',') {
                position++; // Skip ,
                skipWhitespace();
            }
        }

        throw new SyntaxException("Unterminated function call", position);
    }

    private void skipWhitespace() {
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }
} 