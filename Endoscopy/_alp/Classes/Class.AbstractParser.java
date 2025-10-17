/**
 * AbstractTsvParser
 */	

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public abstract class AbstractParser<H extends Enum<H> & ParserHeader> implements Serializable 
{
	protected final Main main;
    protected final Province province;

    protected AbstractParser(Main main, Province province) 
    {
        this.main = main;
        this.province = province;
    }
    
    protected abstract Class<H> getHeaderEnum();

    /**
     * Read the first line (header) from the given file and return a list of cleaned header names.
     * @param file The file name
     * @param delimiter The delimiter character ('\t' for TSV, ',' for CSV)
     * @return list of header names in column order (trimmed and cleaned)
     * @throws IOException if reading the file fails
     */
    protected List<String> readHeader(File file, char delimiter) throws IOException 
    {
        try (BufferedReader r = new BufferedReader(new FileReader(file))) 
        {
            String rawHeaderLine = r.readLine();
            if (rawHeaderLine == null) 
            	return Collections.emptyList();
            String cleaned = Utils.cleanLine(rawHeaderLine);
            String[] columns = cleaned.split(Pattern.quote(String.valueOf(delimiter)), -1);
            List<String> headerNames = new ArrayList<>(columns.length);
            for (String col : columns) 
            {
                String token = Utils.removeQuotes(col == null ? "" : col).trim();
                headerNames.add(token);
            }
            return headerNames;
        }
    }

    /**
     * Determine whether the given columnName matches a header enum constant.
     * @param header The header enum constant to test against
     * @param columnName Column name read from file header
     * @return true if columnName matches header, false otherwise
     */
    protected boolean headerMatches(H header, String columnName) 
    {
        if (columnName == null) 
        	return false;
        String cleanedColumnName = Utils.removeQuotes(columnName).trim();
        String base = header.baseName();
        if (cleanedColumnName.equals(base)) 
        	return true;
        if (base != null && base.endsWith("_"))
            return Pattern.matches("^" + Pattern.quote(base) + "\\d+$", cleanedColumnName);
        return false;
    }

    /**
     * Verify that all non-optional headers declared in headerEnum appear in headerNames.
     * @param headerNames list of column names from the file header (in order)
     * @param headerEnum  header enum class used by the concrete parser
     * @throws ColumnNotFoundException when required columns are missing
     */
    private void checkRequiredHeaders(List<String> headerNames, Class<H> headerEnum) 
    {
        List<String> missing = new ArrayList<>();
        for (H h : headerEnum.getEnumConstants()) 
        {
            if (h.optional()) 
            	continue;
            boolean found = false;
            for (String col : headerNames) 
            {
                if (headerMatches(h, col)) 
                { 
                	found = true; 
                	break; 
                	}
            }
            if (!found) 
            	missing.add(h.baseName());
        }
        if (!missing.isEmpty()) {
            throw new ColumnNotFoundException("Missing required columns: " + String.join(", ", missing));
        }
    }

    /**
     * Log a warning for any header names present in the file that do not match any enum constant.
     * In other word, log unsued columns.
     * @param headerNames list of column names from the file header (in order)
     * @param headerEnum  header enum class used by the concrete parser
     */
    private void warnUnusedHeaders(List<String> headerNames, Class<H> headerEnum) 
    {
        List<String> unused = new ArrayList<>();
        for (String col : headerNames) 
        {
            boolean matched = false;
            for (H h : headerEnum.getEnumConstants()) 
            {
                if (headerMatches(h, col)) 
                { 
                	matched = true; 
                	break; 
            	}
            }
            if (!matched && col != null && !col.trim().isEmpty()) 
            	unused.add(col);
        }
        if (!unused.isEmpty())
            Utils.printWarning("Unused columns: " + String.join(", ", unused));
    }
    
    /**
     * Read and validate file header, warn about unused columns.
     * @param file The input file
     * @param @param delimiter The delimiter character ('\t' for TSV, ',' for CSV)
     * @throws IOException on file read error
     * @throws ColumnNotFoundException if required headers are missing
     */
    protected void validateHeader(File file, char delimiter) throws IOException 
    {
    	Class<H> headerEnum = getHeaderEnum();
        List<String> headerNames = readHeader(file, delimiter);
        checkRequiredHeaders(headerNames, headerEnum);
        warnUnusedHeaders(headerNames, headerEnum);
    }
    
	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * This number is here for model snapshot storing purpose<br>
	 * It needs to be changed when this class gets changed
	 */ 
	private static final long serialVersionUID = 1L;

}