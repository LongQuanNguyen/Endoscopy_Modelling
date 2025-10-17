/**
 * SurgeonParser
 */	
public class SurgeonParser extends endoscopy.AbstractParser implements Serializable 
{
    public SurgeonParser(Main main, Province province) 
    {
    	super(main, province);
    }

    /**
     * Header enum for surgeon file columns.
     */
    public enum Header implements ParserHeader 
    {
        SURGEON_ID("surgeon_id", false),
        NAME("name", true),
        SKILLS("skills", true),
        SHIFT_START("shift_start", true),
        SHIFT_END("shift_end", true);

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