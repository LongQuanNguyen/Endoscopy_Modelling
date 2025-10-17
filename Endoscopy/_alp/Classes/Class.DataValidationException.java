/**
 * DataValidationException
 * 
 * Exception thrown to indicate an error in the input data during file parsing or validation,
 * before the model run time. Used to separate data issues from model runtime errors.
 */	
public class DataValidationException extends RuntimeException 
{
	/**
	* @param message The error message.
	*/
    public DataValidationException(String message) 
    {
        super(message);
    }
}