package com.anven.BigData;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.jdbc.core.metadata.HsqlTableMetaDataProvider;

import java.io.IOException;
import java.net.URISyntaxException;

public class HelloHBase {
    public static void main(String[] args) throws URISyntaxException {
        System.setProperty("hadoop.home.dir", "/Users/mac/IdeaProjects/BigData/bin");

        //Load HBase Configuration
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "172.16.254.150,172.16.254.151,172.16.254.152");
        configuration.set("hbase.master", "172.16.254.150:16010");

        //Read Configuration File
//        configuration.addResource(new Path(ClassLoader.getSystemResource("hbase-site.xml").toURI()));
//        configuration.addResource(new Path(ClassLoader.getSystemResource("core-site.xml").toURI()));

        try {
            //Build HBase Connection
            Connection connection = ConnectionFactory.createConnection(configuration);
            //Get an admin interface to execute CRUD
            Admin admin = connection.getAdmin();

            TableName tableName = TableName.valueOf("hellohbase");
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            //ColumnFamily
            HColumnDescriptor hellocf = new HColumnDescriptor("hellocf");
            tableDescriptor.addFamily(hellocf);

            createOrOverWrite(admin, tableDescriptor);

            //set column family compress type to GZ
            hellocf.setCompressionType(Compression.Algorithm.GZ);
            hellocf.setMaxVersions(HConstants.ALL_VERSIONS);
            tableDescriptor.modifyFamily(hellocf);
            admin.modifyTable(tableDescriptor);

            //add a new column family
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor("newcf");
            hColumnDescriptor.setCompressionType(Compression.Algorithm.GZ);
            hColumnDescriptor.setMaxVersions(HConstants.ALL_VERSIONS);
            admin.addColumnFamily(tableName, hColumnDescriptor);

            //Table
            Table table = connection.getTable(tableName);

            //Put
            Put put = new Put(Bytes.toBytes("row01"));
            put.addColumn(Bytes.toBytes("hellocf"), Bytes.toBytes("name"), Bytes.toBytes("Kobe"));
            table.put(put);

            //Append
            Append append = new Append(Bytes.toBytes("row01"));
            append.add(Bytes.toBytes("hellocf"), Bytes.toBytes("name"), Bytes.toBytes(" GiGi"));
            table.append(append);

            //Increment
            Put put1 = new Put(Bytes.toBytes("row01"));
            put1.addColumn(Bytes.toBytes("hellocf"), Bytes.toBytes("age"), Bytes.toBytes(6L));
            Increment increment = new Increment(Bytes.toBytes("row01"));
            increment.addColumn(Bytes.toBytes("hellocf"), Bytes.toBytes("age"), 10L);
            table.increment(increment);

            //Query
            Get get = new Get(Bytes.toBytes("row01"));
            get.addFamily(Bytes.toBytes("hellocf"));
            Result result = table.get(get);
            byte[] name = result.getValue(Bytes.toBytes("hellocf"), Bytes.toBytes("name"));
            System.out.println("row01 name : " + name.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createOrOverWrite(Admin admin, HTableDescriptor hTableDescriptor) throws IOException {
        //Get Table name
        TableName tableName = hTableDescriptor.getTableName();
        if(admin.tableExists(tableName)) { //if this table exists, disable and delete this table
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }

        admin.createTable(hTableDescriptor);
    }
}
