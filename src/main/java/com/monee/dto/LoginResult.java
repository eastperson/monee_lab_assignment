package com.monee.dto;

import com.monee.model.Account;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class LoginResult {
    private final String accessToken;

    private final Account account;

    public LoginResult(String accessToken, Account account) {
        this.accessToken = accessToken;
        this.account = account;
    }

    public String getToken() {
        return accessToken;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("access_token", accessToken)
                .append("account", account)
                .toString();
    }
}
