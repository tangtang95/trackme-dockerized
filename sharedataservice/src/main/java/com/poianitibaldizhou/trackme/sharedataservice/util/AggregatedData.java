package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class AggregatedData {

    @JsonView(Views.Internal.class)
    private Long thirdPartyId;

    @JsonView(Views.Internal.class)
    private Long groupRequestId;

    private Double value;

    private Timestamp generatedTimestamp;


    public static AggregatedData newAggregatedData(Long thirdPartyId, Long groupRequestId, Double value,
                                                   Timestamp generatedTimestamp) {
        AggregatedData aggregatedData = new AggregatedData();
        aggregatedData.setThirdPartyId(thirdPartyId);
        aggregatedData.setGroupRequestId(groupRequestId);
        aggregatedData.setValue(value);
        aggregatedData.setGeneratedTimestamp(generatedTimestamp);
        return aggregatedData;
    }
}
