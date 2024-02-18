resource "kubernetes_namespace" "argocd" {
  metadata {
    name = "argocd"
  }
}

resource "helm_release" "argocd" {
  name       = "argocd"
  repository = "https://argoproj.github.io/argo-helm"
  chart      = "argo-cd"
  namespace  = "argocd"
  version    = "5.14.2"

  set {
    name  = "server.service.type"
    value = "LoadBalancer"
  }
}

resource "argocd_application" "app" {
  metadata {
    name      = "gitrank"
    namespace = kubernetes_namespace.argocd.metadata[0].name
  }

  spec {
    project = "default"

    source {
      repo_url        = "https://github.com/tukcom2023CD/DragonGuard-JinJin.git"
      target_revision = "HEAD"
      path            = "k8s/"
    }

    destination {
      server    = "https://kubernetes.default.svc"
      namespace = "default"
    }
  }
}
