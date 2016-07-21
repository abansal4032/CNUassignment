FROM python:2.7
VOLUME /tmp
COPY requirements.txt /
RUN pip install -r requirements.txt
ADD . /
CMD python manage.py runserver 0.0.0.0:8080
