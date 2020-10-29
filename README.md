# Homework 2 : DBLP Map Reduce

### Description: Design and implement an instance of the Hadoop MapReduce computational model to perform analyses on DBLP publication data

## Overview

As part of this project, a MapReduce program is created for the parallel processing of the publicly available [DBLP dataset](https://dblp.uni-trier.de/xml/). The dataset contains records for various publications by author(s) at different types of venues (like conferences, schools,books, and journals). Multiple map/reduce jobs have been defined to extract various insights from the dataset. 

The map/reduce jobs created are :

1. List top 10 authors published at each venue
2. List publications with only 1 author at each venue
3. List publications with highest number of authors for each venue
4. List of top 100 authors who publish with most co-authors(in desc. order)
5. List of 100 authors who publish without co-authors

## Instructions to Execute

- Generate executable jar file

  - Clone this repository

  - Open root folder of the project in the terminal and assemble the project jar using command:

    `sbt clean compile assembly`

    This command compiles the source code, executes the test cases and builds the executable jar file “*hw2_dblp_mapred.jar*” in the folder “*target/scala-2.13*”

- Setup Hadoop environment

  - Start Hadoop DFS & YARN services using:

    - Start NameNode & DataNode daemons

      `start-dfs.sh`

    - Start ResourceManager & NodeManager daemons

      `start-yarn.sh`

    - Verify if daemons are running using:

      `jps`

  - Create directory in HDFS to store the input file:

    `hdfs dfs -mkdir input`

  - Place the dblp.xml file in the directory created above:

    `hdfs dfs -put path/to/dblp.xml input`

- Execute jar file

  - Run the jar file using:

    `hadoop jar hw2_dblp_mapred.jar job_num input`

    - The argument ‘*job_num*’ has to be provided by user and can have possible values of 1/2/3/4/5 corresponding to the job being performed as described [above](#overview) and [below](#mapreduce-jobs)

    - The output folder for the job results have been set in the config file ‘*JobSpec.conf*’ as follows:

      ```
      # Output paths for MapReduce jobs
      master_output_path = "output_hw2"
      Job1_output_path = "/top_10_authors_at_venues"
      Job2_output_path = "/pubs_with_1_author_at_venues"
      Job3_output_path = "/pubs_with_max_authors_at_venues"
      Job4_output_path = "/top_100_authors_max_coauthors"
      Job5_output_path = "/100_authors_0_coauthors"
      ```

    - The main output folder ‘output_hw2’ needs to be deleted if repeating any map/reduce job or else an error is raised. Delete folder using:

      `hdfs dfs -rm -r output_hw2`

  - *After executing all jobs*, extract the output files from the HDFS into a local directory “*mapreduce_output*” using:

    `hdfs dfs -get output_hw2 mapreduce_output`

    Output of all jobs is in CSV format.

- Stop Hadoop services

  - Stop all daemons after execution is completed using:

    `stop-yarn.sh
    stop-dfs.sh`

## Application Design

- ### XML parsing

  - For parsing the dblp.xml file using the dblp.dtd schema I have used [multiple tag XMLInputFormatter](https://github.com/Mohammed-siddiq/hadoop-XMLInputFormatWithMultipleTags) by Mohammed Siddiq, which is an implementation of Mahout's XMLInputFormat with support for multiple input and output tags. 

  - The input and output tags are mentioned in the config file.

  - The tags considered are:

    `<article ,<book ,<incollection ,<inproceedings ,<mastersthesis ,<proceedings ,<phdthesis ,<www `

### MapReduce Jobs

- ### Job 1

  - Mapper Class: `VenueTopTenAuthorsMapper`
  - Reducer Class:`VenueTopTenAuthorsReducer`
  - Output path: `output_hw2/top_10_authors_at_venues`
  - Output format: `key:<venue name> & value:<list of authors(seperated by ';')>`

- ### Job 2

  - Mapper Class:`VenueOneAuthorMapper`
  - Reducer Class:`VenueOneAuthorReducer`
  - Output path: `output_hw2/pubs_with_1_author_at_venues"`
  - Output format:`key:<venue name> & value:<list of publications(seperated by ';')>` 

- ### Job 3

  - Mapper Class:`VenueTopPubMapper`
  - Reducer Class:`VenueTopPubReducer`
  - Output path: `output_hw2/pubs_with_max_authors_at_venues`
  - Output format: `key:<venue name> & value:<publication name>`

- ### Job 4

  - Mapper Class:`CoAuthorCountMapper`
  - Reducer Class:`MostCoAuthorCountReducer`
  - Output path: `output_hw2/top_100_authors_max_coauthors`
  - Output format:`key:<author name> & value:<max. number of coauthors>` 

- ### Job 5

  - Mapper Class:`CoAuthorCountMapper`
  - Reducer Class:`ZeroCoAuthorCountReducer`
  - Output path: `output_hw2/100_authors_0_coauthors`
  - Output format:`key:<author name> & value:<0>` 