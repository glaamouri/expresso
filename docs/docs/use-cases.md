---
id: use-cases
title: Use Cases
sidebar_position: 8
---

# Use Cases

This page demonstrates how Expresso can be applied in real-world scenarios.

## User-Defined Formulas

Allow users to define custom calculation formulas that can be evaluated at runtime:

```java
// A pricing calculator allowing custom discount formulas
public class PricingCalculator {
    private final ExpressionEvaluator evaluator;
    private final Map<String, String> savedFormulas;
    
    public PricingCalculator() {
        this.evaluator = new ExpressionEvaluator();
        this.savedFormulas = new HashMap<>();
        
        // Example saved formulas
        savedFormulas.put("standardDiscount", "$price * (1 - $discountRate)");
        savedFormulas.put("bulkDiscount", "$quantity >= 10 ? $price * 0.8 : $price * 0.9");
        savedFormulas.put("seasonalDiscount", "$isSeason ? $price * 0.7 : $price * 0.95");
    }
    
    public double calculatePrice(String formulaName, Map<String, Object> variables) {
        String formula = savedFormulas.get(formulaName);
        if (formula == null) {
            throw new IllegalArgumentException("Formula not found: " + formulaName);
        }
        
        Context context = new Context();
        variables.forEach(context::setVariable);
        
        return (Double) evaluator.evaluate(formula, context);
    }
    
    public void saveFormula(String name, String formula) {
        // Validate formula before saving
        ValidationResult result = evaluator.validateSyntax(formula);
        if (!result.isValid()) {
            throw new IllegalArgumentException("Invalid formula: " + result.getFirstError().getMessage());
        }
        
        savedFormulas.put(name, formula);
    }
}

// Usage
PricingCalculator calculator = new PricingCalculator();

// Using a pre-defined formula
Map<String, Object> orderDetails = Map.of(
    "price", 100.0,
    "quantity", 12,
    "discountRate", 0.15,
    "isSeason", true
);

double price1 = calculator.calculatePrice("standardDiscount", orderDetails); // 85.0
double price2 = calculator.calculatePrice("bulkDiscount", orderDetails);     // 80.0
double price3 = calculator.calculatePrice("seasonalDiscount", orderDetails); // 70.0

// Adding a custom formula
calculator.saveFormula("customDiscount", 
    "$quantity >= 20 ? $price * 0.7 : ($quantity >= 10 ? $price * 0.8 : $price * 0.9)");

double price4 = calculator.calculatePrice("customDiscount", orderDetails);   // 80.0
```

## Dynamic Business Rules

Store and evaluate business rules in a database:

```java
public class BusinessRuleEngine {
    private final ExpressionEvaluator evaluator;
    private final Map<String, BusinessRule> ruleRepository;
    
    public BusinessRuleEngine() {
        this.evaluator = new ExpressionEvaluator();
        this.ruleRepository = loadRulesFromDatabase();
    }
    
    private Map<String, BusinessRule> loadRulesFromDatabase() {
        // Simulating database retrieval
        Map<String, BusinessRule> rules = new HashMap<>();
        
        // Example rules
        rules.put("creditApproval", new BusinessRule(
            "Credit Approval",
            "$creditScore >= 700 && $income >= 50000 && $debtRatio < 0.4",
            "Determines if a customer is eligible for credit"
        ));
        
        rules.put("shippingDiscount", new BusinessRule(
            "Free Shipping",
            "$orderTotal > 100 || $membershipLevel == 'PREMIUM'",
            "Determines if an order qualifies for free shipping"
        ));
        
        return rules;
    }
    
    public boolean evaluateRule(String ruleName, Map<String, Object> facts) {
        BusinessRule rule = ruleRepository.get(ruleName);
        if (rule == null) {
            throw new IllegalArgumentException("Rule not found: " + ruleName);
        }
        
        Context context = new Context();
        facts.forEach(context::setVariable);
        
        ValidationResult validation = evaluator.validateWithContext(rule.getCondition(), context);
        if (!validation.isValid()) {
            throw new IllegalStateException("Invalid rule: " + validation.getFirstError().getMessage());
        }
        
        return (Boolean) evaluator.evaluate(rule.getCondition(), context);
    }
    
    static class BusinessRule {
        private final String name;
        private final String condition;
        private final String description;
        
        public BusinessRule(String name, String condition, String description) {
            this.name = name;
            this.condition = condition;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getCondition() { return condition; }
        public String getDescription() { return description; }
    }
}

// Usage
BusinessRuleEngine ruleEngine = new BusinessRuleEngine();

// Evaluate credit approval rule
Map<String, Object> applicantData = Map.of(
    "creditScore", 720,
    "income", 65000.0,
    "debtRatio", 0.35
);

boolean creditApproved = ruleEngine.evaluateRule("creditApproval", applicantData);
// creditApproved = true

// Evaluate shipping rule
Map<String, Object> orderData = Map.of(
    "orderTotal", 95.0,
    "membershipLevel", "PREMIUM"
);

boolean freeShipping = ruleEngine.evaluateRule("shippingDiscount", orderData);
// freeShipping = true
```

