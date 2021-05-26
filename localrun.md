mvn dependency:tree

Maven : 
gcloud dataproc jobs submit spark \
	--cluster de-dproc-standard \
	--class com.kumargaurav.ReadBigqueryTableDemo \
	--region us-central1 \
	--project itd-aia-de \
	--jars "gs://itd-aia-de/gcs-cs-works/jars/BigQuerySpark3Scala12-1.0-SNAPSHOT.jar,gs://spark-lib/bigquery/spark-bigquery-with-dependencies_2.12-0.20.0.jar"
	

Sbt : 	
gcloud dataproc jobs submit spark \
	--cluster de-dproc-standard \
	--class com.kumargaurav.ReadBigqueryTableDemo \
	--region us-central1 \
	--project itd-aia-de \
	--jars "gs://itd-aia-de/gcs-cs-works/jars/BigQuerySpark3Scala12-assembly-0.1.jar,gs://spark-lib/bigquery/spark-bigquery-with-dependencies_2.12-0.20.0.jar"
	

gcloud dataproc jobs submit spark \
	--cluster datapipeline-dproc-ssot-tableau-etl-batch-export \
	--class com.kumargaurav.WriteBQTableWithPartitionDemo \
	--region us-central1 \
	--project itd-aia-de \
	--properties="spark.executor.cores=4" \
	--jars "gs://itd-aia-de/gcs-cs-works/jars/BigQuerySpark3Scala12-assembly-0.1.jar,gs://spark-lib/bigquery/spark-bigquery-latest_2.12.jar"
	
	
spark-submit \
--class com.kumargaurav.WriteBQTableWithPartitionDemo \
--jars /Users/gkumargaur/workspace/scala/SparkScala31Poc/spark-bigquery-latest_2.12.jar \
target/scala-2.12/BigQuerySpark3Scala12-assembly-0.1.jar	