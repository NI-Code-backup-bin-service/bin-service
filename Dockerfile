######################## STAGE 1 ###########################
FROM nidockerhubgeneral.azurecr.io/eclipse-temurin:8u392-b08-jdk-jammy-v1 as builder

RUN \
    apt update \
    && apt -y install git \
    && apt clean \
    && apt -y autoremove

ARG VERSION=unspecified
ARG GRADLE_TASKS='clean build'

WORKDIR /build
COPY . .
COPY .git .git

RUN \
    echo Building version $VERSION \
    && ./gradlew --no-daemon -P version=$VERSION $GRADLE_TASKS
RUN cp ./bin-service-app/build/libs/*.jar ./bin-service.jar

######################## STAGE 2 ###########################
FROM nidockerhubgeneral.azurecr.io/eclipse-temurin:8u392-b08-jre-jammy-v1

WORKDIR /opt/app

COPY --from=builder /build/bin-service.jar bin-service.jar
COPY --from=builder /build/run.sh run.sh

RUN chmod +x run.sh
USER 1000

ENTRYPOINT ["./run.sh"]
CMD [""]