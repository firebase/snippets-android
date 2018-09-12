package com.google.firebase.referencecode.database.interfaces;

/**
 * Interface for {@link com.google.firebase.referencecode.database.QueryActivity}.
 */
public interface QueryActivityInterface {

    String getUid();

    void basicListen();

    void basicQuery();

    void basicQueryValueListener();

    void cleanBasicListener();

    void cleanBasicQuery();

    void orderByNested();

}
