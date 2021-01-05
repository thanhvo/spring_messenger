# Spring Workers

The project demonstrates how we apply Reactive streams to handle data from SQS queues. We use Terraform to manage resources on AWS. The scripts create 50
SQS queues using specified credentials. The Spring Boot application then publishes and consumes the messages going through the queues and treats them as Reactive streams.

To deploy the project to AWS Lambda, we only need to specify the class LambdaHandler in Lambda Function setting.
The deployment process is as simple as one single line command:  
`mvn clean package` 