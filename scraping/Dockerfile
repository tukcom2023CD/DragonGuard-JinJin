FROM --platform=linux/amd64 python:3.8
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

RUN apt-get update && apt-get install firefox-esr -y

WORKDIR /scraping
RUN pip install --upgrade pip
ENV FLASK_APP=app:app
COPY requirements.txt /scraping/
RUN pip3 install -r requirements.txt
COPY . ./

COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

ENTRYPOINT [ "./wait-for-it.sh" ]
CMD [ "-t", "50", "kafka:9092", "--", "gunicorn", "--bind", "0.0.0.0:5000", "app:app", "--timeout", "70" , "--reload" ]
