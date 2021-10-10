package com.calzproject.toDoList.security;

import com.calzproject.toDoList.ToDoListApplication;
import com.calzproject.toDoList.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.calzproject.toDoList.security.SecurityConstants.H2_URL;
import static com.calzproject.toDoList.security.SecurityConstants.SIGN_UP_URLS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger LOGGER=LoggerFactory.getLogger(ToDoListApplication.class);

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizeHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        LOGGER.info(">>>>>>>>>>>>>>>> AuthenticationManagerBuilder");
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)//FOR AUTHENTICATION
    protected AuthenticationManager authenticationManager() throws Exception {
        LOGGER.info(">>>>>>>>>>>>>>>> authenticationManager");
        return super.authenticationManager();
    }
    // web security/acceess control (config) for AUTHORIZATION
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        Basic authentication filter
        LOGGER.info(">>>>>>>>>>>>>>>> HTTPSecure ");
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizeHandler).and()//part of ExceptionTranslationFilter, to access following resource you must authenticate first
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().sameOrigin()// To enable H2 database
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers(SIGN_UP_URLS)
                .permitAll()
                .antMatchers(H2_URL)
                .permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);//do UsernamePasswordAuthenticationFilter, then do jwtAuthenticationFilter
    }
//    GET TOKEN
//configure(AuthenticationManagerBuilder authenticationManagerBuilder), AuthenticationManager authenticationManager() throws Exception, configure(HttpSecurity http) throws Exception
//those 3 methods running background starting from initialization
// UserController (API login(LoginRequest)-> authentication)-> CustomUserDetailsService (user == null -> throw exception) -> UserController(authentication) -> Fail -> JwtAuthenticationEntryPoint(InvalidLoginResponse)
//                                          configure(AuthenticationManagerBuilder authenticationManagerBuilder)                                           configure(HttpSecurity http) throws Exception


// UserController (API login(LoginRequest)-> authentication)-> CustomUserDetailsService (user != null) ->           UserController(authentication) -> Success -> JwtTokenProvider(generateToken) -> JWTLoginSuccessResponse
//                                 configure(AuthenticationManagerBuilder authenticationManagerBuilder)


//      USE TOKEN
// http.addFilterBefore(jwtAuthenticationFilter()) -> jwtAuthenticationFilter().doFilterInternal ->customUserDetailsService.loadUserById(userId) -> ProjectController(API call)
}
