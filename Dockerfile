FROM dougefresh/society-base:php

MAINTAINER Douglas Chimento "dchimento@gmail.com"

ADD build/distributions/leagues.tar.gz /srv/web/leagues/
ADD build/libs/society-leagues.jar /srv/service/

ENTRYPOINT bash /srv/start.sh
