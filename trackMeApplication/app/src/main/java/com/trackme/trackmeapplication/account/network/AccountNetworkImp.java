package com.trackme.trackmeapplication.account.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.trackme.trackmeapplication.account.exception.InvalidDataLoginException;
import com.trackme.trackmeapplication.account.exception.UserAlreadyLogoutException;
import com.trackme.trackmeapplication.account.exception.UserAlreadySignUpException;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.BusinessURLManager;
import com.trackme.trackmeapplication.httpConnection.ConnectionBuilder;
import com.trackme.trackmeapplication.httpConnection.LockInterface;
import com.trackme.trackmeapplication.httpConnection.UserURLManager;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.sharedData.CompanyDetail;
import com.trackme.trackmeapplication.sharedData.PrivateThirdPartyDetail;
import com.trackme.trackmeapplication.sharedData.ThirdPartyCompanyWrapper;
import com.trackme.trackmeapplication.sharedData.ThirdPartyInterface;
import com.trackme.trackmeapplication.sharedData.ThirdPartyPrivateWrapper;
import com.trackme.trackmeapplication.sharedData.User;
import com.trackme.trackmeapplication.sharedData.exception.UserNotFoundException;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkImp;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkInterface;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * AccountNetworkImp is a class that provide all the function to communicate with the account service
 * on the server.
 */
public class AccountNetworkImp implements AccountNetworkInterface, LockInterface {

    private static AccountNetworkImp instance = null;

    private final Object lock = new Object();

    /**
     * Constructor. (singleton)
     */
    private AccountNetworkImp() {
    }

    public static AccountNetworkImp getInstance(){
        if(instance == null)
            instance = new AccountNetworkImp();
        return instance;
    }


