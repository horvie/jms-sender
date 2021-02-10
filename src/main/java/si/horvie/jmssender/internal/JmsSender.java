package si.horvie.jmssender.internal;

import java.util.List;
import java.util.Optional;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

/**
 * Send text JMS message to specified JMS server
 */
public class JmsSender {
	private static final String PROTOCOL = "tcp://";

	private final String address;
	private final String port;
	private final String user;
	private final String password;

	/**
	 * @param address  of JMS server (ie. artemis.horvie.si)
	 * @param port     of JMS server (ie. 61616)
	 * @param user     of JMS server (ie. horvie)
	 * @param password of JMS server (ie. passwordForHorvie)
	 */
	public JmsSender(String address, String port, String user, String password) {
		this.address = prepareAddress(address);
		this.port = preparePort(port);
		this.user = user;
		this.password = password;
	}

	/**
	 * Send text message to queue. If problems occurred, respond with error message
	 * 
	 * @param queueName to send message to
	 * @param message   to send
	 * @param props     to set on message
	 * @return Error message if problems occurred, {@code empty} otherwise
	 */
	public Optional<String> send(String queueName, String message, String props) {
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(address + ":" + port)) {
			if (!user.isBlank()) {
				cf.setUser(user);
				cf.setPassword(password);
			}

			try (Connection connection = cf.createConnection()) {
				try (Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
					Queue queue = session.createQueue(queueName);
					try (MessageProducer producer = session.createProducer(queue)) {
						TextMessage textMessage = session.createTextMessage(message);

						List<Property> properties = new PropertyParser().parse(props);
						properties.forEach(p -> this.setProperty(textMessage, p));

						producer.send(textMessage);
					}
				}
			}
		} catch (Throwable e) {
			return Optional.of(e.getMessage() != null ? e.getMessage() : e.getClass().getName());
		}

		return Optional.empty();
	}

	private String prepareAddress(String address) {
		if (address.startsWith(PROTOCOL)) {
			return address;
		}
		return PROTOCOL + address;
	}

	private String preparePort(String port) {
		if (port.startsWith(":")) {
			return port.substring(1);
		}
		return port;
	}

	private void setProperty(TextMessage textMessage, Property p) {
		try {
			textMessage.setStringProperty(p.name, p.value);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
