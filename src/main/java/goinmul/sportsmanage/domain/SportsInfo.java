package goinmul.sportsmanage.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class SportsInfo {

    private String initialName;
    private String icon;

    private Map<String,String> initialMap = new HashMap<>();
    private Map<String,String> iconMap = new HashMap<>();

    public SportsInfo() {
        initialMap.put("basketball", "B:A");
        initialMap.put("bowling", "B:A");
        initialMap.put("soccer", "S:A");

        initialMap.put("futsal", "F:A");
        initialMap.put("tennis", "T:A");

        initialMap.put("badminton", "B:A");
        initialMap.put("billiard", "B:A");
        initialMap.put("tableTennis", "P:A");

        iconMap.put("basketball", "<i class=\"fas fa-basketball-ball text-orange mr-2\" style=\"font-size: 1.5em; color: orange;\"></i>");
        iconMap.put("bowling", "<div style=\"color: red;\"><i class=\"fas fa-bowling-ball mr-2\" style=\"font-size: 1.5em;\"></i></div>");
        iconMap.put("soccer", "<i class=\"fas fa-futbol mr-2\" style=\"font-size: 1.5em;\"></i>");

        iconMap.put("futsal", "<div style=\"color: white; font-size: 2em;\">⚽</div>");
        iconMap.put("tennis", "<div style=\"color: white; font-size: 2em;\">\uD83C\uDFBE</div>");

        iconMap.put("badminton", "<img src=\"/images/badminton-icon.png\" alt=\"셔틀콕\" style=\"width: 2em; height: 1.5em;\">");
        iconMap.put("billiard", "<img src=\"/images/billiard-ball-icon.png\" alt=\"당구공\" style=\"width: 1.5em; height: 1.5em;\">");
        iconMap.put("tableTennis", "<img src=\"/images/table-tennis-icon.png\" alt=\"탁구\" style=\"width: 2em; height: 1.5em;\">");

    }



}
