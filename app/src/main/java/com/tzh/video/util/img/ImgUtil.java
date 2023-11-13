package com.tzh.video.util.img;

import com.tzh.video.base.MyApplication;

import java.util.ArrayList;

import io.microshow.rxffmpeg.RxFFmpegCommandList;

public class ImgUtil {
    public static String[] getBoxblur(String imgPath1,String imgPath2) {
        RxFFmpegCommandList cmdlist = new RxFFmpegCommandList();
        cmdlist.append("-i");
        cmdlist.append(imgPath1);
        cmdlist.append("-i");
        cmdlist.append(imgPath2);
        cmdlist.append("-filter_complex");
        cmdlist.append("hstack");
        cmdlist.append("-c:v");
        cmdlist.append("libx264");
        cmdlist.append("-crf");
        cmdlist.append("23");
        cmdlist.append("-preset");
        cmdlist.append("ultrafast");
        cmdlist.append(imgPath2.replace(".mp4","123456.mp4"));
        return cmdlist.build();
    }

    public static String[] toList(ArrayList<String> inputVideos, String outputVideo){
        // 构建concat协议命令
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append("-i concat:");
        for (String inputVideo : inputVideos) {
            cmdBuilder.append(inputVideo).append("|");
        }
        cmdBuilder.deleteCharAt(cmdBuilder.length() - 1); // 删除最后一个"|"
        cmdBuilder.append(" -c copy ").append(outputVideo);

        String[] cmd = cmdBuilder.toString().split(" ");
        return cmd;
    }
}
