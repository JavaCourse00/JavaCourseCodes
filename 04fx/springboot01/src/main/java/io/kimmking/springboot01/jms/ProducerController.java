package io.kimmking.springboot01.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;

@RestController
public class ProducerController
{
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    
    @Autowired
    private Queue queue;
    
    @Autowired
    private Topic topic;
    
    
    // curl http://localhost:8080/queue/test -X POST -d "testququuquqq"
    @PostMapping("/queue/test")
    public String sendQueue(@RequestBody String str) {
        this.sendMessage(this.queue, str);
        return "success";
    }
    
    
    //curl http://localhost:8080/topic/test -X POST -d "testtopiccccc"
    @PostMapping("/topic/test")
    public String sendTopic(@RequestBody String str) {
        this.sendMessage(this.topic, str);
        return "success";
    }
    
    // 发送消息，destination是发送到的队列，message是待发送的消息
    private void sendMessage(Destination destination, final String message){
        jmsMessagingTemplate.convertAndSend(destination, message);
    }
}
