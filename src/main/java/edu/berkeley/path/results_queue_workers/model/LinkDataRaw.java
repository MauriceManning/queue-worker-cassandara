package edu.berkeley.path.results_queue_workers.model;

import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;

import java.io.Serializable;

/**
 *
 */

@PrimaryKeyClass
public class LinkDataRaw  implements Serializable  {


    @PrimaryKeyColumn(name = "link_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private long  linkid;

    @Column(value = "network_id")
    private long  networkid;

    @Column(value = "capacity")
    private double  capacity;

    //@Column(value = "")
    @PrimaryKeyColumn(name = "run_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private long  runid;

    @Column(value = "vehicle_count")
    private double  vehiclecount;

    @Column(value = "speed")
    private double speed;

    @Column(value = "lanes")
    private double lanes;

    @Column(value = "lane_offset")
    private double laneOffset;

    @Column(value = "length")
    private double length;

    @Column(value = "type_name")
    private String typeName;

    @Column(value = "type_id")
    private long typeId;

    @Column(value = "link_name")
    private String linkName;

    @Column(value = "begin_node_id")
    private long beginNodeId;

    @Column(value = "end_node_id")
    private long endNodeId;

    @Column(value = "is_source")
    private boolean isSource;

    @Column(value = "is_sink")
    private boolean isSink;

    @Column(value = "is_valid")
    private boolean isValid;

//    private java.util.List<String> roadNames;
//    private java.util.List<Point> points;

    public boolean getIsSource() {
        return isSource;
    }
    public void setIsSource(boolean isSource) {
        this.isSource = isSource;
    }

    public boolean getIsSink() {
        return isSink;
    }
    public void setIsSink(boolean isSink) {
        this.isSink = isSink;
    }

    public boolean getIsValid() {
        return isValid;
    }
    public void setIsValid(boolean isSink) {
        this.isValid = isValid;
    }

//    public java.util.List<String> getRoadNames() {
//        return roadNames;
//    }
//    public void setRoadNames(java.util.List<String> roadNames) {
//        this.roadNames = roadNames;
//    }

//    public java.util.List<Point> getPoints() {
//        return points;
//    }
//    public void setPoints(java.util.List<Point> roadNames) {
//        this.points = points;
//    }


    public long getEndNodeId() {
        return endNodeId;
    }
    public void setEndNodeId(long endNodeId) {
        this.endNodeId = endNodeId;
    }

    public long getBeginNodeId() {
        return beginNodeId;
    }
    public void setBeginNodeId(long beginNodeId) {
        this.beginNodeId = beginNodeId;
    }

    public String getLinkName() {
        return linkName;
    }
    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public long getTypeId() {
        return typeId;
    }
    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }

    public double getLaneOffset() {
        return laneOffset;
    }
    public void setLaneOffset(double laneOffset) {
        this.laneOffset = laneOffset;
    }

    public double getLanes() {
        return lanes;
    }
    public void setLanes(double lanes) {
        this.lanes = lanes;
    }

    public long getNetworkid() {
        return networkid;
    }
    public void setNetworkid(long networkid) {
        this.networkid = networkid;
    }

    public long getLinkid() {
        return linkid;
    }
    public void setLinkid(long linkid) {
        this.linkid = linkid;
    }

    public double getCapacity() {
        return capacity;
    }
    public void setCapacity(double capacity) { this.capacity = capacity;}

    public long getRunid() {
        return runid;
    }
    public void setRunid(long runid) { this.runid = runid;}

    public double getVehiclecount() {
        return vehiclecount;
    }
    public void setVehiclecount(double vehiclecount) { this.vehiclecount = vehiclecount;}

    public double getSpeedLimit() { return speed; }
    public void setSpeedLimit(double speed) { this.speed = speed; }


}
