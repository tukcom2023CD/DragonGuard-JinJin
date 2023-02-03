FROM python:3.8
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

WORKDIR /usr/src
RUN apt-get -y update
RUN apt install wget
RUN apt install unzip  
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN apt -y install ./google-chrome-stable_current_amd64.deb
RUN wget -O /tmp/chromedriver.zip http://chromedriver.storage.googleapis.com/` curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE`/chromedriver_linux64.zip
RUN mkdir chrome
RUN unzip /tmp/chromedriver.zip chromedriver -d /usr/src/chrome

WORKDIR /scrapping
RUN pip install --upgrade pip
ENV FLASK_APP=app:app
COPY requirements.txt /scrapping/
RUN pip3 install -r requirements.txt
COPY . ./

ENTRYPOINT [ "./wait-for-it.sh" ]
CMD [ "-t", "50", "kafka:9092", "--", "gunicorn", "--bind", "0.0.0.0:5000", "app:app", "--timeout", "70" , "--reload" ]
