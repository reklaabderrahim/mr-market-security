package fr.mr_market.mr_market_security.model.token;

public enum TokenType {
  ACCESS_TOKEN("access_token"), REFRESH_TOKEN("refresh_token"), ACTIVATION_TOKEN("activation_token");

  public final String value;
  TokenType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
