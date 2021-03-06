package com.buffalosoftware.api.processengine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcessInstanceVariable {
   UNIT_RECRUITMENT_TIME(true),
   UNIT_AMOUNT_LEFT(false),
   RECRUITMENT_ID(false),
   CITY_BUILDING_ID(false),
   BUILDING_CONSTRUCTION_TIME(true),
   CONSTRUCTION_ID(false),
   CITY_ID(false),
   PROMOTION_ID(false),
   UNIT_PROMOTION_TIME(true),
   RESOURCE_PRODUCTION_TIME(true),
   RESOURCE_PRODUCTION_AMOUNT(false),
   RESOURCE_PRODUCTION_NAME(false);

   private boolean isTimeValue;
}
