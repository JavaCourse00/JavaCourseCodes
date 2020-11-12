import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author tn
 * @version 1
 * @ClassName MainTest
 * @description
 * @date 2020/11/8 15:13
 */
public class MainTest {
    public static void main(String[] args) {
        String hash="";
        String txt ="39";

        // MD2
        for(int i=0;i<200000000;i++){
            ///   public static byte[] md2(String data) {
            //        return md2(StringUtils.getBytesUtf8(data));
            //    }
            hash = DigestUtils.md2Hex(txt);
        }

        System.out.println(hash);

    }

}
