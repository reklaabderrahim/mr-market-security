package fr.mr_market.mr_market_security.model.mail;

public enum MailStatus {

    PENDING("PENDING"),

    SENT("SENT"),

    FAILED("FAILED");

    private String value;

    MailStatus(String value) {
        this.value = value;
    }
}
