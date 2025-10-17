public interface ParserHeader {
    /**
     * canonical base name for the column (e.g. "type_" or "hsn")
     */
    String baseName();

    /**
     * true if the column is optional
     */
    boolean optional();
}