package edu.berkeley.path.results_queue_workers;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import edu.berkeley.path.model_objects.traffic_state.LinkState;
import edu.berkeley.path.model_objects.traffic_state.LinkStateSet;
import edu.berkeley.path.model_objects.scenario.Scenario;
import edu.berkeley.path.results_queue_workers.model.LinkDataRaw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.integration.Message;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 *    http://docs.spring.io/spring-data/cassandra/docs/1.0.5.RELEASE/reference/html/cassandra.core.html
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:queue-workers-config.xml"} )
public class ResultPersistDevTest {

    private Logger logger = null;
    private static Connection connection;
    private static Scenario scenario;

//    private static Cluster cluster;
//    private static Session session;

    @Autowired
    private CassandraOperations cassandraOperations;

//    @Autowired
//    CountDownLatch latch;

    @Autowired
    protected  LinkManager linkManager;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JmsTemplate jmsTopicTemplate;


    @Autowired
    QueueChannel testChannel;

    @Before
    public void setUp() throws Exception {

        this.logger = LogManager.getLogger(ResultPersistDevTest.class.getName());
    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    @Ignore
    public void testResultPublish() {
        try {

            jmsTemplate.setReceiveTimeout(3000);

            try {

                // create one Link and wrap it in an event so it is ready to publish to Reactor
                LinkDataRaw link = linkManager.getLink();
                System.out.println("link getSpeedLimit: " + link.getSpeedLimit() + "mph");


                //cluster = Cluster.builder().addContactPoints(InetAddress.getLocalHost()).build();
                //session = cluster.connect("dev");

                //CassandraOperations cassandraOps = new CassandraTemplate(session);

                LinkDataRaw ldr = cassandraOperations.insert(link);
                System.out.println("cassandraOperations.insert: " + ldr  );

                //long latchLimit = latch.getCount();
                for (long i = 0; i < 100; i++) {
                    link.setLinkid(i);

                    jmsTemplate.convertAndSend("resultPublish", link);
                }
//
//                // now catch the id of the inserted scenario
//                Message<?> message =  testChannel.receive(2000);
//                logger.info("ResultPublishTest publish  response:" + message.toString() );
//                assertNotNull("Expected a result id in the response", message);

                //todo: retrieve the result id from the payload then delete to clean up

                //latch.await();

            } catch (Exception e) {
                e.printStackTrace();
                // assert fails if exception is thrown
            }

        } catch (Exception ex) {
            logger.info("ResultPublishTest publish  Exception ex:" + ex.getMessage());
            ex.printStackTrace();

            // assert fails if exception is thrown
        }
    }



    @Test
    //@Ignore
    public void testLinkStatePublish() {
        try {

            //long latchLimit = latch.getCount();
            for (long i = 0; i < 3; i++) {
                // create one Link and wrap it in an event so it is ready to publish to Reactor
                LinkState linkState = new edu.berkeley.path.model_objects.traffic_state.LinkState();
                linkState.setDt(1.0);
                linkState.setNetworkId(1L);
                linkState.setCapacity(1.0);
                linkState.setCritSpeed(1.0);
                linkState.setRunId(i);
                linkState.setId(i);
                System.out.println("linkState created: " + linkState.toString());

                List linkStateList = new ArrayList<LinkState>();
                linkStateList.add(linkState);

                LinkStateSet linkStateSet = new LinkStateSet();
                linkStateSet.setLinkStateList(linkStateList);
                linkStateSet.setRunId(i);
                linkStateSet.setNetworkId(1L);

                edu.berkeley.path.model_objects.util.Serializer serializer = new edu.berkeley.path.model_objects.util.Serializer();
                String xmlLSS = serializer.objectToXml(linkStateSet);

                jmsTopicTemplate.convertAndSend("engineResults", xmlLSS);
                logger.info("testLinkStatePublish  convertAndSend");
            }

            //latch.await();

        } catch (Exception ex) {
            logger.info("testLinkStatePublish   Exception ex:" + ex.getMessage());
            ex.printStackTrace();

            // assert fails if exception is thrown
        }
    }


    public void onMessage(javax.jms.Message message) { logger.info("onMessage:  update received" ); }

}
