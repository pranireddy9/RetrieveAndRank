package solr;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.RetrieveAndRank;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.Ranker;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.Ranker.Status;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.Rankers;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrCluster;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrClusterList;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrClusterOptions;

public class CerebriRetrieveAndRank {
	
	private static final String username = "3c4c8888-5e2c-4ebf-a35a-b33abbe3fa0f";
	private static final String password = "VGlLv42xF6Ba";
	private static final String clusterId = "sc314b618d_cf6e_446b_983a_bad2659b8847";
	private static final String configName = "cranfield_solr_config";
	private static final String collection = "cerebri_collection_new";
	private static final String configzip =  "cranfield_solr_config.zip";
	private static final String rankercsv =  "cranfield_gt.csv";
	private static final String cranfielddatajson =  "cranfield_data.json";
	
	
	public static void main(String[] args) throws IOException, SolrServerException {
		// TODO Auto-generated method stub
		
		RetrieveAndRank service = new RetrieveAndRank();
		service.setUsernameAndPassword(username,password);
		SolrClient solrClient = getSolrClient(service.getSolrUrl(clusterId),username,password);
		//createSolrCluster(service);
		//getSolrClusters(service);
		//getSolrCluster(service);
		//uploadSolrClusterConfigurationZip(service);
		//getSolrClusterConfigurations(service);
		//getCofiguration(service);
		//createCollectionRequest(service, solrClient);
		//getDocumentBySolrQuery(solrClient);
		//cleanUp(service,solrClient);
		//createRanker(service);
		//getRankers(service);
		getRankerStatus(service,"3b140ax15-rank-2752");
		
	}
	
	/* create solr cluster*/

	public static void createSolrCluster(RetrieveAndRank service){
		SolrClusterOptions options = new SolrClusterOptions("Cerebri cluster", 1);
		SolrCluster cluster = service.createSolrCluster(options);
		System.out.println("SolrCluster: " + cluster);

		// wait until the cluster is available
		while (cluster.getStatus() == com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrCluster.Status.NOT_AVAILABLE) {
		   try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // sleep 10 seconds
		   cluster = service.getSolrCluster(cluster.getId());
		   System.out.println("SolrCluster status: " + cluster.getStatus());
		   System.out.println("SolrCluster ID: " + cluster.getId());
		   }
		
	}
	/* List Solr clusters*/
	public static void getSolrClusters(RetrieveAndRank service){
	SolrClusterList clusters = service.getSolrClusters();
	System.out.println(clusters);
	}
	
	/*Get information about a Solr cluster*/
	
	public static void getSolrCluster(RetrieveAndRank service){
		System.out.println(service.getSolrCluster(clusterId));
		
	}
	
