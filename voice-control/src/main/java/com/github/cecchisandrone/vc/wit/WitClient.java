package com.github.cecchisandrone.vc.wit;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class WitClient {

	private String baseUri;

	private CloseableHttpClient httpclient = HttpClients.createDefault();

	private AudioFormat audioFormat;

	public WitClient(String url, AudioFormat audioFormat) throws URISyntaxException {
		URIBuilder builder = new URIBuilder(url);
		builder.addParameter("v", "20150318");
		baseUri = builder.toString();
		this.audioFormat = audioFormat;
	}

	public void sendChunkedAudio(InputStream inputStream) {

		HttpPost httpPost;
		try {
			URIBuilder builder = new URIBuilder(baseUri);
			builder.addParameter("encoding", audioFormat.getEncoding() == Encoding.PCM_SIGNED ? "signed-integer"
					: "unsigned-integer");
			builder.addParameter("bits", Integer.toString(audioFormat.getSampleSizeInBits()));
			builder.addParameter("rate", Long.toString((long) audioFormat.getSampleRate()));
			builder.addParameter("endian", audioFormat.isBigEndian() ? "big" : "little");
			httpPost = new HttpPost(builder.build());

			InputStreamEntity reqEntity = new InputStreamEntity(inputStream, -1, ContentType.create("audio/raw"));
			// InputStreamEntity reqEntity = new InputStreamEntity(new
			// FileInputStream(new File("asd.wav")), new File(
			// "asd.wav").length(), ContentType.create("audio/wav"));
			reqEntity.setChunked(true);
			httpPost.setEntity(reqEntity);
			httpPost.addHeader("Authorization", "Bearer GNOUVVQQWWBQCXHJ263FVIRSFWFIGVCE");
			httpPost.addHeader("Content-Type", "audio/wav");
			System.out.println("Executing request: " + httpPost.getRequestLine());

			CloseableHttpResponse response = httpclient.execute(httpPost);
			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();

			StringWriter writer = new StringWriter();
			IOUtils.copy(content, writer, "UTF-8");
			String json = writer.toString();

			System.out.println(json);

			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}