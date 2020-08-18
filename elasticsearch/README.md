# Elasticsearch

## Environment setup
### Single node cluster
- Pulling the image
    ```$xslt
    docker pull docker.elastic.co/elasticsearch/elasticsearch:7.8.1
    docker pull docker.elastic.co/kibana/kibana:7.8.1
    ```
  Alternatively, download other Docker images that contain only features available under the Apache 2.0 license, go to www.docker.elastic.co.
-  Starting a single node cluster
    ```$xslt
    docker volume create elasticsearch_data
    docker run -d -p 9200:9200 -p 9300:9300 --name="elasticsearch" -v /var/run/docker.sock:/var/run/docker.sock -v elasticsearch_data:/data -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.8.1
    ```
- Run Kibana for development, notice in the command we have ```--link elasticsearch:elasticsearch```, this parameter specifies the local elasticsearch container. 
    ```$xslt
    docker volume create kibana_data
    docker run --link elasticsearch:elasticsearch -d -p 5601:5601 --name="kibana" -v /var/run/docker.sock:/var/run/docker.sock -v kibana_data:/data docker.elastic.co/kibana/kibana:7.8.1
    ```
  
### Multi-node cluster
- [Running the Elastic Stack on Docker](https://www.elastic.co/guide/en/elastic-stack-get-started/current/get-started-docker.html)
- Run docker-compose to bring up the Elastic cluster and Kibana
    ```$xslt
    docker-compose up
    ```   
- Submit a ```_cat/nodes``` request to see that the nodes are up and running:
    ```$xslt
    curl -X GET "localhost:9200/_cat/nodes?v&pretty"
    ```         
- Open Kibana to load sample data and interact with the cluster: http://localhost:5601.
- Enjoy!

### Reference: 
- [Guide to Elasticsearch in Java](https://www.baeldung.com/elasticsearch-java)
- [Getting started with the Elastic Stack](https://www.elastic.co/guide/en/elastic-stack-get-started/7.x/get-started-elastic-stack.html)
- [Installing Elasticsearch](https://www.elastic.co/guide/en/elasticsearch/reference/current/install-elasticsearch.html)
- [Install Elastic stack on Docker swarm](https://github.com/shazChaudhry/docker-elastic)
