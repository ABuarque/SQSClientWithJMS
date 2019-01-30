package com.abuarquemf.sqswithjms;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import javax.jms.*;

public class SQSClient {

    private String accessKeyId;
    private String secretAccessKey;
    private SQSConnection sqsConnection;
    private Session session;
    private Queue queue;
    private String queueName;

    /**
     *
     * It creates by getting the access key secret key,
     * sqs name and setting the region.
     *
     *  Ex: SQSClient sqsClient = new SQSClient("ACCESS_KEY", "SECRET_KEY", "MY-SQS", Regions.SA_EAST_1);
     *
     * @param accessKeyId
     * @param secretAccessKey
     * @param queueName
     * @param regions
     * @throws JMSException
     */
    public SQSClient(String accessKeyId, String secretAccessKey,
                     String queueName, Regions regions) throws JMSException {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.queueName = queueName;
        final AWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        sqsConnection = new SQSConnectionFactory.Builder(Region.getRegion(regions))
                .build()
                .createConnection(awsCredentials);
        sqsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        session = sqsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        queue = session.createQueue(this.queueName);
    }

    public void publishToQueue(String payload) throws JMSException {
        try {
            MessageProducer producer = session.createProducer(queue);
            TextMessage textMessage = session.createTextMessage(payload);
            producer.send(textMessage);
            sqsConnection.close();
        } catch (JMSException e) {
            try {
                sqsConnection.close();
            } catch (JMSException e1) {
                throw e1;
            }
        }
    }

    public void setSQSListener(MessageListener messageListener) throws JMSException {
        try {
            MessageConsumer messageConsumer =  session.createConsumer(queue);
            messageConsumer.setMessageListener(messageListener);
            sqsConnection.start();
        } catch (JMSException e) {
            try {
                sqsConnection.close();
            } catch (JMSException e1) {
                throw e1;
            }
        }
    }
}

