package com.trackme.trackmeapplication.request.groupRequest.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.trackme.trackmeapplication.httpConnection.BusinessURLManager;
import com.trackme.trackmeapplication.httpConnection.ConnectionBuilder;
import com.trackme.trackmeapplication.httpConnection.LockInterface;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.exception.RequestNotWellFormedException;
import com.trackme.trackmeapplication.request.exception.ThirdPartyBlockedException;
import com.trackme.trackmeapplication.request.groupRequest.AggregatorOperator;
import com.trackme.trackmeapplication.request.groupRequest.ComparisonSymbol;
import com.trackme.trackmeapplication.request.groupRequest.FieldType;
import com.trackme.trackmeapplication.request.groupRequest.GroupRequestBuilder;
import com.trackme.trackmeapplication.request.groupRequest.GroupRequestWrapper;
import com.trackme.trackmeapplication.request.groupRequest.RequestType;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implement the group request interface
 *
 * @author Mattia Tibaldi
 * @see GroupRequestNetworkInterface
 * @see LockInterface
 */
public class GroupRequestNetworkImp implements GroupRequestNetworkInterface, LockInterface {

    private static GroupRequestNetworkImp instance = null;

    private final Object lock = new Object();

    /**
     * Private constructor.
     */
    private GroupRequestNetworkImp() {

    }

    public static GroupRequestNetworkImp getInstance() {
        if(instance == null)
            instance = new GroupRequestNetworkImp();
        return instance;
    }

    @Override
    public List<String> getAggregators() {
        return Stream.of(AggregatorOperator.values())
                .map(AggregatorOperator::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRequestTypes() {
        return Stream.of(RequestType.values())
                .map(RequestType::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getOperators() {
        return Stream.of(ComparisonSymbol.values())
                .map(ComparisonSymbol::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDbColumns() {
        return Stream.of(FieldType.values())
                .map(FieldType::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupRequestWrapper> getGroupRequest(String token) throws ConnectionException {
        synchronized (lock) {
            ObjectMapper mapper = new ObjectMapper();
            HttpHeaders httpHeaders = new HttpHeaders();
            BusinessURLManager businessURLManager = BusinessURLManager.getInstance();
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.getGroupRequestsLink())
                        .setHttpMethod(HttpMethod.GET).setEntity(entity).getConnection().start();
                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()){
                    case OK:
                        List<LinkedHashMap<String, String>> list = JsonPath.read(
                                connectionBuilder.getConnection().getResponse(), "$..groupRequestWrapperList[*].groupRequest");
                        List<String> links = JsonPath.read(
                                connectionBuilder.getConnection().getResponse(), "$..href");
                        List<GroupRequestWrapper> groupRequestWrappers = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            GroupRequestWrapper groupRequestWrapper = mapper.readValue(
                                    mapper.writeValueAsString(list.get(i)),
                                    GroupRequestWrapper.class);
                            groupRequestWrapper.setSelfLink(links.get(i));
                            groupRequestWrappers.add(groupRequestWrapper);
                        }
                        return groupRequestWrappers;
                    case NOT_FOUND: return new ArrayList<>();
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
    }

    @Override
    public void send(String token, GroupRequestBuilder groupRequestBuilder) throws RequestNotWellFormedException, ConnectionException, ThirdPartyBlockedException, UserNotFoundException {
        synchronized (lock) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                HttpHeaders httpHeaders = new HttpHeaders();
                BusinessURLManager businessURLManager = BusinessURLManager.getInstance();
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(groupRequestBuilder), httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.getNewGroupRequestLink())
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();
                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()){
                    case OK: break;
                    case CREATED: break;
                    case NOT_FOUND: throw new UserNotFoundException();
                    case UNAUTHORIZED: throw new ThirdPartyBlockedException();
                    case BAD_REQUEST: throw new RequestNotWellFormedException();
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException | JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getLock() {
        return lock;
    }

}
