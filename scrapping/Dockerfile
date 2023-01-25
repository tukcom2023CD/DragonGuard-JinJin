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
CMD [ "-t", "70", "db:3306", "--", "python3", "app.py" ]
