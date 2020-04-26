package com.niza.app.givder.networking.actions;

public class RefreshTokenNetworkAction extends NetworkAction {
    private String action = "RefreshToken";
    private String refreshToken;

    public RefreshTokenNetworkAction(){}
    public RefreshTokenNetworkAction(String refreshToken){
        this.refreshToken = refreshToken;
    }
    @Override
    public String getAction() {
        return action;
    }

    @Override
    public void setAction(String action) {

    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
