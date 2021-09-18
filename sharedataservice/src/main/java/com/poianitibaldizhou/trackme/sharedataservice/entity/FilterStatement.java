package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.poianitibaldizhou.trackme.sharedataservice.util.ComparisonSymbol;
import com.poianitibaldizhou.trackme.sharedataservice.util.FieldType;
import lombok.Data;

import javax.persistence.*;

/**
 * JPA entity object regarding the filter about a group request
 */
@Data
@Entity
public class FilterStatement {

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

    public static FilterStatement newFilterStatement(Long id, FieldType fieldType, String value,
                                                     ComparisonSymbol comparisonSymbol, GroupRequest groupRequest){
        FilterStatement filterStatement = new FilterStatement();
        filterStatement.setId(id);
        filterStatement.setColumn(fieldType);
        filterStatement.setComparisonSymbol(comparisonSymbol);
        filterStatement.setValue(value);
        filterStatement.setGroupRequest(groupRequest);
        return filterStatement;
    }

}
