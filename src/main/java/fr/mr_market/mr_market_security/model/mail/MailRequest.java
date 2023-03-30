package fr.mr_market.mr_market_security.model.mail;

import java.util.Set;

public record MailRequest(Set<String> recipients, MailType mailType, String callbackUrl) {
}
