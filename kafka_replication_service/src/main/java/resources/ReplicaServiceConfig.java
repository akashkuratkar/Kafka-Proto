package resources;

public final class ReplicaServiceConfig {
    public final static int REPLICA_SERVICE_PORT = 8700;
    public final static String REPLICA_SERVICE_HOST = "localhost";
    public final static String PATH_TOPICS = "topics/";
    public final static String PATH_DEFAULT_PARTITION = "/partitions/0/";
    public final static String FORMAT_LOG = ".csv";
    public final static String PATH_FULL = PATH_TOPICS + PATH_DEFAULT_PARTITION;
    public final static int ZOOKEEPER_PORT = 5676;
    public final static String ZOOKEEPER_HOST = "localhost";
    public final static String ZOOKEEPER_GET_LEADER_ENDPOINT = "/get_topic_leader";
}
