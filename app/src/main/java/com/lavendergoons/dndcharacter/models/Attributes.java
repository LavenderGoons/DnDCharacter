package com.lavendergoons.dndcharacter.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Attributes implements Parcelable {

    private String name;
    private String clazz;
    private int level;
    private int xp;

    private String race;
    private String alignment;
    private String deity;
    private String size;

    private int age;
    private String gender;
    private float height;
    private float weight;
    private String eyes;
    private String hair;
    private String skin;

    public Attributes() {

    }

    public Attributes(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public Attributes(Parcel parcel) {
        this.name = parcel.readString();
        this.clazz = parcel.readString();
        this.level = parcel.readInt();
        this.xp = parcel.readInt();
        this.race = parcel.readString();
        this.alignment = parcel.readString();
        this.deity = parcel.readString();
        this.size = parcel.readString();
        this.age = parcel.readInt();
        this.gender = parcel.readString();
        this.height = parcel.readFloat();
        this.weight = parcel.readFloat();
        this.eyes = parcel.readString();
        this.hair = parcel.readString();
        this.skin = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.clazz);
        dest.writeInt(this.level);
        dest.writeInt(this.xp);
        dest.writeString(this.race);
        dest.writeString(this.alignment);
        dest.writeString(this.deity);
        dest.writeString(this.size);
        dest.writeInt(this.age);
        dest.writeString(this.gender);
        dest.writeFloat(this.height);
        dest.writeFloat(this.weight);
        dest.writeString(this.eyes);
        dest.writeString(this.hair);
        dest.writeString(this.skin);
    }

    public static final Parcelable.Creator<Attributes> CREATOR = new Parcelable.Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel parcel) {
            return new Attributes(parcel);
        }

        @Override
        public Attributes[] newArray(int i) {
            return new Attributes[i];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getDeity() {
        return deity;
    }

    public void setDeity(String deity) {
        this.deity = deity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    @Override
    public String toString() {
        return "Attributes{" +
                "name='" + name + '\'' +
                ", clazz='" + clazz + '\'' +
                ", level=" + level +
                ", xp=" + xp +
                ", race='" + race + '\'' +
                ", alignment='" + alignment + '\'' +
                ", deity='" + deity + '\'' +
                ", size='" + size + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", eyes='" + eyes + '\'' +
                ", hair='" + hair + '\'' +
                ", skin='" + skin + '\'' +
                '}';
    }
}
