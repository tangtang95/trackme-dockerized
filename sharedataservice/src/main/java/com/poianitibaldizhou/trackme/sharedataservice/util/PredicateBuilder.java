package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder pattern class to create dynamic predicate based on filter statements
 */
public class PredicateBuilder {

    private List<FilterStatement> filterStatements;

    /**
     * Constructor.
     * Create a new predicate builder by initializing the list of filter statements
     */
    public PredicateBuilder(){
        filterStatements = new ArrayList<>();
    }

    /**
     * Add a filter statement to the list of filterStatements
     *
     * @param filterStatement the filter statement to be added
     * @return true if the insertion is successful otherwise false
     */
    public boolean addFilterStatement(FilterStatement filterStatement){
        return filterStatements.add(filterStatement);
    }

    /**
     * Builds a where expression containing all the constraint defined by all the filter statements concatenated by
     * an AND
     *
     * @return the where predicate containing all the constraint defined by filter statements
     */
    public Predicate build(){
        BooleanBuilder where = new BooleanBuilder();
        for(FilterStatement filterStatement: filterStatements) {
            Operator operator = ComparisonSymbolUtils.getSqlOperator(filterStatement.getComparisonSymbol());

            Expression<?> expression = filterStatement.getColumn().getExpression();

            Predicate predicate = Expressions.predicate(operator, expression,
                    Expressions.constant(filterStatement.getValue()));
            where.and(predicate);
        }
        filterStatements = new ArrayList<>();
        return where;
    }

}
