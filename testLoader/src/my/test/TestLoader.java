package my.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.example.api.TestJni;

public class TestLoader
{
    static private String URL = "http://bjrd.mydlink.com/fw/TestForAndroid/testJar.jar";
    static private String JARFILE = "file:///d:/tmp/testJar.jar";

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        System.out.println("start");

        loadJar(URL);
        loadJar(JARFILE);
    }

    static private void loadJar(String jar)
    {
        System.out.println("start load from " + jar);
        try
        {
            URL url = new URL(jar);
            URLClassLoader loader = new URLClassLoader(new URL[] { url });

            Class<?> c = loader.loadClass("ny.test.TestPrint");

            // c.getm

            Object o = c.newInstance();
            ((TestJni) o).testJniPrint("test");

            loader.close();

        } catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
