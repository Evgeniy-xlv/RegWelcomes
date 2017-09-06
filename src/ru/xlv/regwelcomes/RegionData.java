package ru.xlv.regwelcomes;

/**
 * Created by Xlv on 06.09.2017.
 */
public class RegionData {

    public String name;
    public String welcomeMessage;
    public String welcomeTitle;
    public String welcomeSubtitle;
    public String byeMessage;
    public String byeTitle;
    public String byeSubtitle;
    public String soundName;
    public int songID = -1;
    public int fadeIn = 10;
    public int fadeOut = 20;
    public int stay = 30;

    public RegionData(String name){
        this.name = name;
    }
}
