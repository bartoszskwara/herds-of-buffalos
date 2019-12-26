package com.buffalosoftware.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Resource {

    private Long amount;

    public static class Wood extends Resource {

        public Wood(Long amount) {
            super(amount);
        }
    }

    public static class Clay extends Resource {

        public Clay(Long amount) {
            super(amount);
        }
    }

    public static class Iron extends Resource {

        public Iron(Long amount) {
            super(amount);
        }
    }
}
