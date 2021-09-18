package com.trackme.trackmeapplication.request.groupRequest;

import com.trackme.trackmeapplication.baseUtility.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Build a group request with all the filter add by the user
 *
 * @author Mattia Tibaldi
 */
public class GroupRequestBuilder implements Serializable {

    //attributes
    private GroupRequest groupRequest;
    private List<LinkedHashMap<String, String>> filterStatementList;

    /**
     * Constructor
     */
    GroupRequestBuilder(){
        filterStatementList = new ArrayList<>();
    }

    /**
     * Add new filter to the builder
     *
     * @param filters new filter to add
     */
    void addNewFilter(String[] filters){
        LinkedHashMap<String, String> item = new LinkedHashMap<>();
        for (String filter : filters) {
            String[] param = filter.split(" ");
            item.put(Constant.MAP_COLUMN_KEY, param[0]);
            item.put(Constant.MAP_COMPARISON_SYMBOL_KEY, param[1]);
            item.put(Constant.MAP_VALUE_KEY, param[2]);
            filterStatementList.add(item);
        }
    }

    /**
     * Setter method.
     *
     * @param groupRequest new group request
     */
    public void setGroupRequest(GroupRequest groupRequest) {
        this.groupRequest = groupRequest;
    }

    //getter methods
    public GroupRequest getGroupRequest() {
        return groupRequest;
    }

    public List<LinkedHashMap<String, String>> getFilterStatementList() {
        return filterStatementList;
    }
}
