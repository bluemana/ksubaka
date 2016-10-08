package com.ksubaka.mquery.connect;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

public abstract class ConnectorTest {

	@Test
	public abstract void movies_NoMatch_EmptyList() throws Exception;
	
	@Test
	public abstract void movies_Match_List() throws Exception;
	
	@Test
	public abstract void movies_MultiplePageMatch_List() throws Exception;
	
	@Test
	public abstract void clean_NoReleaseYear_NullReleaseYear() throws Exception;
	
	@Test
	public abstract void clean_NoDirector_NullDirector() throws Exception;
	
	protected String readResource(String resourceName) throws IOException {
		try (InputStream input = getClass().getResourceAsStream(resourceName);
				Scanner scanner = new Scanner(input)) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
