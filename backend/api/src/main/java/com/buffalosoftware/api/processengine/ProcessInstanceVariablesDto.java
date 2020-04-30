package com.buffalosoftware.api.processengine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ProcessInstanceVariablesDto {
    private Map<ProcessInstanceVariable, Object> variables;

    private ProcessInstanceVariablesDto(Map<ProcessInstanceVariable, Object> variables) {
        this.variables = variables;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Map<ProcessInstanceVariable, Object> variables = new HashMap<>();

        public Builder variable(ProcessInstanceVariable name, Object value) {
            variables.put(name, value);
            return this;
        }

        public ProcessInstanceVariablesDto build() {
            return new ProcessInstanceVariablesDto(variables);
        }
    }

}
