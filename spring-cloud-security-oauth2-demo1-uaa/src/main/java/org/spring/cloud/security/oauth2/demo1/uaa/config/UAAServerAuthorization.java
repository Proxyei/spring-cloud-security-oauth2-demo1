package org.spring.cloud.security.oauth2.demo1.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @Author future
 * @DateTime 2019/11/14 21:33
 * @Description 1，配置客户端详情
 * 2，配置令牌服务
 * 3，配置端点
 * 4，配置oauth2的url权限
 */
@Configuration
@EnableAuthorizationServer
public class UAAServerAuthorization extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService detailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private CustomUserDetailService userDetailService;

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

//    @Bean
//    public ClientDetailsService clientDetailsService() {
//        return new InMemoryClientDetailsService();
//    }


    /**
     * @Description 1，配置合法的客户端信息
     * @Author future
     * @DateTime 2019/11/14 21:46
     **/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("xy-client1")
                .secret(new BCryptPasswordEncoder().encode("xy-password1"))
                .resourceIds("/user/list")
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                .scopes("all")
                .autoApprove(false)
                .redirectUris("http://www.baidu.com");
    }

    /**
     * @Description 2，设置token的格式与存储有效期
     * @Author future
     * @DateTime 2019/11/14 22:04
     **/
    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(detailsService);
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(2 * 3600);
        tokenServices.setRefreshTokenValiditySeconds(3 * 24 * 3600);
        return tokenServices;
    }

    /**
     * @Description 3，配置令牌端点也就是URL
     * @Author future
     * @DateTime 2019/11/15 0:13
     **/
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.authenticationManager(authenticationManager);
        endpoints.userDetailsService(userDetailService);
        endpoints.authorizationCodeServices(authorizationCodeServices);
        endpoints.tokenServices(authorizationServerTokenServices());
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.POST);

    }

    /**
     * @Description 4，配置oauth的URL权限控制
     * @Author future
     * @DateTime 2019/11/14 23:30
     **/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")//oauth/token_key公开
                .checkTokenAccess("permitAll()")//oauth/check_token公开
                .allowFormAuthenticationForClients();//运行表单认证申请令牌
    }


}
