public interface Statistics {
	public String getBeginningXml(String dateTimeString);
	public Runnable getWriteHandler(String dateTimeString, File gpxFile, Location loc, boolean addNewTrackSegment);
	public void appendCourseAndSpeed(StringBuilder track, Location loc);
}