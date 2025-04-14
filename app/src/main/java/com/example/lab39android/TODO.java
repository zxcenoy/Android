package com.example.lab39android;

import android.os.FileUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TODO {
    private String Title;
    private String Text;

    public void setTitle(String title) {
        Title = title;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }


    public String getTitle(){
        return  Title;
    }

    public TODO(String test1, String test2){
        this.Title = test1;
        this.Text = test2;
    }
    public static String toString(TODO todo){
        return todo.getTitle()+","+todo.getText();
    }
    public static TODO fromString(String str){
        String[] parrts = str.split(",");
            return new TODO(parrts[0], parrts[1]);
    }

}
