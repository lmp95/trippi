package com.example.trippi;

import android.app.Application;

public class CurrentUser extends Application {
    UserAccount userAccount;

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
