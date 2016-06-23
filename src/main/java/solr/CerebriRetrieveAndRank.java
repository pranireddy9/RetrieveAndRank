package solr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
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

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.RetrieveAndRank;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.Ranker;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.Ranker.Status;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.Rankers;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrCluster;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrClusterOptions;
import com.ibm.watson.developer_cloud.retrieve_and_rank.v1.model.SolrClusters;

public class CerebriRetrieveAndRank {

	private static final String username = "7a5dafc4-8ca2-4e5c-bf08-f9a93db13dc2";
	private static final String password = "aPIRdD47EOZx";
	private static final String clusterId = "sc8d29e583_a5d7_4cc5_b7cc_ebae25e7664e";
	private static final String configName = "cranfield_solr_config";
	private static final String collection = "example_collection";
	private static final String configzip = "cranfield_solr_config.zip";
	private static final String rankercsv = "cranfield_gt.csv";
	private static final String writercsv = "traindata.txt";
	private static final String cranfielddatajson = "cranfield_data.json";
	private static final String NEW_LINE_SEPARATOR = "\n";

	public static void main(String[] args) throws IOException, SolrServerException {

		RetrieveAndRank service = new RetrieveAndRank();
		service.setUsernameAndPassword(username, password);
		SolrClient solrClient = getSolrClient(service.getSolrUrl(clusterId), username, password);

		// createSolrCluster(service);
		// getSolrClusters(service);
		// getSolrCluster(service);
		// uploadSolrClusterConfigurationZip(service);
		// getSolrClusterConfigurations(service);
		// getCofiguration(service);
		// createCollectionRequest(service, solrClient);
		// getDocumentBySolrQuery(solrClient);
		// cleanUp(service,solrClient);
		// try {
		// createRankerWithJava(service);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// }
		// try {
		// createRanker(service);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// }
		// deleteRanker(service, "3b140ax14-rank-3044");
		// getRankers(service);
		// try {
		// getRankerStatus(service,"3b140ax14-rank-3046");
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }

	}

	/* create solr cluster */
	public static void createSolrCluster(RetrieveAndRank service) {
		SolrClusterOptions options = new SolrClusterOptions("Cerebri cluster", 1);
		ServiceCall<SolrCluster> cluster = service.createSolrCluster(options);
		System.out.println("SolrCluster: " + cluster);
		// wait until the cluster is available
		while (!cluster.rx().isDone()) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/* List Solr clusters */
	public static void getSolrClusters(RetrieveAndRank service) {
		ServiceCall<SolrClusters> clusters = service.getSolrClusters();
		System.out.println(clusters);
	}

	/* Get information about a Solr cluster */
	public static void getSolrCluster(RetrieveAndRank service) {
		System.out.println(service.getSolrCluster(clusterId));
	}

	/* Delete Solr cluster */
	public static void deleteCluster(RetrieveAndRank service) {
		service.deleteSolrCluster(clusterId);
	}

	/* Upload Solr configuration */
	public static void uploadSolrClusterConfigurationZip(RetrieveAndRank service) {
		URL url = CerebriRetrieveAndRank.class.getClassLoader().getResource(configzip);
		File configZip = null;
		try {
			configZip = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		service.uploadSolrClusterConfigurationZip(clusterId, configName, configZip);
	}

	/* List Solr configurations */
	public static void getSolrClusterConfigurations(RetrieveAndRank service) {
		System.out.println(service.getSolrClusterConfigurations(clusterId));
	}

	/* Get configuration */
	public static void getCofiguration(RetrieveAndRank service) throws IOException {
		ServiceCall<InputStream> inputStream = service.getSolrClusterConfiguration(clusterId, configName);
		OutputStream os = new FileOutputStream("src/main/resources/targetFile.zip");
		byte[] buffer = new byte[1024];
		int bytesRead;
		os.flush();
		os.close();
	}

	/* Delete configuration */
	public static void deleteSolrClusterConfiguration(RetrieveAndRank service) {
		service.deleteSolrClusterConfiguration(clusterId, configName);
		System.out.println("Configuration deleted.");
	}

	/* Create Solr collection */
	public static void createCollectionRequest(RetrieveAndRank service, SolrClient solrClient) {
		ServiceCall<SolrCluster> cluster = service.getSolrCluster(clusterId);
		CollectionAdminRequest.Create createCollectionRequest = new CollectionAdminRequest.Create();
		createCollectionRequest.setCollectionName(collection);
		createCollectionRequest.setConfigName(configName);
		System.out.println("Creating collection...");
		CollectionAdminResponse response = null;
		try {
			response = createCollectionRequest.process(solrClient);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!response.isSuccess()) {
			System.out.println(response.getErrorMessages());
			throw new IllegalStateException("Failed to create collection: " + response.getErrorMessages().toString());
		}
		System.out.println("Collection created.");
		System.out.println(response);
	}

	/* Delete Solr collection */
	private static void cleanUp(RetrieveAndRank service, SolrClient solrClient) {
		try {
			cleanupResources(solrClient, service);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		deleteCluster(service);
	}

	/* Index documents */
	public static void solrIndexDocument(RetrieveAndRank service) {
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", 1);
		document.addField("author", "pranitha,d.");
		document.addField("bibliography", "j. ae. scs. 25, 1958, 324.");
		document.addField(
				"body",
				"experimental investigation of the aerodynamics of a wing in a slipstream .   an experimental study of a wing in a propeller slipstream was made in order to determine the spanwise distribution of the lift increase due to slipstream at different angles of attack of the wing and at different free stream to slipstream velocity ratios .  the results were intended in part as an evaluation basis for different theoretical treatments of this problem .   the comparative span loading curves, together with supporting evidence, showed that a substantial part of the lift increment produced by the slipstream was due to a /destalling/ or boundary-layer-control effect .  the integrated remaining lift increment, after subtracting this destalling lift, was found to agree well with a potential flow theory .   an empirical evaluation of the destalling effects was made for the specific configuration of the experiment .");
		document.addField("title", "experimental investigation of the aerodynamics of a wing in a slipstream .");
		System.out.println("Indexing document...");
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs.add(document);
		SolrClient client = getSolrClient(service.getSolrUrl(clusterId), username, password);
		UpdateResponse addResponse = null;
		try {
			addResponse = client.add("example_collection", docs);
			UpdateResponse response = client.deleteById(collection, 1);
			client.commit("example_collection");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(addResponse);
		System.out.println("Indexed and committed document.");
	}

	private static void getDocumentBySolrQuery(SolrClient client) {
		SolrQuery query = new SolrQuery("*:*");
		QueryResponse response1 = null;
		try {
			response1 = client.query(collection, query);
		} catch (SolrServerException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println(response1);
	}

	/* Search Solr standard query parser */
	public static void queryParser(RetrieveAndRank service) {
		HttpSolrClient solrClient = getSolrClient(service.getSolrUrl(clusterId), username, password);
		solrClient = getSolrClient(service.getSolrUrl("scfaaf8903_02c1_4297_84c6_76b79537d849"), "{username}",
				"{password}");
		SolrQuery query = new SolrQuery("*:*");
		QueryResponse response = null;
		try {
			response = solrClient.query("example_collection", query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(response);
	}

	/* Create ranker */
	public static void createRanker(RetrieveAndRank service) throws InterruptedException, ExecutionException {
		URL url = CerebriRetrieveAndRank.class.getClassLoader().getResource(rankercsv);
		File rankerCsv = null;
		ServiceCall<Ranker> ranker = null;
		try {
			rankerCsv = new File(url.toURI());
			ranker = service.createRanker("ranker_new_1", rankerCsv);
			System.out.println(ranker.rx().get().getId());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void createRankerWithJava(RetrieveAndRank service) throws InterruptedException, ExecutionException {
		URL url = CerebriRetrieveAndRank.class.getClassLoader().getResource(rankercsv);
		URL writeurl = CerebriRetrieveAndRank.class.getClassLoader().getResource(writercsv);
		File csv = null;
		File writecsv = null;
		try {
			csv = new File(url.toURI());
			writecsv = new File(writeurl.toURI());
		FileReader filereader = null;
		CSVParser csvFileParser = null;
		// create the csv format object with the header mapping
		CSVFormat csvFileCsvFormat = CSVFormat.EXCEL.withSkipHeaderRecord();
			filereader = new FileReader(csv);
			csvFileParser = new CSVParser(filereader, csvFileCsvFormat);

			List<CSVRecord> csvRecords = csvFileParser.getRecords();
			FileWriter fileWriter = null;
			CSVPrinter csvFilePrinter = null;
			CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
			String gt = "";
			// String gt =
			// "184,3,29,3,31,3,12,2,51,2,102,2,13,1,14,1,15,1,57,3,378,3,859,3,185,2,30,2,37,2,52,1,142,1,195,1,875,3,56,2,66,2,95,2,462,1,497,2,858,2,876,2,879,2,880,2,486,0";
			boolean isHeader = true;
			for (int i = 0; i < csvRecords.size(); i++) {
				CSVRecord record = csvRecords.get(i);
				String question = record.get(0);
				String csvRecord = record.toString();
				for (int k = 0; k < record.size(); k++) {
					if (k == 0) {
						question = record.get(0);
					} else {
						if (k == (record.size() - 1)) {
							gt = gt + record.get(k);
						} else {
							gt = gt + record.get(k) + ",";
						}
					}
				}
				String curl = "curl -k -s -v -u " + username + ":" + password + " \"q=" + question + "&gt=" + gt
						+ "&generateHeader=" + isHeader + "&rows=10&returnRSInput=true&wt=json\" "
						+ "\"http://gateway.watsonplatform.net/retrieve-and-rank/api/v1/solr_clusters/" + clusterId
						+ "/" + collection + "/fcselect\"";
				ProcessBuilder builder = new ProcessBuilder(curl);
				builder.redirectOutput(writecsv);
				builder.start();
				// ProcessBuilder p=new ProcessBuilder("curl","-k","-s",
				// "-v","-u",
				// username + ":" + password, curl);
				// Process p = Runtime.getRuntime().exec(curl);
				// p.getInputStream();
				// curl -k -s %s -u %s -d
				// "q=%s&gt=%s&generateHeader=%s&rows=%s&returnRSInput=true&wt=json"
				// "%s"' (VERBOSE, CREDS, question, relevance, add_header, ROWS,
				// SOLRURL)
				// this is the place where we form the curl command to watson
				// http://gateway.watsonplatform.net/retrieve-and-rank/api/v1/solr_clusters/{cluster_id}/{collection_name}/fcselect?q=in
				// practice, how close to reality are the assumptions that the
				// flow in a hypersonic shock tube using nitrogen is non-viscous
				// and in thermodynamic
				// equilibrium.&gt=656,2,1313,2,1317,2,1316,1,1318,2,1319,2,1157,2,1274,2,1286,0&generateHeader=false&rows=50&returnRSInput=true&wt=json&fl=id
				isHeader = false;
			}
		} catch (Exception e) {
			System.out.println("the exception is " + e.getMessage());

		}
	}

	/* List rankers */
	public static void getRankers(RetrieveAndRank service) {
		ServiceCall<Rankers> rankers = service.getRankers();
		try {
			System.out.println(rankers.rx().get().getRankers().toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/* Get information about a ranker */
	public static void getRankerStatus(RetrieveAndRank service, String rankerId) throws ExecutionException,
			InterruptedException {
		ServiceCall<Ranker> ranker = service.getRankerStatus(rankerId);
		Ranker ranker2 = null;
		try {
			ranker2 = ranker.rx().get();
		System.out.println(ranker2.getStatus());
		while (ranker2.getStatus() == Status.TRAINING) {
				Thread.sleep(4000);
				ranker = service.getRankerStatus(ranker.rx().get().getId());
				System.out.println("ranker status: " + ranker.rx().get().getStatus());
		}
		}
		catch (InterruptedException e2) {
			e2.printStackTrace();
		} catch (ExecutionException e2) {
			e2.printStackTrace();
		}
		System.out.println("my ranker status is " + ranker2.getStatus());
	}

	/* Delete the ranker */
	public static void deleteRanker(RetrieveAndRank service, String id) {
		ServiceCall<Void> call = service.deleteRanker(id);
		System.out.println("ranker deleted." + call.rx().isDone());
	}

	private static HttpSolrClient getSolrClient(String uri, String username, String password) {
		return new HttpSolrClient(uri, createHttpClient(uri, username, password));
	}

	private static HttpClient createHttpClient(String uri, String username, String password) {
		final URI scopeUri = URI.create(uri);
		final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(scopeUri.getHost(), scopeUri.getPort()),
				new UsernamePasswordCredentials(username, password));
		final HttpClientBuilder builder = HttpClientBuilder.create().setMaxConnTotal(128).setMaxConnPerRoute(32)
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

	/* cleanup method */
	private static void cleanupResources(SolrClient solrClient, RetrieveAndRank service) throws SolrServerException,
			IOException {
		// TODO Auto-generated method stub
		try {
			final CollectionAdminRequest.Delete deleteCollectionRequest = new CollectionAdminRequest.Delete();
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