## Dynamic Content Generation

Generate dynamic content based on contextual variables:

```java
public class ContentTemplate {
    private final ExpressionEvaluator evaluator;
    private final String templateContent;
    private final Pattern expressionPattern;
    
    public ContentTemplate(String template) {
        this.evaluator = new ExpressionEvaluator();
        this.templateContent = template;
        this.expressionPattern = Pattern.compile("\\{\\{(.+?)\\}\\}");
    }
    
    public String generate(Map<String, Object> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);
        
        Matcher matcher = expressionPattern.matcher(templateContent);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String expression = matcher.group(1);
            Object value = evaluator.evaluate(expression, context);
            matcher.appendReplacement(result, value != null ? value.toString() : "");
        }
        
        matcher.appendTail(result);
        return result.toString();
    }
}

// Usage
String template = "Hello {{$user.name}},\n\n" +
                  "Your current balance is {{formatCurrency($account.balance)}}.\n" +
                  "{{$account.balance < 100 ? 'Your balance is low.' : ''}}\n\n" +
                  "Your membership level is {{upperCase($user.membershipLevel)}}.\n" +
                  "{{$user.membershipLevel == 'premium' ? 'Thank you for being a premium member!' : ''}}\n\n" +
                  "Regards,\nThe Team";

ContentTemplate emailTemplate = new ContentTemplate(template);

Map<String, Object> variables = new HashMap<>();
variables.put("user", Map.of(
    "name", "John Smith",
    "membershipLevel", "premium"
));
variables.put("account", Map.of(
    "balance", 75.50,
    "accountNumber", "AC-12345"
));

String emailContent = emailTemplate.generate(variables);
// Result would be:
// Hello John Smith,
//
// Your current balance is $75.50.
// Your balance is low.
//
// Your membership level is PREMIUM.
// Thank you for being a premium member!
//
// Regards,
// The Team
```

## Configurable Workflows

Use Expresso to create configurable workflow conditions:

