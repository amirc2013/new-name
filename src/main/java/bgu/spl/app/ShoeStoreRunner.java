package bgu.spl.app;

import bgu.spl.app.Json.ExecutionInfo;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by matan_000 on 22/12/2015.
 */
public class ShoeStoreRunner {
    public static void main(String[] args){
        if(args == null || args.length == 0)
            throw new RuntimeException("Expected Gson File Name");

        String gsonFilePath = args[0];
        Gson gson = new Gson();
        try {
            ExecutionInfo executionInfo = gson.fromJson(new FileReader(gsonFilePath), ExecutionInfo.class);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Gson file (%s) not found !",gsonFilePath));
        }
    }
}
