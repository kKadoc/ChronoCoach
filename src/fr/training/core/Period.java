package fr.training.core;

public class Period {

    private long duration;
    private int styleIndex;
    private String description;

    public long getDuration() {
	return duration;
    }

    public void setDuration(long duration) {
	this.duration = duration;
    }

    public int getStyleIndex() {
	return styleIndex;
    }

    public void setStyleIndex(int styleIndex) {
	this.styleIndex = styleIndex;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Period(long duration, int styleIndex, String description) {
	super();
	this.duration = duration;
	this.styleIndex = styleIndex;
	this.description = description;
    }

}
