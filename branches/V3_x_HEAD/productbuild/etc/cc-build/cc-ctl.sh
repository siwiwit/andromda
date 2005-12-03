#!/bin/sh
# chkconfig: 2345 95 05
# description: CruiseControl build loop
#
# based on http://confluence.public.thoughtworks.org/display/CC/RunningCruiseControlFromUnixInit
# adapted for JMX access
#
# Cruise Control startup: Startup and kill script for Cruise Control
#

PATH=/sbin:/usr/sbin:/usr/bin:/bin
export PATH

CCUSER=${CC_USER}
CCDIR=/usr/local/cruisecontrol
CCSTARTSCRIPT=$CCDIR/main/cruisecontrol.sh
CCPORT=8989
CCJMXURL="http://localhost:${CCPORT}"
CCJMXUSER=$CC_JMX_USER
CCJMXPASS=$CC_JMX_PASS
CCLOG=/var/log/cruisecontrol

# Activate the following, if you want parallel builds:
#CCARGS="-p"
CCUPDATE_SCRIPT="cvs update"

if [ -f /etc/cruisecontrol.conf ]; then
  source /etc/cruisecontrol.conf
fi

# Get cruisecontrol status via JMX:
stat=`curl -s "$CCJMXURL/getattribute?objectname=CruiseControl%20Manager:id=unique&attribute=Projects&format=collection&template=identity" | perl -pe 's/>/>\n/g' | grep '<Element ' | cut -d '"' -f 2`

case "$1" in

    "start")
         # pass on the environment
         su $CCUSER -c "$CCSTARTSCRIPT $CCARGS 2>&1 > $CCLOG &" 
         sleep 3
         $0 status
         RETVAL=$?
    ;;

    "stop")
        if [ "$stat" ]
        then
          curl -s "$CCJMXURL/invoke?operation=halt&objectname=CruiseControl+Manager:id=unique"
          $0 status
          RETVAL=$?
        else
          echo "cruisecontrol is not running"
          RETVAL=1
        fi
      ;;

  'status')
    if [ "$stat" ]
    then
      echo cruisecontrol is running:
      echo "$stat"
      RETVAL=0
    else
      echo "cruisecontrol is stopped"
      RETVAL=1
    fi
    ;;

  'restart')
    $0 stop && $0 start
    RETVAL=$?
    ;;

  'update')
    cd $CCDIR && $CCUPDATE_SCRIPT
    RETVAL=$?
    ;;

  *)
    echo "Usage: $0 { start | stop | status | restart | update }"
    exit 1
esac
exit 0;
