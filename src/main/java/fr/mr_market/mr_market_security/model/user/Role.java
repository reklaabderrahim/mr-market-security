package fr.mr_market.mr_market_security.model.user;

public enum Role {

  USER("user"),
  ADMIN("admin");

  private final String value;

  Role(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
