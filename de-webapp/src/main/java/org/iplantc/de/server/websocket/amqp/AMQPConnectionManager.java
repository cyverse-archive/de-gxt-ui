package org.iplantc.de.server.websocket.amqp;

import org.iplantc.de.server.websocket.util.PropertiesUtil;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

/**
 * Created by sriram on 4/7/16.
 * @author <a href="mailto:sriram@cyverse.org">Sriram Srinivasan</a>
 * @author <a href="mailto:jstroot@cyverse.org">Jonathan Strootman</a>
 */
public class AMQPConnectionManager {

    public static AMQPConnectionManager instance;

    private final Logger LOG = LoggerFactory.getLogger(AMQPConnectionManager.class);

    private String amqpUri;

    Properties deProperties = PropertiesUtil.getDEProperties();
    private Connection connection;

    private AMQPConnectionManager() {
        amqpUri = deProperties.getProperty("org.iplantc.discoveryenvironment.notification.amqp.uri");
        createConnection();
    }

    private void createConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            factory.setUri(amqpUri);
            connection = factory.newConnection();
            LOG.info("amqp connection created!");
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("IOEException when creating amqp connection!" + e.getMessage());
        } catch (TimeoutException e) {
            e.printStackTrace();
            LOG.error("Timeout Exception when creating amqp connection!" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Exception when creating AMQP connection!" + e.getMessage());
        }
    }

    public static AMQPConnectionManager getInstance() {
        if(instance == null) {
            instance = new AMQPConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection == null) {
            createConnection();
        }
        return connection;
    }
}
