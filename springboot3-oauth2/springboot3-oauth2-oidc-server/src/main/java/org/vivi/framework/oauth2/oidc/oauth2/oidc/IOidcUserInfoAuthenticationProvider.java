package org.vivi.framework.oauth2.oidc.oauth2.oidc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.*;
import java.util.function.Function;

public class IOidcUserInfoAuthenticationProvider implements AuthenticationProvider {
    private final Log logger = LogFactory.getLog(this.getClass());

    private final OAuth2AuthorizationService authorizationService;
    private Function<OidcUserInfoAuthenticationContext,IOidcUserInfo> userInfoMapper = new IOidcUserInfoAuthenticationProvider.DefaultOidcUserInfoMapper();

    public IOidcUserInfoAuthenticationProvider(OAuth2AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OidcUserInfoAuthenticationToken userInfoAuthenticationToken = (OidcUserInfoAuthenticationToken) authentication;
        AbstractOAuth2TokenAuthenticationToken<?> accessTokenAuthentication = null;

        if (AbstractOAuth2TokenAuthenticationToken.class.isAssignableFrom(userInfoAuthenticationToken.getPrincipal().getClass())){
            accessTokenAuthentication = (AbstractOAuth2TokenAuthenticationToken<?>) userInfoAuthenticationToken.getPrincipal();
        }

        if (accessTokenAuthentication != null && accessTokenAuthentication.isAuthenticated()){
            String accessTokenValue = accessTokenAuthentication.getToken().getTokenValue();
            OAuth2Authorization auth2Authorization = this.authorizationService.findByToken(accessTokenValue, OAuth2TokenType.ACCESS_TOKEN);
            if (authentication == null){
                throw new OAuth2AuthenticationException("invalid_token");
            }else {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Retrieved authorization with access token");
                }

                OAuth2Authorization.Token<OAuth2AccessToken> authorizationAccessToken = auth2Authorization.getAccessToken();
                if (!authorizationAccessToken.isActive()){
                    throw new OAuth2AuthenticationException("invalid_token");
                }else {
                    //从认证结果中获取userInfo
                    IOidcUserInfo myOidcUserInfo = (IOidcUserInfo)userInfoAuthenticationToken.getUserInfo();
                    //从authorizedAccessToken中获取授权范围
                    Set<String> scopeSet = (HashSet<String>)authorizationAccessToken.getClaims().get("scope") ;
                    //获取授权范围对应userInfo的字段信息
                    Map<String, Object> claims = DefaultOidcUserInfoMapper.getClaimsRequestedByScope(myOidcUserInfo.getClaims(),scopeSet);
                    if (this.logger.isTraceEnabled()) {
                        this.logger.trace("Authenticated user info request");
                    }
                    //构造新的OidcUserInfoAuthenticationToken
                    return new OidcUserInfoAuthenticationToken(accessTokenAuthentication, new IOidcUserInfo(claims));
                }
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OidcUserInfoAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static final class DefaultOidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext,IOidcUserInfo>{
        private static final List<String> EMAIL_CLAIMS = Arrays.asList("email", "email_verified");
        private static final List<String> PHONE_CLAIMS = Arrays.asList("phone_number", "phone_number_verified");
        private static final List<String> PROFILE_CLAIMS = Arrays.asList("name", "username", "description", "status", "profile");
        private DefaultOidcUserInfoMapper() {
        }

        @Override
        public IOidcUserInfo apply(OidcUserInfoAuthenticationContext oidcUserInfoAuthenticationContext) {
            OAuth2Authorization authorization = oidcUserInfoAuthenticationContext.getAuthorization();
            OidcIdToken idToken = authorization.getToken(OidcIdToken.class).getToken();
            OAuth2AccessToken accessToken = oidcUserInfoAuthenticationContext.getAccessToken();
            Map<String, Object> scopeRequestedClaims = getClaimsRequestedByScope(idToken.getClaims(), accessToken.getScopes());
            return new IOidcUserInfo(scopeRequestedClaims);
        }

        private static Map<String, Object> getClaimsRequestedByScope(Map<String, Object> claims, Set<String> requestedScopes) {
            HashSet<Object> scopeRequestedClaimNames = new HashSet<>(32);
            scopeRequestedClaimNames.add("sub");
            if (requestedScopes.contains("address")){
                scopeRequestedClaimNames.add("address");
            }

            if (requestedScopes.contains("email")){
                scopeRequestedClaimNames.add(EMAIL_CLAIMS);
            }

            if (requestedScopes.contains("phone")){
                scopeRequestedClaimNames.add(PHONE_CLAIMS);
            }

            if (requestedScopes.contains("profile")){
                scopeRequestedClaimNames.add(PROFILE_CLAIMS);
            }

            Map<String, Object> requestedClaims = new HashMap(claims);
            requestedClaims.keySet().removeIf((claimName) -> {
                return !scopeRequestedClaimNames.contains(claimName);
            });
            return requestedClaims;
        }
    }
}
