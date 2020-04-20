package com.wk.demo.cmds;

import com.wk.demo.util.CommConfig;
import com.wk.demo.util.ProducerUtil;
import com.wk.demo.util.ReadContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class getcmd {
    private static CommConfig config = CommConfig.Instance();
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        getParam(args);
        String file = args[0];
        int count = Integer.parseInt(args[1]);
        String kafkaTopic = config.getKafkaTopic();

        ReadContent readContent = new ReadContent(file);
        List<String> contents = readContent.getContent();

        Producer<String, String> producer = ProducerUtil.getProducer();
        for (int i = 1; i <= count; i++) {
            sendContent(contents, producer, kafkaTopic);
        }
    }

    private static void sendContent(List<String> cons, Producer<String, String> producer, String topic) throws ExecutionException, InterruptedException {
        for (String con : cons) {
            log.info("send msg:{}, topic:{}", con, topic);
            producer.send(new ProducerRecord<>(topic, null, con));
            producer.flush();
            /*Future<RecordMetadata> future = producer.send(new ProducerRecord<>(topic, null, con));
            long offset = future.get().offset();
            log.info("offset :{}", offset);*/
        }
    }

    private static void getParam(String[] args) {
        if (args.length < 2){
            log.info("./a.out filepath count");
            System.exit(1);
        }
    }
}
