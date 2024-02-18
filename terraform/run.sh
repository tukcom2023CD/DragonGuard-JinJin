#!/bin/bash

# Docker (docker-desktop 등) 실행 상태에서 실행해주세요. 
# init, plan, apply, destroy 등을 인자로 넘겨주세요.
# 예시: sh run.sh init

docker run -i -t -v $(pwd):/app -w /app hashicorp/terraform:latest $1
