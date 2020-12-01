provider "aws" {
  region  = "eu-central-1"
  alias   = "sc-dev"
  profile = "default"
  access_key = "AKIAZUXNQEWMJMZZ3HPO"
  secret_key = "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"
  assume_role {
    role_arn = "arn:aws:iam::476125643079:role/OrganizationAccountAccessRole"
  }
}
