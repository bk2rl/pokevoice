package com.bk2rl.pokevoice.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SoundItem implements Serializable, Cloneable {
    private int id;
    private String imgSrc;
    private String label;
    private String audioSrc;
    private String russian;
    private String german;
    private String french;

    public int getId() {
        return id;
    }

    public String getRussian() {
        return russian;
    }

    public String getGerman() {
        return german;
    }

    public String getFrench() {
        return french;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public SoundItem setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public SoundItem setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getAudioSrc() {
        return audioSrc;
    }

    public void setAudioSrc(String audioSrc) {
        this.audioSrc = audioSrc;
    }

    @Override
    public SoundItem clone() throws CloneNotSupportedException {
        return (SoundItem) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoundItem soundItem = (SoundItem) o;

        if (id != soundItem.id) return false;
        if (imgSrc != null ? !imgSrc.equals(soundItem.imgSrc) : soundItem.imgSrc != null)
            return false;
        if (label != null ? !label.equals(soundItem.label) : soundItem.label != null) return false;
        if (audioSrc != null ? !audioSrc.equals(soundItem.audioSrc) : soundItem.audioSrc != null)
            return false;
        if (russian != null ? !russian.equals(soundItem.russian) : soundItem.russian != null)
            return false;
        if (german != null ? !german.equals(soundItem.german) : soundItem.german != null)
            return false;
        return french != null ? french.equals(soundItem.french) : soundItem.french == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (imgSrc != null ? imgSrc.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (audioSrc != null ? audioSrc.hashCode() : 0);
        result = 31 * result + (russian != null ? russian.hashCode() : 0);
        result = 31 * result + (german != null ? german.hashCode() : 0);
        result = 31 * result + (french != null ? french.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SoundItem{" +
                "imgSrc='" + imgSrc + '\'' +
                ", label='" + label + '\'' +
                ", audioSrc='" + audioSrc + '\'' +
                '}';
    }

    public static ArrayList<SoundItem> findElementsByLabel(ArrayList<SoundItem> elements, String findString) {
        ArrayList<SoundItem> soundItems = new ArrayList<>();
        for (SoundItem element : elements) {
            if (element.label.contains(findString)) {
                soundItems.add(element);
            }
        }
        return soundItems;
    }

}
