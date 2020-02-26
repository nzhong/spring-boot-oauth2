package org.springframework.security.oauth2.client.web;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AbClientRegistrationRepository implements ClientRegistrationRepository, Iterable<ClientRegistration> {
    private final Map<String, ClientRegistration> registrations;

    /**
     * Constructs an {@code InMemoryClientRegistrationRepository} using the provided parameters.
     *
     * @param registrations the client registration(s)
     */
    public AbClientRegistrationRepository(ClientRegistration... registrations) {
        this(Arrays.asList(registrations));
    }

    /**
     * Constructs an {@code InMemoryClientRegistrationRepository} using the provided parameters.
     *
     * @param registrations the client registration(s)
     */
    public AbClientRegistrationRepository(List<ClientRegistration> registrations) {
        this(createRegistrationsMap(registrations));
    }

    private static Map<String, ClientRegistration> createRegistrationsMap(List<ClientRegistration> registrations) {
        Assert.notEmpty(registrations, "registrations cannot be empty");
        return toUnmodifiableConcurrentMap(registrations);
    }

    private static Map<String, ClientRegistration> toUnmodifiableConcurrentMap(List<ClientRegistration> registrations) {
        ConcurrentHashMap<String, ClientRegistration> result = new ConcurrentHashMap<>();
        for (ClientRegistration registration : registrations) {
            if (result.containsKey(registration.getRegistrationId())) {
                throw new IllegalStateException(String.format("Duplicate key %s",
                        registration.getRegistrationId()));
            }
            result.put(registration.getRegistrationId(), registration);
        }
        return Collections.unmodifiableMap(result);
    }

    /**
     * Constructs an {@code InMemoryClientRegistrationRepository} using the provided {@code Map}
     * of {@link ClientRegistration#getRegistrationId() registration id} to {@link ClientRegistration}.
     *
     * @since 5.2
     * @param registrations the {@code Map} of client registration(s)
     */
    public AbClientRegistrationRepository(Map<String, ClientRegistration> registrations) {
        Assert.notNull(registrations, "registrations cannot be null");
        this.registrations = registrations;
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        Assert.hasText(registrationId, "registrationId cannot be empty");
        return this.registrations.get(registrationId);
    }

    /**
     * Returns an {@code Iterator} of {@link ClientRegistration}.
     *
     * @return an {@code Iterator<ClientRegistration>}
     */
    @Override
    public Iterator<ClientRegistration> iterator() {
        return this.registrations.values().iterator();
    }
}


