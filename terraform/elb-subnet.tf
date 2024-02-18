resource "aws_elb" "gitrank_elb" {
  subnets = [aws_subnet.public.id]

  listener {
    instance_port     = 80
    instance_protocol = "http"
    lb_port           = 80
    lb_protocol       = "http"
  }
}

resource "aws_network_interface" "gitrank_network_interface" {
  subnet_id   = aws_subnet.private.id
}
