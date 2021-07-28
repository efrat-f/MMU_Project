package main.test.com.hit.memory;


import com.google.gson.Gson;
import main.java.com.hit.algorithm.IAlgoCache;
import main.java.com.hit.algorithm.LRUAlgoCacheImpl;
import main.java.com.hit.dao.DaoFileImpl;
import main.java.com.hit.dao.IDao;
import main.java.com.hit.dm.DataModel;
import main.java.com.hit.memory.CacheUnit;
import main.java.com.hit.server.Request;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class CacheUnitTest {
    private static IDao dao1;
    private static  DataModel<String> dataModels1;
    private static  DataModel<String> dataModels2;
    private static  DataModel<String> dataModels3;
    private static  DataModel<String> dataModels4;

    @BeforeClass
    public static void beforeTest() throws FileNotFoundException {

        dao1 = new DaoFileImpl<String>("hardMemory.json");
        dataModels1 = new DataModel<String>(1L, "page1");
        dataModels2 = new DataModel<String>(2L, "page2");
        dataModels3 = new DataModel<String>(3L, "page3");
        dataModels4 = new DataModel<String>(4L, "page4");
    }

    @Test
    public void testDaoFileImpl() throws IOException {
        dao1.delete(dataModels1);
        Assert.assertNull(dao1.find(dataModels1.getId()));
        dao1.delete(dataModels2);
        Assert.assertNull(dao1.find(dataModels2.getId()));
        dao1.delete(dataModels3);
        Assert.assertNull(dao1.find(dataModels3.getId()));
        dao1.delete(dataModels4);
        Assert.assertNull(dao1.find(dataModels4.getId()));

        dao1.save(dataModels1);
        Assert.assertNotNull(dao1.find(dataModels1.getId()));
        dao1.save(dataModels2);
        Assert.assertNotNull(dao1.find(dataModels2.getId()));
        dao1.save(dataModels3);
        Assert.assertNotNull(dao1.find(dataModels3.getId()));
        dao1.save(dataModels4);
        Assert.assertNotNull(dao1.find(dataModels4.getId()));
    }
    @Test
    public void testCacheUnit(){
        IAlgoCache<Long, DataModel<String>> iAlg = new LRUAlgoCacheImpl<>(3);
        CacheUnit<String> cache = new CacheUnit<String>(iAlg);
        DataModel<String> dataArray[] = new DataModel[5];
        dataArray[0] = new DataModel<String>(0L, "0");
        dataArray[1] = new DataModel<String>(1L, "1");
        dataArray[2] = new DataModel<String>(2L, "2");
        dataArray[3] = new DataModel<String>(3L, "3");
        dataArray[4] = new DataModel<String>(4L, "4");
        Long ids[] = new Long[2];
        ids[0] = 2L;
        ids[1] = 4L;
        Assert.assertEquals(cache.putDataModels(dataArray)[3], dataArray[0]);
        Assert.assertEquals(cache.getDataModels(ids)[0], dataArray[2]);

    }
    @Test
    public void testC() {

        Map<String, String> headerReq = new HashMap<>();
        headerReq.put("action", "DELETE");
        DataModel<String>[] dmArray = new DataModel[]{new DataModel<String>(4L, "page42"),new DataModel<String>(9L, "ayala")};

        Request<DataModel<String>[]> req = new Request<>(headerReq, dmArray);

        Gson gson =new Gson();
        try {
            System.out.println("start");
            Socket socket = new Socket("127.0.0.1", 12345);
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            System.out.println("start2");
            writer.writeUTF(gson.toJson(req));
            writer.flush();
            DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String content = "";
            do{
                System.out.println("start");
                content = input.readUTF();
                sb.append(content);
            }
            while (input.available() != 0);
            content = sb.toString();
            String response = "true";
            //Type requestType = new TypeToken<DataModel<String>[]>() {
            //}.getType();
            //DataModel<String>[] request = new Gson().fromJson(content, requestType);
            //response = new Gson().fromJson(content);
            System.out.println("message from server: " + content);


        } catch ( IOException e) {
            e.printStackTrace();
        }

    }
}

