package com.abuarquemf.sqswithjms;

import com.amazonaws.regions.Regions;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

class MyListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println(">>> " + ((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

public class SqswithjmsApplication {

    public static void main(String[] args) {
        try {
            SQSClient sqsClient = new SQSClient("ACCESS_KEY", "SECRET_KEY",
                                                "QUEUE_NAME", Regions.SA_EAST_1);
              sqsClient.publishToQueue("Testing sending....");
            //sqsClient.setSQSListener(new MyListener());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}

