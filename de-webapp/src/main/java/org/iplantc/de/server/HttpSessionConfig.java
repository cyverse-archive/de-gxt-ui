package org.iplantc.de.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by sriram on 6/16/17.
 */

@EnableRedisHttpSession
@Configuration
public class HttpSessionConfig {

    @Autowired
    private Environment environment;

    private final Logger LOG = LoggerFactory.getLogger(HttpSessionConfig.class);

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(buildPoolConfig());
        factory.setHostName(getHostName());
        factory.setPort(getPort());
        factory.setDatabase(getDbNumber());
        factory.setUsePool(true);
        factory.setPassword(getPassword());
        return factory;
    }

    private String getHostName() {
        return environment.getProperty("org.iplantc.discoveryenvironment.redis.host");
    }

    private int getPort() {
        return Integer.parseInt(environment.getProperty("org.iplantc.discoveryenvironment.redis.port"));
    }

    private int getDbNumber() {
        return Integer.parseInt(environment.getProperty(
                "org.iplantc.discoveryenvironment.redis.db.number"));
    }

    private String getPassword() {
        return environment.getProperty("org.iplantc.discoveryenvironment.redis.password");
    }

    private JedisPoolConfig buildPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(128);
        return config;
    }

}

