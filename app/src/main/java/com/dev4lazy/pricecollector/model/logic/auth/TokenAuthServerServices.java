package com.dev4lazy.pricecollector.model.logic.auth;

public interface TokenAuthServerServices {

    boolean isAuthServerAvailable();

    void sendCredentials( String login, String password );

    void TASSCallIfSuccesfull();

    void TASSCallIfUnsuccessfull();

}
