# SQSClientWithJMS
SQS client to send messages to a SQS in AWS and listen to it asynchronously.

## Usage
### Creating instance
+ Create an instance of SQSClient class:
```
SQSClient sqsClient = new SQSClient("ACCESS_KEY", "SECRET_KEY", "QUEUE_NAME", Regions.SA_EAST_1);
```
### Sending messages
+ To send message, wrap your payload into a string and call the below method:
```
sqsClient.publishToQueue("Testing sending....");
```
### Listening asynchronously
+ To listen asynchronously the sqs, create a class implementing the MessageListener interface. It has only one method:
```
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
```
+ and then set an instance of your listener like the following:
```
sqsClient.setSQSListener(new MyListener());
```
+ Full usage:
```
public static void main(String[] args) {
        try {
            SQSClient sqsClient = new SQSClient("ACCESS_KEY", "SECRET_KEY",
                                                "QUEUE_NAME", Regions.SA_EAST_1);
              sqsClient.publishToQueue("Testing sending....");
              sqsClient.setSQSListener(new MyListener());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
```
