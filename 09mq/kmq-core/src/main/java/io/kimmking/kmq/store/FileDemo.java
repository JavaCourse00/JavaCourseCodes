package io.kimmking.kmq.store;

import com.alibaba.fastjson.JSON;
import io.kimmking.kmq.core.KmqMessage;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/13 下午9:59
 */
public class FileDemo {

    public static void main(String[] args) throws IOException {

        String content = "this is a good file.\r\n" +
                "that is a new line.\r\n";
        String topic = "topicA";
        System.out.println(content.length());

        File file = new File("store001.dat");
        if (!file.exists()) {
            file.createNewFile();
        }
        Path path = Paths.get(file.toURI());
        try(FileChannel channel = (FileChannel) Files.newByteChannel(path,
                StandardOpenOption.READ,
                StandardOpenOption.WRITE)) {

            MappedByteBuffer mappedByteBuffer = channel
                    .map(FileChannel.MapMode.READ_WRITE, 0, 10240);

            if (mappedByteBuffer != null) {

                System.out.println(Charset.forName("utf-8")
                        .decode(mappedByteBuffer.asReadOnlyBuffer()));

                for (int i = 0; i < 100; i++) {
                    KmqMessage<String> km = KmqMessage.from(topic, content);
                    String message = encodeMessage(km);
                    Indexer.addEntry(topic, km.getId(), mappedByteBuffer.position(), message.length());
                    int pos = write(mappedByteBuffer, message);
                    System.out.println("POS = " + pos);
                }

                System.out.println(" ======== indexer ========= ");
                System.out.println(Indexer.getEntries(topic));
            }

            ByteBuffer readOnlyBuffer = mappedByteBuffer.asReadOnlyBuffer();
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals("exit")) {
                    break;
                }
                System.out.println("IN = "+line);
                Long id = Long.valueOf(line);
                Indexer.Entry entry = Indexer.getEntry(id);
                System.out.println("EN = " + entry);
                if(entry == null) {
                    System.out.println("!!!No entry for id=" + id);
                } else {
                    readOnlyBuffer.position(entry.offset);
                    byte[] bytes = new byte[entry.length];
                    readOnlyBuffer.get(bytes, 0, entry.length);
                    System.out.println("MSG = " + new String(bytes));
                }
            }

        }
    }

    private static String encodeMessage(KmqMessage<String> message) {
        return JSON.toJSONString(message);
    }

    public static int write(MappedByteBuffer buffer, String content) throws IOException {
        buffer.put(
                Charset.forName("utf-8")
                        .encode(content));
        return buffer.position();
    }

}
