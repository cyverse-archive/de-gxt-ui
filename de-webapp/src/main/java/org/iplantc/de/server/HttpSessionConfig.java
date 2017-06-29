package org.iplantc.de.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Configure Spring to use Redis for HTTP Session Management
 * <p>
 * Created by sriram on 6/16/17.
 * 
 */

@EnableRedisHttpSession
@Configuration
public class HttpSessionConfig {

    @Autowired
    private Environment environment;

    @Value("${org.iplantc.discoveryenvironment.redis.host}")
    String redisHost;
    @Value("${org.iplantc.discoveryenvironment.redis.port}")
    int redisPort;
    @Value("${org.iplantc.discoveryenvironment.redis.db.number}")
    int dbNumber;
    @Value("${org.iplantc.discoveryenvironment.redis.password}")
    String password;

    private final Logger LOG = LoggerFactory.getLogger(HttpSessionConfig.class);

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(buildPoolConfig());
        factory.setHostName(redisHost);
        factory.setPort(redisPort);
        factory.setDatabase(dbNumber);
        factory.setUsePool(true);
        factory.setPassword(password);
        return factory;
    }

    private JedisPoolConfig buildPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(128);
        return config;
    }

}

