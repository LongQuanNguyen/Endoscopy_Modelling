/**
 * ColumnNotFoundException
 * 
 * Exception thrown when a required column is not found in the input file during parsing,
 * before the model run time. Used to distinguish file structure issues from model runtime errors.
 */	
public class ColumnNotFoundException extends RuntimeException 
{
	/**
	 * @param columnName The name of the missing column.
	 */
	public ColumnNotFoundException(String columnName) 
	{
	    super("Column not found: " + columnName);
	}
	
	/**
	 * @param columnName The name of the missing column.
	 * @param message The error message.
	 */
	public ColumnNotFoundException(String message, String columnName) 
	{
	    super(message + " (Column: " + columnName + ")");
	}
}