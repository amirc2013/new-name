package bgu.spl.app;

import bgu.spl.app.Json.ExecutionInfo;
import bgu.spl.app.Passive.Store;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ShoeStoreRunner {

    public static void main(String[] args){
        if(args == null || args.length == 0)
            throw new RuntimeException("Expected Gson File Name");

        String gsonFilePath = args[0];
        Gson gson = new Gson();
        try {
            ExecutionInfo executionInfo = gson.fromJson(new FileReader(gsonFilePath), ExecutionInfo.class);
            execute(executionInfo);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Gson file (%s) not found !",gsonFilePath));
        }
    }

    private static void execute(ExecutionInfo executionInfo) {

    }
}
