terraform {
  required_providers {
    argocd = {
      source  = "registry.terraform.io/oboukili/argocd"
    }
  }
}

provider "aws" {
  region = "ap-northeast-2"
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}
