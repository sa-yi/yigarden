package com.sayi.yi_garden.entity;

import com.google.gson.annotations.SerializedName;

public class JwtToken {

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("iat")
    private long iat; // Issued At

    @SerializedName("expires_in")
    private long expiresIn;

    @SerializedName("jwt_token")
    private String jwtToken;

    // Getters and Setters
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "JwtToken{" +
                "tokenType='" + tokenType + '\'' +
                ", iat=" + iat +
                ", expiresIn=" + expiresIn +
                ", jwtToken='" + jwtToken + '\'' +
                '}';
    }
}