```java
public class WorkflowEngine {
    private final ExpressionEvaluator evaluator;
    private List<WorkflowStep> steps;
    
    public WorkflowEngine() {
        this.evaluator = new ExpressionEvaluator();
        this.steps = loadWorkflowConfiguration();
    }
    
    private List<WorkflowStep> loadWorkflowConfiguration() {
        // In a real application, this would be loaded from a database or config file
        return Arrays.asList(
            new WorkflowStep(
                "validateOrder",
                "$order.items.size() > 0 && $order.totalAmount > 0",
                "Order validation failed"
            ),
            new WorkflowStep(
                "checkInventory",
                "allItemsInStock($order.items)",
                "Some items are out of stock"
            ),
            new WorkflowStep(
                "processCreditCard",
                "$customer.creditStatus == 'APPROVED' && $order.totalAmount <= $customer.creditLimit",
                "Credit card processing failed"
            ),
            new WorkflowStep(
                "scheduleShipping",
                "true",
                "Shipping scheduling failed"
            )
        );
    }
    
    // Process a workflow with the given context data
    public WorkflowResult processWorkflow(Map<String, Object> contextData) {
        Context context = new Context();
        contextData.forEach(context::setVariable);
        
        for (WorkflowStep step : steps) {
            try {
                boolean condition = (Boolean) evaluator.evaluate(step.getCondition(), context);
                if (!condition) {
                    return new WorkflowResult(false, step.getStepName(), step.getErrorMessage());
                }
            } catch (Exception e) {
                return new WorkflowResult(false, step.getStepName(), 
                    "Error evaluating condition: " + e.getMessage());
            }
        }
        
        return new WorkflowResult(true, "complete", "Workflow completed successfully");
    }
    
    static class WorkflowStep {
        private final String stepName;
        private final String condition;
        private final String errorMessage;
        
        public WorkflowStep(String stepName, String condition, String errorMessage) {
            this.stepName = stepName;
            this.condition = condition;
            this.errorMessage = errorMessage;
        }
        
        public String getStepName() { return stepName; }
        public String getCondition() { return condition; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    static class WorkflowResult {
        private final boolean success;
        private final String stepName;
        private final String message;
        
        public WorkflowResult(boolean success, String stepName, String message) {
            this.success = success;
            this.stepName = stepName;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getStepName() { return stepName; }
        public String getMessage() { return message; }
    }
}
```

## Data Transformation

Transform data using dynamic Expresso expressions:

```java
public class DataTransformer {
    private final ExpressionEvaluator evaluator;
    private final Map<String, String> transformationRules;
    
    public DataTransformer() {
        this.evaluator = new ExpressionEvaluator();
        this.transformationRules = new HashMap<>();
        
        // Register custom functions
        evaluator.registerFunction("convertToUSD", args -> {
            double amount = ((Number) args[0]).doubleValue();
            String fromCurrency = (String) args[1];
            
            // Simplified conversion logic
            Map<String, Double> rates = Map.of(
                "EUR", 1.1,
                "GBP", 1.3,
                "JPY", 0.009
            );
            
            return amount * rates.getOrDefault(fromCurrency, 1.0);
        });
        
        // Example transformation rules
        transformationRules.put("fullName", "$customer.firstName + ' ' + $customer.lastName");
        transformationRules.put("discountedPrice", "$product.price * (1 - $discountRate)");
        transformationRules.put("usdAmount", "convertToUSD($transaction.amount, $transaction.currency)");
        transformationRules.put("formattedAddress", 
            "$customer.address.street + ', ' + $customer.address.city + ', ' + $customer.address.postalCode");
    }
    
    public Map<String, Object> transform(String ruleName, Map<String, Object> sourceData) {
        if (!transformationRules.containsKey(ruleName)) {
            throw new IllegalArgumentException("Transformation rule not found: " + ruleName);
        }
        
        Context context = new Context();
        sourceData.forEach(context::setVariable);
        
        String expression = transformationRules.get(ruleName);
        Object result = evaluator.evaluate(expression, context);
        
        return Map.of("result", result);
    }
    
    public Map<String, Object> batchTransform(Map<String, Object> sourceData) {
        Context context = new Context();
        sourceData.forEach(context::setVariable);
        
        Map<String, Object> result = new HashMap<>();
        
        for (Map.Entry<String, String> rule : transformationRules.entrySet()) {
            try {
                result.put(rule.getKey(), evaluator.evaluate(rule.getValue(), context));
            } catch (Exception e) {
                // Skip rules that can't be evaluated with the given data
            }
        }
        
        return result;
    }
}
```

These examples demonstrate how Expresso's expression evaluation capability can be applied to solve real-world problems with dynamic, configurable logic. 