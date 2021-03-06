resource "aws_sqs_queue" "queue" {
  count = var.queue_amount
  name                       = "spring-worker-${count.index + 1}"
  delay_seconds              = 0
  max_message_size           = 262144
  message_retention_seconds  = 86400
  receive_wait_time_seconds  = 20
  visibility_timeout_seconds = 60
  policy = <<POLICY
           {
             "Version": "2012-10-17",
             "Id": "sqspolicy",
             "Statement": [
               {
                 "Sid": "First",
                 "Effect": "Allow",
                 "Principal": "*",
                 "Action": "sqs:*",
                 "Resource": "arn:aws:sqs:eu-central-1:476125643079:spring-worker-${count.index + 1}"
               }
             ]
           }
           POLICY
}
