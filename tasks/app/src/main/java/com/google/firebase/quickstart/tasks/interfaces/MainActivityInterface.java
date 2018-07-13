package com.google.firebase.quickstart.tasks.interfaces;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface MainActivityInterface {

    void basicTaskHandlers();

    void taskOnExecutor();

    void activityScopedTask();

    Task<String> doSomething(AuthResult authResult);

    void taskChaining();

    void blockingTask();
}