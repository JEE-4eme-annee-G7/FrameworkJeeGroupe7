package com.esgi.framework_JEE.jwt.security;



import com.esgi.framework_JEE.jwt.filter.CustomAuthenticationFilter;
import com.esgi.framework_JEE.jwt.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        http.cors().and().authorizeRequests().antMatchers("/resource").permitAll();
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        http.csrf().disable();
        http.authorizeRequests()

                /*
                    TOKEN LESS
                 */
                .antMatchers(
                        HttpMethod.POST,
                        "/login",
                        "/token/refresh",
                        "/user/create"
                ).permitAll()

                /*
                    GET WITH TOKEN
                 */
                .antMatchers(
                        HttpMethod.GET,
                        "/api/v1/basket/**",
                        "/api/v1/basket/user/**",
                        "/api/v1/basket/",
                        "/api/v1/invoice/user/**",
                        "/api/v1/invoice/**",
                        "/api/v1/invoice",
                        "/products/**",
                        "/products/",
                        "/product_category/**",
                        "/product_category/",
                        "/role/**",
                        "/role/name/**",
                        "/role/",
                        "/slot/**",
                        "/slot/",
                        "/slot/start",
                        "/products/visited",
                        "/user/**",
                        "/user/"
                ).hasAnyAuthority("ADMIN","USER")
                .antMatchers(
                        HttpMethod.GET,
                        "/**"
                ).hasAnyAuthority("ADMIN")

                /*
                    POST WITH TOKEN
                */

                .antMatchers(
                        HttpMethod.POST,
                "/api/v1/basket/generate/**",
                        "/api/v1/basket",
                        "/api/v1/invoice",
                        "/api/v1/invoice/generate/**",
                        "/slot/create",
                        "/products/**/visited"
                ).hasAnyAuthority("ADMIN","USER")

                .antMatchers(
                        HttpMethod.POST,
                        "/**",
                        "/role/create",
                        "/products/create",
                        "/product_category/create",
                        "/**"
                ).hasAnyAuthority("ADMIN")

                /*
                    PUT WITH TOKEN
                */
                .antMatchers(
                        HttpMethod.PUT,
                        "/user/email/**",
                        "/user/password/**"
                ).hasAnyAuthority("ADMIN","USER")

                .antMatchers(
                        HttpMethod.PUT,
                        "/user/email/**",
                        "/user/password/**",
                        "/products/**",
                        "/**"
                ).hasAnyAuthority("ADMIN")


                /*
                    PATCH WITH TOKEN
                */
                .antMatchers(
                        HttpMethod.PATCH,
                        "/user/email/**",
                        "/user/password/**",
                        "/user/firstname/**",
                        "/user/lastname/**",
                        "/slot/start/**",
                        "/slot/end/**"
                ).hasAnyAuthority("ADMIN","USER")

                .antMatchers(
                        HttpMethod.PATCH,
                        "/product_category/**",
                        "/role/**",
                        "/**"
                ).hasAnyAuthority("ADMIN")

                /*
                    DELETE WITH TOKEN
                */
                .antMatchers(
                        HttpMethod.DELETE,
                        "/user/**",
                        "/slot/**"
                ).hasAnyAuthority("ADMIN", "USER")

                .antMatchers(
                        HttpMethod.DELETE,
                        "/**",
                        "/api/v1/basket/**",
                        "/api/v1/invoice/**",
                        "/products/**",
                        "/product_category/**",
                        "/role/**",
                        "/**"
                ).hasAnyAuthority("ADMIN");

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

}
