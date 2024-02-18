module "eks" {
  source  = "terraform-aws-modules/eks/aws"

  cluster_name    = "gitrank-cluster"
  cluster_version = "1.24"
  vpc_id          = aws_vpc.main.id
  subnet_ids      = aws_subnet.private.id

  eks_managed_node_groups = {
    default_node_group = {
      min_size     = 2
      max_size     = 5
      desired_size = 2
      instance_types = ["t3.medium", "t3.large"]
    }
  }

  tags = {
    Environment = "prod"
  }
}
