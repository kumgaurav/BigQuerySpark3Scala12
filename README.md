# BigQuerySpark3Scala12
A sample demo to check latest spark 3.1.1, bigquery connector(0.20.0) and scala 2.12

Get the latest spark-bigquery-latest_2.12.jar from gs://spark-lib/bigquery/spark-bigquery-latest_2.12.jar
## To run using Maven
mvn clean package
1. To run ReadBigqueryTableDemo use following command
spark-submit \
--class com.kumargaurav.ReadBigqueryTableDemo \
--jars /Users/gkumargaur/workspace/scala/SparkScala31Poc/spark-bigquery-latest_2.12.jar \
target/BigQuerySpark3Scala12-1.0-SNAPSHOT.jar 

## To run using SBT
sbt clean assembly
1. To run ReadBigqueryTableDemo use following command
spark-submit \
--class com.kumargaurav.ReadBigqueryTableDemo  \
--jars /Users/gkumargaur/workspace/scala/SparkScala31Poc/spark-bigquery-latest_2.12.jar \ 
target/scala-2.12/BigQuerySpark3Scala12-assembly-0.1.jar 





bigquery+scala+spark3+github
