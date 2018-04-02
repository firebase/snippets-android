package com.google.firebase.referencecode.database.interfaces;

public interface ReadAndWriteSnippetsInterface {

    void writeNewUser(String userId, String name, String email);

    void writeNewUserWithTaskListeners(String userId, String name, String email);

}

