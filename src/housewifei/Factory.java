package housewifei;

public interface Factory {

    /**
     * Create objects from a pattern
     *
     * @param pattern pattern which correspond to the name of the object to be created
     * @return Object requested
     */
    Object create(String pattern);

}
