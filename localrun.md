mvn dependency:tree

gcloud dataproc jobs submit spark \
	--cluster de-dproc-standard \
	--class com.kumargaurav.ReadBigqueryTableDemo \
	--region us-central1 \
	--project itd-aia-de \
	--jars "gs://itd-aia-de/gcs-cs-works/jars/BigQuerySpark3Scala12-1.0-SNAPSHOT.jar,gs://spark-lib/bigquery/spark-bigquery-with-dependencies_2.12-0.20.0.jar"
	
	
gcloud dataproc jobs submit spark \
	--cluster de-dproc-standard \
	--class com.kumargaurav.ReadBigqueryTableDemo \
	--region us-central1 \
	--project itd-aia-de \
	--jars "gs://itd-aia-de/gcs-cs-works/jars/BigQuerySpark3Scala12-assembly-0.1.jar,gs://spark-lib/bigquery/spark-bigquery-with-dependencies_2.12-0.20.0.jar"