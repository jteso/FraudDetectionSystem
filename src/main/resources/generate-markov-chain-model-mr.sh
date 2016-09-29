Run this script

JAR_NAME=/Users/javierteso/Dev/hadoopLabs/target/hadoopLabs-1.0.jar
CLASS_NAME=org.jtedilla.mr.markov.MarkovStateTransitionModel

echo "running mr"
IN_PATH=/Users/javierteso/Dev/hdfs/mmfr/output
OUT_PATH=/Users/javierteso/Dev/hdfs/mmfr/model
echo "input $IN_PATH output $OUT_PATH"
hadoop fs -rmr $OUT_PATH
echo "removed output dir"

hadoop jar $JAR_NAME $CLASS_NAME -Dconf.path=/Users/javierteso/Dev/hadoopLabs/src/main/resources/mmfr.properties $IN_PATH $OUT_PATH