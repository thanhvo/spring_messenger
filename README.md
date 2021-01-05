# Spring Workers

This project is to demonstrate how we use Reactive streams to handle data from SQS queues.

To deploy the project to AWS Lambda, we only need to specify the class LambdaHandler in Lambda Function setting.
The deployment process is as simple as one line command:  
`mvn clean package` 