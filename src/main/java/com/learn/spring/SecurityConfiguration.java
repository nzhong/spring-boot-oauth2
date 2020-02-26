package com.learn.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AbClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AbOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.AbOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {

        ClientRegistration okta = CommonOAuth2Provider.OKTA
                .getBuilder("okta")
                .clientId("0oa276p844RkFkXWA357")
                .clientSecret("6bk6aYJVgLIPmRTeATNg8QbpJmqKSk2NUPVf2fMp")
                .authorizationUri("https://dev-727231.okta.com/oauth2/default/v1/authorize")
                .tokenUri("/tokenUri")
                .redirectUriTemplate("http://localhost:8080/my/oauth2/code/okta")
                .build();
        ClientRegistrationRepository clientRegistrationnRepo = new AbClientRegistrationRepository(okta);

        AbOAuth2AuthorizedClientService authorizedClientService = new AbOAuth2AuthorizedClientService(clientRegistrationnRepo);

        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().oauth2Login()
                .authorizationEndpoint()
                .baseUri("/my/oauth2/authorization")
                .authorizationRequestResolver(
                        new AbOAuth2AuthorizationRequestResolver(
                                clientRegistrationnRepo, "/my/oauth2/authorization")
                )
                //.and().redirectionEndpoint()
                //.baseUri("/my/oauth2/code")
                .and()
                .loginProcessingUrl("/my/oauth2/code/okta")
                .authorizedClientService(authorizedClientService)
        ;
    }
}