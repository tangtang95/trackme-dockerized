package com.trackme.trackmeapplication.request.individualRequest.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.trackme.trackmeapplication.httpConnection.BusinessURLManager;
import com.trackme.trackmeapplication.httpConnection.ConnectionBuilder;
import com.trackme.trackmeapplication.httpConnection.LockInterface;
import com.trackme.trackmeapplication.httpConnection.UserURLManager;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.exception.RequestNotWellFormedException;
import com.trackme.trackmeapplication.request.exception.ThirdPartyBlockedException;
import com.trackme.trackmeapplication.request.individualRequest.IndividualRequest;
import com.trackme.trackmeapplication.request.individualRequest.IndividualRequestWrapper;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Implement the individual request interface
 *
 * @author Mattia Tibaldi
 * @see IndividualRequestNetworkIInterface
 * @see LockInterface
 */
public class IndividualRequestNetworkImp implements IndividualRequestNetworkIInterface, LockInterface {

    private static IndividualRequestNetworkImp instance = null;

    private ObjectMapper mapper;

    private final Object lock = new Object();

    /**
     * Private constructor
     */
    private IndividualRequestNetworkImp() {
        mapper = new ObjectMapper();
    }

    public static IndividualRequestNetworkImp getInstance() {
        if(instance == null)
            instance = new IndividualRequestNetworkImp();
        return instance;
    }

    @Override
    public List<IndividualRequestWrapper> getIndividualRequest(String token) throws ConnectionException {
        synchronized (lock) {
            BusinessURLManager businessURLManager = BusinessURLManager.getInstance();
            HttpHeaders httpHeaders = new HttpHeaders();
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.getIndividualRequestsLink())
                        .setHttpMethod(HttpMethod.GET).setEntity(entity).getConnection().start();
                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()){
                    case OK:
                        List<LinkedHashMap<String, String>> list = JsonPath.read(
                                connectionBuilder.getConnection().getResponse(), "$..individualRequestWrapperList[*]");
                        List<String> links = JsonPath.read(
                                connectionBuilder.getConnection().getResponse(), "$..href");
                        List<IndividualRequestWrapper> individualRequestWrappers = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            IndividualRequestWrapper individualRequestWrapper = mapper.readValue(
                                    mapper.writeValueAsString(list.get(i)),
                                    IndividualRequestWrapper.class);
                            individualRequestWrapper.setResponseLink(links.get(i));
                            individualRequestWrappers.add(individualRequestWrapper);
                        }
                        return individualRequestWrappers;
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
    public List<IndividualRequestWrapper> getOwnIndividualRequest(String token) throws ConnectionException {
        synchronized (lock) {
            UserURLManager userUrlManager = UserURLManager.getInstance();
            HttpHeaders httpHeaders = new HttpHeaders();
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(userUrlManager.getPendingRequestsLink())
                        .setHttpMethod(HttpMethod.GET).setEntity(entity).getConnection().start();
                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()){
                    case OK:
                        List<List<LinkedHashMap<String, String>>> list = JsonPath.read(
                                connectionBuilder.getConnection().getResponse(), "$..individualRequestWrapperList");
                        List<String> links = JsonPath.read(
                                connectionBuilder.getConnection().getResponse(), "$..href");
                        List<IndividualRequestWrapper> individualRequestWrappers = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            IndividualRequestWrapper individualRequestWrapper = mapper.readValue(
                                    mapper.writeValueAsString(list.get(0).get(i)),
                                    IndividualRequestWrapper.class);
                            individualRequestWrapper.setResponseLink(links.get(i));
                            individualRequestWrappers.add(individualRequestWrapper);
                        }
                        return individualRequestWrappers;
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
    public void acceptIndividualRequest(String token, String url) throws ConnectionException {
        String responseUrl = getResponseLink(token, url);
        synchronized (lock) {
            HttpHeaders httpHeaders = new HttpHeaders();
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>("ACCEPT", httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(responseUrl)
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();
                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() != HttpStatus.CREATED)
                    throw new ConnectionException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String refuseIndividualRequest(String token,String url) throws ConnectionException {
        String responseUrl = getResponseLink(token, url);
        synchronized (lock) {
            HttpHeaders httpHeaders = new HttpHeaders();
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>("REFUSE", httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(responseUrl)
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();
                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() == HttpStatus.CREATED) {
                    List<String> links = JsonPath.read(
                            connectionBuilder.getConnection().getResponse(), "$..blockThirdParty.href");
                    return links.get(0);
                } else throw new ConnectionException();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void blockThirdPartyCustomer(String token, String url) throws ConnectionException {
        synchronized (lock) {
            HttpHeaders httpHeaders = new HttpHeaders();
            try {
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(url)
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();
                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() != HttpStatus.CREATED)
                    throw new ConnectionException();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void send(String token, IndividualRequest individualRequest, String userSSN) throws ConnectionException, RequestNotWellFormedException, ThirdPartyBlockedException, UserNotFoundException {
        synchronized (lock) {
            HttpHeaders httpHeaders = new HttpHeaders();
            try {
                ObjectMapper mapper = new ObjectMapper();
                BusinessURLManager businessURLManager = BusinessURLManager.getInstance();
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(individualRequest), httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.getNewIndividualRequestLink() + "/" + userSSN)
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

    /**
     * Get the link to response a request from the server
     *
     * @param token user token
     * @param url url to call for having the link
     * @return the response link
     * @throws ConnectionException throw when the connection offline
     */
    private String getResponseLink(String token, String url) throws ConnectionException {
        synchronized (lock) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + token);
            try {
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(url)
                        .setHttpMethod(HttpMethod.GET).setEntity(entity).getConnection().start();
                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                if (connectionBuilder.getConnection().getStatusReturned() == HttpStatus.OK){
                    List<String> links = JsonPath.read(
                            connectionBuilder.getConnection().getResponse(), "$..addResponse.href");
                    return links.get(0);
                }
                throw new ConnectionException();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public Object getLock() {
        return lock;
    }

}
