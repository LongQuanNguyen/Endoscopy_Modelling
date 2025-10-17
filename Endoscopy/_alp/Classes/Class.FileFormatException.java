/**
 * FileFormatException
 * 
 * Exception thrown when the input file format is invalid or empty during parsing,
 * before the model run time.
 */	
public class FileFormatException extends RuntimeException 
{
	/**
	 * @param message The error message.
	 */
    public FileFormatException(String message) 
    {
        super(message);
    }
}