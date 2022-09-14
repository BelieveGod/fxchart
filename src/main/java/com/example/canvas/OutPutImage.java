package com.example.canvas;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author LTJ
 * @date 2022/9/2
 */
public class OutPutImage {
    private static final Double MEASURING_SCALE = (double) 1 / 10;
    private static final int radius=1;
    private static Color[] COLORS = {Color.yellow, Color.RED, Color.orange, Color.MAGENTA, Color.green, Color.pink};

    public static void main(String[] args) {
        BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics gc = bufferedImage.getGraphics();
        JSONObject jsonObject = readOutLine();
        List<Double[]> outLine = jsonObject.getObject("outLine",new TypeReference<List<Double[]>>(){});
        gc.setColor(Color.CYAN);

        for(int i=0;i<outLine.size()-1;i++){
            Double[] cordinate = outLine.get(i);
            Double[] cordinate2 = outLine.get(i+1);
            gc.drawLine(mm2pixel(cordinate[1]), mm2pixel(cordinate[0]), mm2pixel(cordinate2[1]), mm2pixel(cordinate2[0]));
        }

        JSONObject jsonObject1 = readSection();
        JSONObject cameraDataList = jsonObject1.getJSONObject("cameraDataList");
        int i=0;
        for (Map.Entry<String, Object> entry : cameraDataList.entrySet()) {
            int idx = i % COLORS.length;
            gc.setColor(COLORS[idx]);
            i++;
            JSONObject camera = (JSONObject) entry.getValue();
            List<Double[]> codinateList=camera.getObject("cordinateList",new TypeReference<List<Double[]>>(){});
            for (Double[] cordinate : codinateList) {
                int x = mm2pixel(cordinate[1]);
                int y = mm2pixel(cordinate[0]);

                gc.fillRect(x,y,radius,radius);
            }
        }


        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File deskTop = fileSystemView.getHomeDirectory();
        File jpg = FileUtil.file(deskTop, "outLine.jpg");
        try {
            ImageIO.write(bufferedImage, "jpg", jpg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("%s 图片输出完毕！", FileUtil.getCanonicalPath(jpg));
    }

    private static JSONObject readOutLine(){
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File deskTop = fileSystemView.getHomeDirectory();
        File outLineJson = FileUtil.file(deskTop, "outLine.json");
        String content = FileUtil.readUtf8String(outLineJson);
        JSONObject jsonObject = JSONObject.parseObject(content);
        return jsonObject;
    }

    private static int mm2pixel(double mm){
        double v = mm * MEASURING_SCALE;
        long round = Math.round(v);
        return (int) round;
    }

    private static JSONObject readSection(){
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        File deskTop = fileSystemView.getHomeDirectory();
        File outLineJson = FileUtil.file(deskTop, "\\转换数据文件夹\\section2.json");
        String content = FileUtil.readUtf8String(outLineJson);
        JSONObject jsonObject = JSONObject.parseObject(content);
        return jsonObject;
    }
}
