module "queue" {
  source = "./modules/queue"

  queue_amount = 50

  providers = {
    aws = aws.sc-dev
  }
}
