FROM gradle:7.5.1-jdk17

WORKDIR /app

COPY . /app

EXPOSE 8090

RUN chmod +x start.sh && gradle clean build -x test --stacktrace

CMD ["sh", "start.sh"]