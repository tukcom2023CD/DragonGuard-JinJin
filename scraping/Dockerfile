FROM --platform=linux/amd64 python:3.8
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

RUN apt-get update && apt-get install firefox-esr -y

WORKDIR /scraping
RUN pip install --upgrade pip
COPY requirements.txt /scraping/
RUN pip3 install -r requirements.txt
COPY . ./

COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

ENTRYPOINT [ "./wait-for-it.sh" ]
CMD [ "-t", "200", "gitrank-backend:8080", "--", "faust", "-A", "app", "worker", "-l", "info" ]
