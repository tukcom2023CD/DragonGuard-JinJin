resource "local_file" "setup_jenkins" {
  content         = <<EOF
        #!/bin/bash
        apt-get update
        apt-get install -y apt-transport-https ca-certificates curl software-properties-common
        curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
        add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
        apt-get update
        apt-get install -y docker-ce

        curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
        chmod +x /usr/local/bin/docker-compose

        curl -L "https://raw.githubusercontent.com/ohksj77/jenkins-docker/main/docker-compose.yml" -o /home/ubuntu/docker-compose.yml

        docker-compose -f /home/ubuntu/docker-compose.yml up -d
        EOF
  filename        = "setup-jenkins.sh"
  file_permission = "0755"
}

resource "aws_instance" "jenkins" {
  ami           = "ami-0f915581c862507fd"
  instance_type = "t2.medium"
  subnet_id     = aws_subnet.public.id

  user_data = local_file.setup_jenkins.content

  tags = {
    Name = "Jenkins"
  }
}


resource "aws_instance" "bastion" {
  ami           = "ami-0f915581c862507fd"
  instance_type = "t2.medium"
  subnet_id     = aws_subnet.public.id

  tags = {
    Name = "Bastion"
  }
}
