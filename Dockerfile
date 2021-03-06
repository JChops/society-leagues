FROM dougefresh/society-base

MAINTAINER Douglas Chimento "dchimento@gmail.com"

ADD . /data/src

RUN  mkdir -p /srv/service /data/web/leagues && cd /data/src && ./gradlew clean build && mv build/libs/society-leagues.jar /srv/service/ && tar zxf build/distributions/leagues.tar.gz -C /data/web/leagues/ && cp contrib/linux/docker-entrypoint.sh /srv/start.sh

EXPOSE 80

ENTRYPOINT bash /srv/start.sh
