import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
public class PhilippsGet {
	 public static void main(String[] args) throws ClientProtocolException, IOException {
		  HttpClient client = new DefaultHttpClient();
		  HttpGet request = new HttpGet('http://restUrl');
		  HttpResponse response = client.execute(request);
		  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = '';
		  while ((line = rd.readLine()) != null) {
		    System.out.println(line);
		  }
		 }
}
