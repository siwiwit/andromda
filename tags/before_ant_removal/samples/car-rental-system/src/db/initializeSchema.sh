#! /bin/sh

java -classpath $JBOSS_HOME/server/default/lib/hsqldb.jar org.hsqldb.util.ScriptTool -driver org.hsqldb.jdbcDriver -url jdbc:hsqldb:hsql: -database //localhost:1701 -script ../../build/hibernate-schema-initialize.sql
