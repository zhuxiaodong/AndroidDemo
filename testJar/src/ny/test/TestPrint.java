package ny.test;

import com.example.api.TestJni;

public class TestPrint implements TestJni
{
    public TestPrint()
    {
        System.out.println("create : hello jar!");
    }

    public void testPrint(final String str)
    {
        System.out.println(str);
    }

    public void testPrint()
    {
        System.out.println("hello jar!");
    }

    static public void TryTestPrint()
    {
        System.out.println("static hello jar!");
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        TryTestPrint();
        TestPrint tp = new TestPrint();
        tp.testPrint();
        tp.testPrint("good!");
    }

    @Override
    public void testJniPrint(String tag)
    {
        System.out.println(tag + ":" + "interface");
    }

}