    @Override
    public String userLogin(String username, String password) throws InvalidDataLoginException, ConnectionException {
        synchronized (lock) {
            UserURLManager userUrlManager = UserURLManager.getInstance();
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
            try {

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                String url = userUrlManager.createURLWithPort(
                        Constant.PUBLIC_USER_API + Constant.LOGIN_USER_API + "?username=" + username + "&password=" + password);
                connectionBuilder.setUrl(url).setHttpMethod(HttpMethod.POST).setEntity(entity)
                        .getConnection().start();

                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()){
                    case OK:
                        //Log.d("BODY", connectionBuilder.getConnection().getResponse());
                        userUrlManager.setUrls(JsonPath.read(connectionBuilder.getConnection().getResponse(), "$.._links"));
                        List<String> list = JsonPath.read(connectionBuilder.getConnection().getResponse(), "$..token");
                        return list.get(0);
                    case UNAUTHORIZED: throw new InvalidDataLoginException();
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public String thirdPartyLogin(String email, String password) throws InvalidDataLoginException, ConnectionException {
        synchronized (lock) {
            HttpHeaders httpHeaders = new HttpHeaders();
            BusinessURLManager businessURLManager = BusinessURLManager.getInstance();
            HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
            try {

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                String url = businessURLManager.createURLWithPort(
                        Constant.PUBLIC_TP_API + Constant.LOGIN_USER_API+"?email="+email+"&password="+password);
                connectionBuilder.setUrl(url).setHttpMethod(HttpMethod.POST).setEntity(entity)
                        .getConnection().start();

                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()){
                    case OK:
                        //Log.d("BODY", connectionBuilder.getConnection().getResponse());
                        businessURLManager.setUrls(JsonPath.read(connectionBuilder.getConnection().getResponse(), "$.._links"));
                        List<String> list = JsonPath.read(connectionBuilder.getConnection().getResponse(), "$..token");
                        return list.get(0);
                    case UNAUTHORIZED: throw new InvalidDataLoginException();
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void userLogout(String token) throws UserAlreadyLogoutException, ConnectionException {
        synchronized (lock) {
            try {
                HttpHeaders httpHeaders = new HttpHeaders();
                UserURLManager userURLManager = UserURLManager.getInstance();
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(userURLManager.getLogoutLink()).setHttpMethod(HttpMethod.POST).setEntity(entity)
                        .getConnection().start();

                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();

                switch (connectionBuilder.getConnection().getStatusReturned()) {
                    case OK: break;
                    case UNAUTHORIZED: throw new UserAlreadyLogoutException();
                    default: throw new ConnectionException();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void thirdPartyLogout(String token) throws UserAlreadyLogoutException, ConnectionException {
        synchronized (lock) {
            try {
                HttpHeaders httpHeaders = new HttpHeaders();
                BusinessURLManager businessURLManager = BusinessURLManager.getInstance();
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.getLogoutLink()).setHttpMethod(HttpMethod.POST)
                        .setEntity(entity).getConnection().start();

                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();

                switch (connectionBuilder.getConnection().getStatusReturned()) {
                    case OK: break;
                    case UNAUTHORIZED: throw new UserAlreadyLogoutException();
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void userSignUp(User user) throws UserAlreadySignUpException, ConnectionException {
        synchronized (lock) {
            HttpHeaders httpHeaders = new HttpHeaders();
            UserURLManager userUrlManager = UserURLManager.getInstance();
            try {
                ObjectMapper mapper = new ObjectMapper();

                HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(user), httpHeaders);

                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(userUrlManager.createURLWithPort(
                        Constant.PUBLIC_USER_API + Constant.REGISTER_USER_API + user.extractSsn()))
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();

                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()) {
                    case INTERNAL_SERVER_ERROR: throw new ConnectionException();
                    case BAD_REQUEST: throw new UserAlreadySignUpException();
                    case CREATED: break;
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException | JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void thirdPartySignUp(PrivateThirdPartyDetail privateThirdPartyDetail) throws UserAlreadySignUpException, ConnectionException {
        synchronized (lock) {
            BusinessURLManager businessURLManager = BusinessURLManager.getInstance();
            try {
                HttpHeaders httpHeaders = new HttpHeaders();
                ThirdPartyPrivateWrapper thirdPartyPrivateWrapper = new ThirdPartyPrivateWrapper();
                thirdPartyPrivateWrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail);
                thirdPartyPrivateWrapper.setThirdPartyCustomer(privateThirdPartyDetail.getThirdPartyCustomer());
                ObjectMapper mapper= new ObjectMapper();

                HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(thirdPartyPrivateWrapper), httpHeaders);
                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.createURLWithPort(Constant.PUBLIC_TP_API + Constant.REGISTER_PRIVATE_TP_API))
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();

                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()) {
                    case INTERNAL_SERVER_ERROR: throw new ConnectionException();
                    case BAD_REQUEST: throw new UserAlreadySignUpException();
                    case CREATED: break;
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException | JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void companySignUp(CompanyDetail companyDetail) throws UserAlreadySignUpException, ConnectionException {
        synchronized (lock) {
            BusinessURLManager businessURLManager = BusinessURLManager.getInstance();
            try {
                HttpHeaders httpHeaders = new HttpHeaders();
                ThirdPartyCompanyWrapper thirdPartyCompanyWrapper = new ThirdPartyCompanyWrapper();
                thirdPartyCompanyWrapper.setCompanyDetail(companyDetail);
                thirdPartyCompanyWrapper.setThirdPartyCustomer(companyDetail.getThirdPartyCustomer());
                ObjectMapper mapper= new ObjectMapper();

                HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(thirdPartyCompanyWrapper), httpHeaders);
                ConnectionBuilder connectionBuilder = new ConnectionBuilder(this);
                connectionBuilder.setUrl(businessURLManager.createURLWithPort(Constant.PUBLIC_TP_API + Constant.REGISTER_COMPANY_TP_API))
                        .setHttpMethod(HttpMethod.POST).setEntity(entity).getConnection().start();

                while (connectionBuilder.getConnection().getStatusReturned() == null)
                    lock.wait();
                switch (connectionBuilder.getConnection().getStatusReturned()) {
                    case INTERNAL_SERVER_ERROR: throw new ConnectionException();
                    case BAD_REQUEST: throw new UserAlreadySignUpException();
                    case CREATED: break;
                    default: throw new ConnectionException();
                }
            } catch (InterruptedException | JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public User getUser(String token) throws UserNotFoundException, ConnectionException {
        SharedDataNetworkInterface sharedDataNetwork = SharedDataNetworkImp.getInstance();
        return sharedDataNetwork.getUser(token);
    }

    @Override
    public ThirdPartyInterface getThirdParty(String token) throws UserNotFoundException, ConnectionException {
        SharedDataNetworkInterface sharedDataNetwork = SharedDataNetworkImp.getInstance();
        return sharedDataNetwork.getThirdParty(token);
    }



    @Override
    public  Object getLock() {
        return lock;
    }



}
