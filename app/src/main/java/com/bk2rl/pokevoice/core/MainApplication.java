package com.bk2rl.pokevoice.core;

import android.app.Application;
import android.util.Log;

import com.bk2rl.pokevoice.entity.SoundItem;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainApplication extends Application {

    public static final int MAX_POCKEMON_ID = 151;
//    private static Belvedere audioChooser;
//    private static Belvedere imageChooser;
    private static List<SoundItem> soundItems;
    private static Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
//        audioChooser = Belvedere.from(this)
//                .withContentType("audio/*")
//                .build();
//        imageChooser = Belvedere.from(this)
//                .withContentType("image/*")
//                .build();
        gson = new Gson();
        loadList();
        if (soundItems == null) {
            soundItems = new ArrayList<>();
        }
    }

    private void loadList() {
        try {
            InputStreamReader in = new InputStreamReader(getAssets().open("songs.list"),"UTF-8");
            JsonReader jsonReader = gson.newJsonReader(in);
//            gson.fromJson(jsonReader,SoundItemArrayList.class);
//            SoundItemArrayList soundItemArrayList = gson.fromJson(jsonReader, SoundItemArrayList.class);
//            jsonReader.beginObject();
            jsonReader.beginArray();
            soundItems = Arrays.asList(new SoundItem[MAX_POCKEMON_ID]);
            while(jsonReader.hasNext()) {
                SoundItem soundItem = (SoundItem) gson.fromJson(jsonReader, SoundItem.class);
                if  (soundItem.getId() == 21){
                    Log.d("Wrongp","Pause");
                }
                if (soundItem.getId() > 151)
                    Log.d("Wrongp","Wrong pockemon; id: " + soundItem.getId() + " name: " + soundItem.getLabel());
                else {
                    String label;
                    switch (Locale.getDefault().toString()) {
                        case "de_DE":
                            label = soundItem.getGerman();
                            break;
                        case "fr_FR":
                            label = soundItem.getFrench();
                            break;
                        case "uk_UA":
                            label = soundItem.getRussian();
                            break;
                        case "ru_RU":
                            label = soundItem.getRussian();
                            break;
                        default:
                            label = soundItem.getLabel();
                    }
                    soundItem.setLabel(label);
                    soundItems.set(soundItem.getId() - 1, soundItem);
                }
            }
//            jsonReader.endObject();
            jsonReader.endArray();
            jsonReader.close();

            for (int i = 0; i < soundItems.size(); i++){
                if (soundItems.get(i) == null) {
                    Log.d("Wrongp", String.format("Missing pokemon: id=%s", String.valueOf(i + 1)));
                    soundItems.set(i,new SoundItem());
                }
            }
//            if (soundItemArrayList != null) {
//                soundItems = soundItemArrayList.getSoundItems();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveList() {
        try {
            File file = new File(getApplicationContext().getFilesDir(), "songs.list");
            FileWriter jsonWrite = new FileWriter(file);
            jsonWrite.write(gson.toJson(soundItems));
            jsonWrite.flush();
            jsonWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static Belvedere getAudioChooser() {
//        return audioChooser;
//    }

//    public static Belvedere getImageChooser() {
//        return imageChooser;
//    }

    public static List<SoundItem> getSoundItems() {
        return soundItems;
    }
}
