/**
 * Utils
 */	
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Utils
{
	private static Main main;
    
    /**
     * Returns a new SimpleDateFormat for formatting dates without 'T' and 'Z'.
     * Format: "yyyy-MM-dd H:mm:ss"
     * Each call returns a new instance for thread safety.
     */
    public static SimpleDateFormat getDateFormatWithoutTAndZ () 
    {
        return new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
    }

    /**
     * Returns a new SimpleDateFormat for formatting dates with 'T' and 'Z'.
     * Format: "yyyy-MM-dd'T'H:mm:ss'Z'"
     * Each call returns a new instance for thread safety.
     */
    public static SimpleDateFormat getDateFormatWithTAndZ () 
    {
        return new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ss'Z'");
    }

    /**
     * Formats a Date object as a string without 'T' and 'Z'.
     * Uses a new SimpleDateFormat instance for thread safety.
     * @param date The Date to format
     * @return Formatted date string ("yyyy-MM-dd H:mm:ss")
     */
    public static String formatDateWithoutTAndZ(Date date) 
    {
        return getDateFormatWithoutTAndZ().format(date);
    }

    /**
     * Formats a Date object as a string with 'T' and 'Z'.
     * Uses a new SimpleDateFormat instance for thread safety.
     * @param date The Date to format
     * @return Formatted date string ("yyyy-MM-dd'T'H:mm:ss'Z'")
     */
    public static String formatDateWithTAndZ(Date date) 
    {
        return getDateFormatWithTAndZ().format(date);
    }
    
    /**
     * Prints a warning message to the console if warnings are enabled.
     * Use this for non-fatal issues or data problems that do not stop the model,
     * but should be brought to the user's attention (e.g., skipped rows, missing optional data).
     */
    public static void printWarning(String msg)
    {
        if (main != null && main.isPrintWarnings)
            main.traceln(red, "WARNING (time " +String.format("%.2f", main.time())+ "): " + msg);
    }

    /**
     * Prints a debug message to the console if debug output is enabled.
     * Use this for detailed internal state or troubleshooting information
     * that is only relevant during development or debugging.
     */
    public static void printDebug(String msg)
    {
        if (main != null && main.isPrintingDebugs)
            main.traceln(orange, "DEBUG (time " + String.format("%.2f", main.time()) + "): " + msg);
    }

    /**
     * Prints an update message to the console if updates are enabled.
     * Use this for high-level progress updates or milestones in the model run,
     * such as starting or finishing a major step.
     */
    public static void printUpdate(String msg)
    {
        if (main != null && main.isPrintingUpdates)
            main.traceln(green, "UPDATE (time " + String.format("%.2f", main.time()) + "): " + msg);
    }
    
    /**
     * Removes existing leading and trailing single or double quotes from a string.
     * @param string
     */
    public static String removeQuotes(String string)
    {
        if (string == null || string.length() < 2) return string;
        if ((string.startsWith("'") && string.endsWith("'")) || (string.startsWith("\"") && string.endsWith("\"")))
            return string.substring(1, string.length() - 1);
        return string;
    }
    
    /**
     * Converts date string to date in without TZ format.
     * Removes quotes if exist before parsing.
     * @param dateString
     * @return date without TZ
     */
    public static Date dateFromString(String dateString) 
    {
        dateString = removeQuotes(dateString);
        if (dateString == null || dateString.isEmpty()) return null;
        String removedTZ = dateString.replace("T", " ").replace("Z", "").replace("'", "");
        try 
        {
        	// SimpleDateFormat is not thread-safe, use local instance for safety.
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
            if (dateString.length() == 10) 
                return sdf.parse(removedTZ + " 00:00:00");
            return sdf.parse(removedTZ);
        } 
        catch (ParseException pe) 
        {
            printDebug("Exception converting string date '" + dateString + "' to date");
            throw new DataValidationException("Failed to parse date string: '" + dateString + "' - " + pe.getMessage());
        }
    }
    
    /**
     * Converts a string to int, with warning and error handling.
     * Removes quotes if they exist before parsing.
     * Returns 0 if the string is empty or "NA".
     * Throws DataValidationException if the string cannot be parsed as an integer.
     * @param intString The string to parse
     * @return int value, or 0 if empty or "NA"
     * @throws DataValidationException if the string cannot be parsed as an integer
     */
    public static int parseInteger(String intString)
    {
    	intString = removeQuotes(intString);
        if (intString == null || intString.isEmpty() || "NA".equals(intString))
            return 0;
        try
        {
            return Integer.parseInt(intString);
        }
        catch (NumberFormatException e)
        {
            printDebug("Exception parse int string " +intString);
            throw new DataValidationException(e.toString());
        }
    }
    
    /**
     * Converts a string to double, with warning and error handling.
     * Removes quotes if they exist before parsing.
     * Returns 0.0 if the string is empty or "NA".
     * Throws DataValidationException if the string cannot be parsed as a double.
     * @param doubleString The string to parse
     * @return double value, or 0.0 if empty or "NA"
     * @throws DataValidationException if the string cannot be parsed as a double
     */
    public static double parseDouble(String doubleString)
    {	
    	doubleString = removeQuotes(doubleString);
        if (doubleString == null || doubleString.isEmpty() || "NA".equals(doubleString))
            return 0.0;
        try
        {
            return Double.parseDouble(doubleString);
        }
        catch (NumberFormatException e)
        {
            printDebug("Exception parse double string " +doubleString);
            throw new DataValidationException(e.toString());
        }
    }
    
    /**
     * Finds the index of a column name in a row of column names (list of strings).
     * Throws a ColumnNotFoundException if the column name is not found.
     * @param rowOfColNames List of column names
     * @param colName The column name to find
     * @return The index of the column name in the row
     * @throws ColumnNotFoundException if the column name is not found in the list
     */
    public static int getColumnIndexError(List<String> rowOfColNames, String colName)
    {
        int colIndex = rowOfColNames.indexOf(colName);
        if (colIndex < 0)
        {
        	throw new ColumnNotFoundException("Can't find col name " + colName + " in row: " +rowOfColNames);
        }
        return colIndex;
    }
    
    /**
     * Finds the index of a column name in a row of column names (list of strings).
     * Returns -1 if the column name is not found.
     * @param row List of column names
     * @param colName The column name to find
     * @return The index of the column name in the row
     */
    public static int getColumnIndex(List<String> rowOfColNames, String colName)
    {
        return rowOfColNames.indexOf(colName);
    }
    
    /**
     * Converts a string to upper case and replaces spaces and dashes with underscores.
     * Also replaces " - " with "_".
     * @param string The input string
     * @return The formatted string
     */
    public static String toUpperUnderscore(String string)
    {
        if (string == null) return null;
        return string.toUpperCase()
                     .replaceAll(" - ", "_")
                     .replaceAll(" ", "_")
                     .replaceAll("-", "_");
    }

    /**
     * Checks if a string is contained in an array (case-sensitive).
     * @param value The string to check
     * @param array The array to search
     * @return true if value is found in array, false otherwise
     */
    public static boolean isStringInArray(String value, String[] array)
    {
        for (String s : array)
        {
            if (s.equals(value))
                return true;
        }
        return false;
    }
    
    /**
     * Retrieves the value from the specified header in the given row.
     * @param row The data row
     * @param headers The list of column headers
     * @param headerName The name of the column to retrieve
     * @return The value at the specified header, or null if the header is not present.
     */
    public static String getHeaderValueInRow(String[] row, List<String> headers, String headerName) 
    {
        int headerIndex = Utils.getColumnIndex(headers, headerName);
        if (headerIndex == -1) return null;
        return row[headerIndex].trim();
    }
    
    /**
     * Checks if the specified header in the given row matches the provided value.
     * @param row The data row
     * @param headers The list of column headers
     * @param headerName The name of the column to check
     * @param value The value to check for in the column
     * @return true if the header value matches the specified value, false otherwise (including if the header is not present).
     */
    public static boolean matchesHeaderValueInRow(String[] row, List<String> headers, String headerName, String value) 
    {
        String valueAtHeaderIndex = getHeaderValueInRow(row, headers, headerName);
        return value.equals(valueAtHeaderIndex);
    }
    
    /**
     * Cleans a line by removing leading non-printable characters such as BOM.
     * @param line The input string to clean
     * @return The cleaned string, or empty string if input is null
     */
    public static String cleanLine(String line)
    {
        if (line == null) return "";
        return line.replaceFirst("^[^\\x20-\\x7E]+", "");
    }
}