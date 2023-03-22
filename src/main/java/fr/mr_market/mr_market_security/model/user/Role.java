package fr.mr_market.mr_market_security.model.user;

public enum Role {

  USER("USER"),
  MANAGER("MANAGER"),
  ADMIN("ADMIN");

  private final String value;

  Role(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
