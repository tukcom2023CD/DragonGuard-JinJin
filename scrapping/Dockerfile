FROM python:3.8
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

WORKDIR /scrapping
RUN pip install --upgrade pip
ENV FLASK_APP=app:app
COPY requirements.txt /scrapping/
RUN pip install -r requirements.txt
COPY . ./

ENTRYPOINT [ "./wait-for-it.sh" ]
CMD [ "-t", "50", "kafka:9092", "--", "gunicorn", "--bind", "0.0.0.0:5000", "app:app", "--timeout", "70" , "--reload" ]
