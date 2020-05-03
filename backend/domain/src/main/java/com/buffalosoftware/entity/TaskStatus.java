package com.buffalosoftware.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Getter
@AllArgsConstructor
public enum TaskStatus {
    Pending(2), InProgress(1), Completed(3);

    private int order;
    private static final Map<String, TaskStatus> names;

    static {
        names = Arrays.stream(values()).collect(toMap(TaskStatus::name, identity()));
    }

    public static TaskStatus getByName(String name) {
        return names.get(name);
    }

    public boolean notCompleted() {
        return !Completed.equals(this);
    }

    public boolean inProgress() {
        return InProgress.equals(this);
    }

    public boolean pending() {
        return Pending.equals(this);
    }
}
