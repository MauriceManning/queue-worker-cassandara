package edu.berkeley.path.results_queue_workers.workers;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import edu.berkeley.path.model_objects.jaxb.LinkState;
import edu.berkeley.path.model_objects.traffic_state.LinkStateSet;
import com.datastax.driver.core.Cluster;
import edu.berkeley.path.results_queue_workers.model.LinkDataRaw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Session;

import java.util.concurrent.CountDownLatch;


/**
 * This is the listener class for the scenario channel. When a scenario is
 * received the worker will persist into the database
 */

/**
 * create table linkdataraw ( link_id bigint, network_id bigint, capacity double, run_id bigint,
 * vehicle_count double, speed double,  lanes double, lane_offset double, length double, type_name text,
 * type_id bigInt, link_name text, begin_node_id  bigint,  end_node_id bigint, is_source boolean,
 * is_sink boolean, is_valid boolean, primary key (link_id, run_id) ) with CLUSTERING ORDER BY (run_id DESC) ;
 */


@Component
public class PersistWorker {

    @Autowired
    private CassandraOperations cassandraOperations;

//    @Autowired
//    CountDownLatch latch;


    public long persistLinkResult(LinkDataRaw linkDataRaw) {

        final Logger logger = LogManager.getLogger(PersistWorker.class.getName());


        logger.info("PersistWorker.persistLinkResult: " + linkDataRaw  );


        try {

            // todo: insert set of objects
            // http://docs.spring.io/spring-data/cassandra/docs/1.1.2.RELEASE/reference/html/
//            //Multiple inserts for high speed ingestion
//            List<LinkDataRaw> person1 = new ArrayList<LinkDataRaw>();
//            String cql = "insert into linkdataraw (id, ...,) values ('123123123', 'Alison', 39)";
//            cassandraOperations.ingest(cqlIngest, people);


            LinkDataRaw ldr = cassandraOperations.insert(linkDataRaw);
            System.out.println("cassandraOperations.insert: " + ldr  );

        } catch (Exception e) {
            logger.info("persistLinkResult insert exception: " + e.getMessage()  );
            e.printStackTrace();
        }

        // this counts down the number of messages received so that we know that everything arrived as expected
        //latch.countDown();

        return 1;

    }

    public void persistLinkStateSet(String linkStateSetString) {

        final Logger logger = LogManager.getLogger(PersistWorker.class.getName());
        logger.info("PersistWorker.persistLinkStateSet received "  );

        try {
            edu.berkeley.path.model_objects.util.Serializer serializer = new edu.berkeley.path.model_objects.util.Serializer();
            LinkStateSet linkStateSet = serializer.xmlToObject(linkStateSetString, LinkStateSet.class, null);

            logger.info("PersistWorker.persistLinkStateSet: " + linkStateSet  );

            // todo: insert set of objects

            for (LinkState ls : linkStateSet.getLinkState()) {

                Insert insert = QueryBuilder.insertInto("link_data_total");   //test cassandra
                insert.setConsistencyLevel(ConsistencyLevel.ONE);

                insert.value("app_run_id", linkStateSet.getRunId() );
                insert.value("ts", linkStateSet.getTimestamp() );
                insert.value("link_id", ls.getId());
                insert.value("agg_type", ls.getAggregationType().getId());
                insert.value("app_type_id", ls.getApplicationType().getId());
                insert.value("capacity", ls.getCapacity());
                insert.value("capacity_drop", ls.getCapacityDrop());
                insert.value("congestion_wave_speed", ls.getCongestionSpeed());
                insert.value("critical_speed", ls.getCritSpeed());
                insert.value("density", ls.getDensity());
                insert.value("free_flow_speed", ls.getFfSpeed());
                //insert.value("in_flow", ls.());
                insert.value("jam_density", ls.getJamDensity());
                insert.value("network_id", linkStateSet.getNetworkId());
                //insert.value("out_flow", ls.());




//                Insert insert = QueryBuilder.insertInto("linkStateset");
//                insert.setConsistencyLevel(ConsistencyLevel.ONE);
//                insert.value("id", ls.getId());
//                insert.value("linkTime", linkStateSet.getTimestamp());
//                insert.value("network_id", linkStateSet.getNetworkId());
//                insert.value("run_id", ls.getRunId());
//                insert.value("particle_id", linkStateSet.getParticleId());
//                insert.value("dt", linkStateSet.getDt());
//                insert.value("quantity_type", linkStateSet.getQuantityType());
//                insert.value("density", ls.getDensity());
//                insert.value("jam_density", ls.getJamDensity());
//                insert.value("capacity_drop", ls.getCapacityDrop());
//                insert.value("ff_speed", ls.getFfSpeed());
//                insert.value("crit_speed", ls.getCritSpeed());
//                insert.value("congestion_speed", ls.getCongestionSpeed());

                logger.info("cassandraOperations insert:  " + insert.toString() );
//                insert.value("", );
//                insert.value("", );

                cassandraOperations.execute(insert);
                logger.info("cassandraOperations inserted ");
            }

        } catch (Exception e) {
            logger.info( "persistLinkStateSet insert exception: " + e.getMessage()  );
            e.printStackTrace();
        }

        // this counts down the number of messages received so that we know that everything arrived as expected
        //latch.countDown();

        return;

    }

}
