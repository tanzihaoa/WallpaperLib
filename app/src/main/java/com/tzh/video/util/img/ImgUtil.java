package com.tzh.video.util.img;

import com.tzh.video.base.MyApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.microshow.rxffmpeg.RxFFmpegCommandList;

public class ImgUtil {
    public static String[] getBoxblur(ArrayList<String> list) {
        RxFFmpegCommandList cmdlist = new RxFFmpegCommandList();
        for(String url : list){
            cmdlist.append("-i");
            cmdlist.append(url);
        }
        cmdlist.append("-filter_complex");
        cmdlist.append("[0:v][0:a][1:v][1:a]concat=n="+list.size()+":v=1:a=1[v][a]");
        cmdlist.append("-map");
        cmdlist.append("[v]");
        cmdlist.append("-map");
        cmdlist.append("[a]");
        cmdlist.append("-c:v");
        cmdlist.append("libx264");
        cmdlist.append("-crf");
        cmdlist.append("23");
        cmdlist.append("-preset");
        cmdlist.append("ultrafast");
        cmdlist.append(KtFileUtil.INSTANCE.getRandomVideoName());
        return cmdlist.build();
    }

    public static String[] toList(ArrayList<String> inputVideos, String outputVideo){
        // 构建concat协议命令
        String cmdBuilder = "-f concat -safe 0 -i " +
                getListText(inputVideos) +
                " -c copy output " + outputVideo;

        String[] cmd = cmdBuilder.split(" ");
        return cmd;
    }

    public static String getListText(ArrayList<String> videoPaths){
        String listFilePath = KtFileUtil.INSTANCE.getTextCacheFolder(MyApplication.mContext).getAbsolutePath();
        try {
            File listFile = new File(listFilePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(listFile));

            for (String videoPath : videoPaths) {
                writer.write("file '" + videoPath + "'\n");
            }
            writer.close();
            return listFilePath;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String[] getConcatCommand(List<String> videoPaths, String outputFilePath){
        try {
            File listFile = new File(KtFileUtil.INSTANCE.getVideoCacheFolder(MyApplication.mContext), "list.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(listFile));
            for (String videoPath : videoPaths) {
                writer.write("file '"+videoPath+"'\n");
            }
            writer.close();
            String cmd = "-f concat -safe 0 -i "+listFile.getAbsolutePath() +" -c copy "+outputFilePath;
            return cmd.split(" ");
        }catch (IOException e){

        }
        return null;
    }
}