	/*Delete Solr cluster*/
	 public static void deleteCluster(RetrieveAndRank  service ){
			service.deleteSolrCluster(clusterId);
		}
	 /*Upload Solr configuration*/
	 public static void uploadSolrClusterConfigurationZip(RetrieveAndRank service){
			URL url = CerebriRetrieveAndRank.class.getClassLoader().getResource(configzip);
			File configZip=null;
			try {
				configZip = new File(url.toURI());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			service.uploadSolrClusterConfigurationZip(clusterId, configName, configZip);
			
	 }
	 /*List Solr configurations*/
	 public static void getSolrClusterConfigurations(RetrieveAndRank service){
		 System.out.println(service.getSolrClusterConfigurations(clusterId));
		 
	 }
	 
	 /*Get configuration*/

	 public static void getCofiguration(RetrieveAndRank service) throws IOException{
		 InputStream inputStream = service.getSolrClusterConfiguration(clusterId, configName);
		 
		  OutputStream os = new FileOutputStream("src/main/resources/targetFile.zip");
          
          byte[] buffer = new byte[1024];
          int bytesRead;
          //read from is to buffer
          while((bytesRead = inputStream.read(buffer)) !=-1){
              os.write(buffer, 0, bytesRead);
          }
          inputStream.close();
          //flush OutputStream to write any buffered data to file
          os.flush();
          os.close();
	 }
	 
	 /*Delete configuration*/
	 
	 public static void deleteSolrClusterConfiguration(RetrieveAndRank service){
		 service.deleteSolrClusterConfiguration(clusterId, configName);
	        System.out.println("Configuration deleted.");
	 }
	 /*Create Solr collection*/
	 
	 public static void createCollectionRequest(RetrieveAndRank service,SolrClient solrClient){
			SolrCluster cluster = service.getSolrCluster(clusterId);
		CollectionAdminRequest.Create createCollectionRequest =
		        new CollectionAdminRequest.Create();
		createCollectionRequest.setCollectionName(collection);
		createCollectionRequest.setConfigName(configName);
		System.out.println("Creating collection...");
		CollectionAdminResponse response = null;
		try {
			response = createCollectionRequest.process(solrClient);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    if (!response.isSuccess()) {
		      System.out.println(response.getErrorMessages());
		      throw new IllegalStateException("Failed to create collection: "
		          + response.getErrorMessages().toString());
		    }
		System.out.println("Collection created.");
		System.out.println(response);
	}
	 /*Delete Solr collection*/

	private static void cleanUp(RetrieveAndRank service, SolrClient solrClient) {
		try {
			cleanupResources(solrClient,service);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deleteCluster(service);
	}
	 
	 
	 /*Index documents*/
	 
	 public static void solrIndexDocument(RetrieveAndRank service){
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", 1);
			document.addField("author", "pranitha,d.");
			document.addField("bibliography", "j. ae. scs. 25, 1958, 324.");
			document.addField("body", "experimental investigation of the aerodynamics of a wing in a slipstream .   an experimental study of a wing in a propeller slipstream was made in order to determine the spanwise distribution of the lift increase due to slipstream at different angles of attack of the wing and at different free stream to slipstream velocity ratios .  the results were intended in part as an evaluation basis for different theoretical treatments of this problem .   the comparative span loading curves, together with supporting evidence, showed that a substantial part of the lift increment produced by the slipstream was due to a /destalling/ or boundary-layer-control effect .  the integrated remaining lift increment, after subtracting this destalling lift, was found to agree well with a potential flow theory .   an empirical evaluation of the destalling effects was made for the specific configuration of the experiment .");
			document.addField("title", "experimental investigation of the aerodynamics of a wing in a slipstream .");
			System.out.println("Indexing document...");
			
			//Future code to be like this ..where we have to bring the or parse the cranfield json doc with the json parser and get all the documents from the json file and create a array list where we can add the documents and pass that array list to add method of solr client.
			
			// first get the json file ..
			//after you parse the json file..
			//create a new array list and add each document from the above file to the arraylist
			//once array list is done with the al additions then pass it to the add method solr client
			
			List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
			
			//parse json file
			
			docs.add(document);
			SolrClient client = getSolrClient(service.getSolrUrl(clusterId), username, password);
			
		UpdateResponse addResponse = null;
		try {
			addResponse = client.add("example_collection", docs);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println(addResponse);
			try {
				UpdateResponse response = client.deleteById(collection, 1);
			} catch (SolrServerException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			


			// Commit the document to the index so that it will be available for searching.
			try {
				client.commit("example_collection");
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Indexed and committed document.");
		}

	private static void getDocumentBySolrQuery(SolrClient client) {
		SolrQuery query = new SolrQuery("*:*");
		QueryResponse response1 = null;
		try {
			response1 = client.query(collection, query);
		} catch (SolrServerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(response1);
	}
	 /*Search Solr standard query parser*/
	public static void queryParser(RetrieveAndRank service){
		
		HttpSolrClient solrClient = getSolrClient(service.getSolrUrl(clusterId), username, password);
		solrClient = getSolrClient(service.getSolrUrl("scfaaf8903_02c1_4297_84c6_76b79537d849"), "{username}","{password}");
		SolrQuery query = new SolrQuery("*:*");
		QueryResponse response = null;
		try {
			response = solrClient.query("example_collection", query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(response);
	} 
	
	/*Create ranker*/
	public static void createRanker(RetrieveAndRank service){
		URL url = CerebriRetrieveAndRank.class.getClassLoader().getResource(rankercsv);
		File rankerCsv=null;
		try {
			rankerCsv = new File(url.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	Ranker ranker = service.createRanker("ranker2", rankerCsv);
	System.out.println(ranker);
	}
	
	/*List rankers*/
	public static void getRankers(RetrieveAndRank service){
		Rankers rankers = service.getRankers();
		System.out.println(rankers);
	}
	
	/*Get information about a ranker*/
	
	public static void getRankerStatus(RetrieveAndRank service , String rankerId){
		Ranker ranker = service.getRankerStatus(rankerId);
		System.out.println(ranker);
	
		 while (ranker.getStatus() == Status.TRAINING) {
			   try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // sleep 4 seconds
			  ranker = service.getRankerStatus(ranker.getId());
			   System.out.println("ranker status: " + ranker.getStatus());
			 }
	}
	
	/*Delete ranker*/
	
	//not yet ready
	
	private static HttpSolrClient getSolrClient(String uri, String username, String password) {
	    return new HttpSolrClient(uri, createHttpClient(uri, username, password));
	  }
	
	private static HttpClient createHttpClient(String uri, String username, String password) {
	    final URI scopeUri = URI.create(uri);

	    final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	    credentialsProvider.setCredentials(new AuthScope(scopeUri.getHost(), scopeUri.getPort()),
	        new UsernamePasswordCredentials(username, password));

	    final HttpClientBuilder builder = HttpClientBuilder.create()
	        .setMaxConnTotal(128)
	        .setMaxConnPerRoute(32)
	        .setDefaultRequestConfig(RequestConfig.copy(RequestConfig.DEFAULT).setRedirectsEnabled(true).build())
	        .setDefaultCredentialsProvider(credentialsProvider)
	        .addInterceptorFirst(new PreemptiveAuthInterceptor());
	    return builder.build();
	  }

	 private static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {
		    public void process(final HttpRequest request, final HttpContext context) throws HttpException {
		      final AuthState authState = (AuthState) context.getAttribute(HttpClientContext.TARGET_AUTH_STATE);

		      if (authState.getAuthScheme() == null) {
		        final CredentialsProvider credsProvider = (CredentialsProvider) context
		            .getAttribute(HttpClientContext.CREDS_PROVIDER);
		        final HttpHost targetHost = (HttpHost) context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
		        final Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(),
		            targetHost.getPort()));
		        if (creds == null) {
		          throw new HttpException("No creds provided for preemptive auth.");
		        }
		        authState.update(new BasicScheme(), creds);
		      }
		    }
	 }
		    
	
	/*cleanup method*/
	private static void cleanupResources(SolrClient solrClient, RetrieveAndRank service) throws SolrServerException, IOException {
		// TODO Auto-generated method stub
	  try {
	      final CollectionAdminRequest.Delete deleteCollectionRequest =
	          new CollectionAdminRequest.Delete();
	      deleteCollectionRequest.setCollectionName("example_collection");
         
	      System.out.println("Deleting collection...");
	      deleteCollectionRequest.process(solrClient);
	      System.out.println("Collection deleted.");
	    } finally {
	      try {
	        System.out.println("Deleting configuration...");
	        service.deleteSolrClusterConfiguration(clusterId, configName);
	        System.out.println("Configuration deleted.");
	      } finally {
	        System.out.println("Closing Solr client...");
	        solrClient.close();
	        System.out.println("Clients closed.");
	      }
	    }
		
	}
	
}
