# Ksubaka's query tool

Implementation of a command-line tool that queries web APIs to retrieve information on movies.

Current usage:

```
usage: mquery [-?] -a <arg> -m <arg>
 -?,--help          print this help message
 -a,--api <arg>     use the specified API. Available options:
                    tmdb : the API of The Movie Database (themoviedb.org)
                    omdb : the API of Open Movie Database API (omdbapi.com)
 -m,--movie <arg>   a space-separated list of keywords to search for
                    movies
```

Command-line example:

```
java -Dlog4j.configurationFile=log4j2.xml -jar mquery-0.1-SNAPSHOT.jar --api tmdb --movie blade runner
```

Sample output for the above command:

```
16:44:33.270 [main] INFO  com.ksubaka.mquery.Main - Searching for "blade runner" from TMDb...
16:44:35.140 [main] INFO  com.ksubaka.mquery.connect.tmdb.TmdbConnector - Aggregating movie data of movie 1 of 5...
16:44:36.028 [main] INFO  com.ksubaka.mquery.connect.tmdb.TmdbConnector - Aggregating movie data of movie 2 of 5...
16:44:36.957 [main] INFO  com.ksubaka.mquery.connect.tmdb.TmdbConnector - Aggregating movie data of movie 3 of 5...
16:44:37.265 [main] INFO  com.ksubaka.mquery.connect.tmdb.TmdbConnector - Aggregating movie data of movie 4 of 5...
16:44:37.565 [main] INFO  com.ksubaka.mquery.connect.tmdb.TmdbConnector - Aggregating movie data of movie 5 of 5...
Blade Runner (1982) - Ridley Scott
Blade Runner 2049 (2017) - Denis Villeneuve
Dangerous Days: Making Blade Runner (2007) - Charles de Lauzirika
Earthkiller (2011) - Andrew Bellware
On the Edge of 'Blade Runner' (2000) - Andrew Abbott
```

Note that the actual result (the last five lines) is printed directly to `stdout` and not through the logger.
