/**
 * PatientParser
 */	
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PatientParser extends endoscopy.AbstractParser implements Serializable 
{

    public PatientParser(Main main, Province province) 
    {
    	super(main, province);
    }
    
    /**
     * Header enum for patient file columns.
     */
    public enum Header implements ParserHeader 
    {
        PATIENT_ID("patient_id", false),
        NAME("name", true),
        SCHEDULED_DATETIME("scheduled_datetime", false),
        PROCEDURE("procedure", false),
        PREFERRED_SURGEON("preferred_surgeon", true),
        PRIORITY("priority", true);

        private final String base;
        private final boolean optional;
        Header(String base, boolean optional) 
        { 
        	this.base = base; 
        	this.optional = optional; 
        	}
        public String baseName()
        {
        	return base;
        }
        public boolean optional() 
        { 
        	return optional; 
        }
    }
    
    @Override
    protected Class<Header> getHeaderEnum() 
    {
        return Header.class;
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