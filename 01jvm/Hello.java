public class Hello {
    public static void main(String[] args) {
        int num1 = 1;       // 字面量1;
        double num2 = 2.0D; // 大小写的D都可以
        long num3 = 3L;     // 大小写的L都可以, 加L是好习惯;
        byte num4 = 4;      // 可以直接赋予 [-128, 127] 范围内的字面量;
        if ("".length() < 10) {
            // 错误用法: num2 + num3 = 2.03
            System.out.println("错误用法: num2 + num3 = " + num2 + num3);
        }
        for (int i = 0; i < num1; i++) {
            // 四则运算: num1 * num4 = 4
            System.out.print("四则运算: num1 * num4 = ");
            System.out.println(num1 * num4);
        }
    }
}
