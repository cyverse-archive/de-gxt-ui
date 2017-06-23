package org.iplantc.de.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
/**
 * Created by sriram on 6/16/17.
 */

@EnableRedisHttpSession
@Configuration
public class HttpSessionConfig {

    @Autowired
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(getHostName());
        factory.setPort(getPort());
        factory.setUsePool(true);
        return factory;
    }

    private String getHostName() {
        return environment.getProperty("redis.host");
    }

    private int getPort() {
        return Integer.parseInt(environment.getProperty("redis.port"));
    }

}
