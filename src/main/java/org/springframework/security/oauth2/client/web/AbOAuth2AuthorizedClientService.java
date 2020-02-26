package org.springframework.security.oauth2.client.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientId;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {
    private final Map<OAuth2AuthorizedClientId, OAuth2AuthorizedClient> authorizedClients;
    private final ClientRegistrationRepository clientRegistrationRepository;

    /**
     * Constructs an {@code InMemoryOAuth2AuthorizedClientService} using the provided parameters.
     *
     * @param clientRegistrationRepository the repository of client registrations
     */
    public AbOAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
        System.out.println("AbOAuth2AuthorizedClientService");
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClients = new ConcurrentHashMap<>();
    }

    /**
     * Constructs an {@code InMemoryOAuth2AuthorizedClientService} using the provided parameters.
     *
     * @since 5.2
     * @param clientRegistrationRepository the repository of client registrations
     * @param authorizedClients the initial {@code Map} of authorized client(s) keyed by {@link OAuth2AuthorizedClientId}
     */
    public AbOAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository,
                                                 Map<OAuth2AuthorizedClientId, OAuth2AuthorizedClient> authorizedClients) {
        System.out.println("AbOAuth2AuthorizedClientService");
        Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
        Assert.notEmpty(authorizedClients, "authorizedClients cannot be empty");
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClients = new ConcurrentHashMap<>(authorizedClients);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        System.out.println("loadAuthorizedClient");
        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        if (registration == null) {
            return null;
        }
        return (T) this.authorizedClients.get(new OAuth2AuthorizedClientId(clientRegistrationId, principalName));
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        System.out.println("saveAuthorizedClient");
        Assert.notNull(authorizedClient, "authorizedClient cannot be null");
        Assert.notNull(principal, "principal cannot be null");
        this.authorizedClients.put(new OAuth2AuthorizedClientId(authorizedClient.getClientRegistration().getRegistrationId(),
                principal.getName()), authorizedClient);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        System.out.println("removeAuthorizedClient");
        Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
        if (registration != null) {
            this.authorizedClients.remove(new OAuth2AuthorizedClientId(clientRegistrationId, principalName));
        }
    }
}
