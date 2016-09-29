/*
 * beymani: Outlier and anamoly detection 
 * Author: Pranab Ghosh
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.jtedilla.predictor;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * Storm topolgy driver for outlier detection
 * @author pranab
 *
 */
public class OutlierPredictor {
	
	/**
	 * @author pranab
	 *
	 */
	public static class PredictorBolt extends BaseRichBolt {
        private OutputCollector collector;
        private ModelBasedPredictor predictor;

		/* (non-Javadoc)
		 * @see backtype.storm.task.IBolt#prepare(java.util.Map, backtype.storm.task.TopologyContext, backtype.storm.task.OutputCollector)
		 */
		public void prepare(Map stormConf, TopologyContext context,
				OutputCollector collector) {
			this.collector = collector;
			String strategy = stormConf.get("predictor.model").toString();
			if (strategy.equals("mm")){
				predictor = new MarkovModelPredictor(stormConf);
			}
		}
		
		/* (non-Javadoc)
		 * @see backtype.storm.task.IBolt#execute(backtype.storm.tuple.Tuple)
		 */
		public void execute(Tuple input) {
			String entityID = input.getString(0);
			String record  = input.getString(1);

            System.out.print(this.getClass().getCanonicalName());
			double score = predictor.execute( entityID,  record);
			
			//write score to db
			System.out.println("Score[" + entityID + ", " + record + "] == " + score);
			//ack
			collector.ack(input);
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			
		}
		
	}
	
    public static void main(String[] args) throws Exception {
    	String topologyName = args[0];
    	String configFilePath = args[1];
    	
        FileInputStream fis = new FileInputStream(configFilePath);
        Properties configProps = new Properties();
        configProps.load(fis);

        //intialize config
        Config conf = new Config();
        conf.setDebug(true);
        for (Object key : configProps.keySet()){
            String keySt = key.toString();
            String val = configProps.getProperty(keySt);
            conf.put(keySt, val);
        }
        
        //spout
        LocalCluster localCluster = new LocalCluster();
        TopologyBuilder builder = new TopologyBuilder();
        int spoutThreads = Integer.parseInt(configProps.getProperty("predictor.spout.threads"));
        builder.setSpout("redisSpout", new RedisSpout(), spoutThreads);
        
        //detector bolt
        int boltThreads = Integer.parseInt(configProps.getProperty("predictor.bolt.threads"));
        builder.setBolt("predictor", new PredictorBolt(), boltThreads)
        	.fieldsGrouping("redisSpout", new Fields("entityID"));
       
        //submit topology
        int numWorkers = Integer.parseInt(configProps.getProperty("num.workers"));
        conf.setNumWorkers(numWorkers);
        //StormSubmitter.submitTopology(topologyName, conf, builder.createTopology());

        //... locally :)
        localCluster.submitTopology(topologyName, conf, builder.createTopology());

    }	
}
