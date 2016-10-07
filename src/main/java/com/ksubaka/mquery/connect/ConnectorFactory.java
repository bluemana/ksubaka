package com.ksubaka.mquery.connect;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.web.client.RestTemplate;

import com.ksubaka.mquery.connect.omdbapi.OmdbApiConnector;
import com.ksubaka.mquery.connect.tmdb.TmdbConnector;

public class ConnectorFactory {
	
	public static Connector create(Api api, RestTemplate restTemplate) throws Exception {
		Connector connector = null;
		if (api == Api.TMDB) {
			try (InputStream input = new FileInputStream("config.properties")) {
				Properties config = new Properties();
				config.load(input);
				String apiKey = config.getProperty("api_key");
				if (apiKey != null) {
					connector = new TmdbConnector(new RestTemplate(), apiKey);
				} else {
					throw new Exception("api_key entry not found in config.properties");
				}
			} catch (IOException e) {
				throw new Exception("An I/O error occured while accessing config.properties", e);
			}
		} else if (api == Api.OMDB_API) {
			connector = new OmdbApiConnector(restTemplate);
		}
		return connector;
	}
}
