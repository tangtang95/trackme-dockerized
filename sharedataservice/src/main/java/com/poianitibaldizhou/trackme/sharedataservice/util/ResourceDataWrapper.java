package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import lombok.Data;
import org.springframework.hateoas.Resource;

import java.util.List;

@Data
public class ResourceDataWrapper {

    @JsonView(Views.Internal.class)
    private String userSsn;

    private List<Resource<PositionData>> positionDataList;
    private List<Resource<HealthData>> healthDataList;

}
