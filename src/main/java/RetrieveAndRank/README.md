# RetrieveAndRank

This is sample retrieve and rank..

Stage 1: Get your service credentials
Before you can work with a service in Bluemix, you need service credentials. 

Stage 2: Create a cluster
To use the Retrieve and Rank service, you must create a Solr cluster. A Solr cluster manages your search collections, that we will create later.
Stage 3: Create a collection and add documents
A Solr collection is a logical index of the data in your documents. A collection is a way to keep data separate in the cloud. In this stage, you create a collection, associate it with a configuration, and upload and index your documents.
Stage 4: Create and train the ranker
To return the most relevant documents at the top of your results, the Retrieve and Rank services uses a machine learning component called a ranker. You send queries to the trained ranker.The ranker learns from examples before it can rerank results from queries that it hasn't seen before. Collectively, the examples are referred to as "ground truth."
Stage 5: Retrieve some answers
While you're waiting for the ranker to finish training, you can search your documents. This search, which uses the "Retrieve" part of the Retrieve and Rank service, does not use the machine learning ranking features. It's a standard Solr search query.
Stage 6: Rerank the results
Check the status of the ranker until you see a status of Available. With this sample data, training takes about 5 minutes and started when the script finished in Stage 4.
Query the ranker to review the reranked results, now that the ranker is trained.
Here is an example call. It's a similar query as in Stage 5, but the select parameter is changed to fcselect. Using fcselect runs the request against the trained ranker instead of against Solr as in Stage 5. When you call fcselect, you must specify the ID of the trained ranker against which to run the request.
The query returns your reranked search results in JSON format. You can compare these results against the results you got with the simple search in Stage 5.
After evaluating the reranked search results, you can refine them by repeating Stages 4, 5, and 6. You can also add new documents, as described in Stage 3, to broaden the scope of the search. Repeat the process until you are completely satisfied with the results. This can require multiple iterations of refining and reranking.
Stage 7: Clean up
You might want to delete the Solr components and ranker that you created in this tutorial. To clean up, you delete the cluster that you created in Stage 2 and the ranker that you created in Stage 4.
