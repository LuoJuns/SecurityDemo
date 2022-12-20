package com.securitydemo.config;

import com.securitydemo.service.MyUserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final private MyUserDetailService userDetailsService;

    final private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(getEncoder());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public PasswordEncoder getEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //退出登录映射
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/test/hello").permitAll();
        //权限不足映射
        http.exceptionHandling().accessDeniedPage("/unauth.html");
        //登录映射-接口映射
        http.formLogin()
                .loginPage("/login.html")//登录页面
                .loginProcessingUrl("/user/login")//登录表单接口
                .defaultSuccessUrl("/success.html").permitAll()//登录成功映射
                .and().authorizeHttpRequests()
                .antMatchers("/","/test/hello","/user/login").permitAll()//指定接口放行
                //.antMatchers("/test/hello").hasAuthority("admin")//指定某一权限放行
                //.antMatchers("/test/hello").hasAnyAuthority("admin","guest")//指定任意权限放行
                //.antMatchers("/test/hello").hasRole("employee")//指定某一角色放行
                .antMatchers("/test/index").hasAnyRole("boss","employee")//指定任意角色放行
                .anyRequest().authenticated()
                .and().rememberMe().tokenRepository(persistentTokenRepository())//自动登录需要的token数据库配置
                .tokenValiditySeconds(60)//token失效时间
                .userDetailsService(userDetailsService);//需要操作user表，配置userService
                //.and().csrf().disable();//跨域请求伪造，默认开启，防护put，post，delete接口
    }
}
