public class AnalysisForList {

    private int[] array = new int[] {1,2,3};

    public void testFor() {
        for (int i : array) {
            System.out.println(i);
        }
    }

    public void testForIndex() {
        for (int i=0;i<array.length;i++) {
            System.out.println(array[i]);
        }
    }

    public void testForIndex01() {
        int len = array.length;
        for (int i=0;i<len;i++) {
            System.out.println(array[i]);
        }
    }

}
