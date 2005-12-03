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

dirname=`dirname $0`
basename=`basename $0`
cd $dirname
dirname=`pwd`
CCCTL=$dirname/$basename

CCUSER=${CC_USER}
CCDIR=/usr/local/cruisecontrol
CCSCRIPT=main/bin/cruisecontrol.sh
CCPORT=8989
CCJMXURL="http://localhost:${CCPORT}"
CCJMXUSER=$CC_JMX_USER
CCJMXPASS=$CC_JMX_PASS
CCLOG=/var/log/cruisecontrol
CCWORKDIR=/var/andromda-build
CCCONFIG=${CCWORKDIR}/andromda-all/etc/cc-build/${CC_CONFIG_FILE}

# Activate the following, if you want parallel builds:
#CCARGS="-p"
CCUPDATE_SCRIPT="cvs update"

if [ -f /etc/cruisecontrol.conf ]; then
  echo "Using /etc/cruisecontrol.conf settings"
  source /etc/cruisecontrol.conf
fi
CCSTARTSCRIPT="$CCDIR/$CCSCRIPT"

if [ ! -f ${CCSTARTSCRIPT} ]; then
  echo "${CCSTARTSCRIPT} not found"
  exit
fi

if [ -z ${CCPORT} ]; then
  CCJMXPARAMS=
else
  CCJMXPARAMS="-port ${CCPORT} -user ${CCJMXUSER} -password ${CCJMXPASS}"
fi

CC_CALLER=`whoami`
if [ ${CC_CALLER} = ${CCUSER} ];then
  RUNIT=
else
 RUNIT="su $CCUSER -c"
fi
 
# Get cruisecontrol status via JMX:
stat=`curl -s "$CCJMXURL/getattribute?objectname=CruiseControl%20Manager:id=unique&attribute=Projects&format=collection&template=identity" | perl -pe 's/>/>\n/g' | grep '<Element ' | cut -d '"' -f 2`

case "$1" in

    "start")
         # pass on the environment
         echo "Starting cruisecontrol"
         echo "User .......: ${CCUSER}"
         echo "Start script: ${CCSTARTSCRIPT}"
         echo "Log ........: ${CCLOG}"
         echo "Work dir ...: ${CCWORKDIR}"
         cd ${CCWORKDIR}
         if [ -z $RUNIT ]; then
           eval "nohup $CCSTARTSCRIPT $CCARGS $CCJMXPARAMS -configfile ${CCCONFIG} 2>&1 > $CCLOG &"
         else
           $RUNIT "nohup $CCSTARTSCRIPT $CCARGS $CCJMXPARAMS -configfile ${CCCONFIG} 2>&1 > $CCLOG &" 
         fi
         sleep 3
         $CCCTL status
         RETVAL=$?
    ;;

    "stop")
        if [ "$stat" ]
        then
          curl -s "$CCJMXURL/invoke?operation=halt&objectname=CruiseControl+Manager:id=unique"
          $CCCTL status
          RETVAL=$?
        else
          echo "cruisecontrol is not running"
          RETVAL=1
        fi
      ;;

  'tail')
      tail -f ${CCLOG}
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
    $CCCTL stop && $CCCTL start
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
