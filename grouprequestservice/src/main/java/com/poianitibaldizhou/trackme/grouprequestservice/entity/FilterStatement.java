package com.poianitibaldizhou.trackme.grouprequestservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poianitibaldizhou.trackme.grouprequestservice.util.ComparisonSymbol;
import com.poianitibaldizhou.trackme.grouprequestservice.util.FieldType;
import lombok.Data;

import javax.persistence.*;

/**
 * JPA entity object regarding the filter about a group request
 */
@Data
@Entity
public class FilterStatement {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filter_column", length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private FieldType column;

    @Column(length = 50, nullable = false)
    private String value;

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ComparisonSymbol comparisonSymbol = ComparisonSymbol.EQUALS;

    @ManyToOne
    @JoinColumn(name = "group_request_id", nullable = false)
    private GroupRequest groupRequest;
}


