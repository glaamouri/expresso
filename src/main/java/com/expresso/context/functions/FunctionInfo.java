package com.expresso.context.functions;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains metadata information about a function.
 */
public class FunctionInfo {
    private final String name;
    private final String description;
    private final List<ParameterInfo> parameters;
    private final String returnType;
    private final boolean isBuiltIn;

    private FunctionInfo(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.parameters = new ArrayList<>(builder.parameters);
        this.returnType = builder.returnType;
        this.isBuiltIn = builder.isBuiltIn;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ParameterInfo> getParameters() {
        return new ArrayList<>(parameters);
    }

    public String getReturnType() {
        return returnType;
    }

    public boolean isBuiltIn() {
        return isBuiltIn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0)
                sb.append(", ");
            sb.append(parameters.get(i));
        }
        sb.append(") -> ").append(returnType);
        if (description != null && !description.isEmpty()) {
            sb.append("\n  ").append(description);
        }
        return sb.toString();
    }

    /**
     * Creates a new builder for FunctionInfo.
     *
     * @param name The function name
     * @return A new Builder instance
     */
    public static Builder builder(String name) {
        return new Builder(name);
    }

    /**
     * Builder class for FunctionInfo.
     */
    public static class Builder {
        private final String name;
        private String description = "";
        private final List<ParameterInfo> parameters = new ArrayList<>();
        private String returnType = "Object";
        private boolean isBuiltIn = true;

        private Builder(String name) {
            this.name = name;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder parameter(String name, String type, String description) {
            this.parameters.add(new ParameterInfo(name, type, description));
            return this;
        }

        public Builder parameter(String name, String type) {
            this.parameters.add(new ParameterInfo(name, type, ""));
            return this;
        }

        public Builder returnType(String returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder isBuiltIn(boolean isBuiltIn) {
            this.isBuiltIn = isBuiltIn;
            return this;
        }

        public FunctionInfo build() {
            return new FunctionInfo(this);
        }
    }

    /**
     * Contains information about a function parameter.
     */
    public static class ParameterInfo {
        private final String name;
        private final String type;
        private final String description;

        public ParameterInfo(String name, String type, String description) {
            this.name = name;
            this.type = type;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return name + ": " + type + (description.isEmpty() ? "" : " - " + description);
        }
    }
}
