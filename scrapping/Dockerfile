FROM python:3.8
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

WORKDIR /scrapping
RUN pip install --upgrade pip
ENV FLASK_APP=app:app
COPY requirements.txt /scrapping/
RUN pip install -r requirements.txt
COPY . ./

ENTRYPOINT [ "gunicorn" ]
CMD [ "--bind", "0.0.0.0:5000", "app:app", "--reload" ]
