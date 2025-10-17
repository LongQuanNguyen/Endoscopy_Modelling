/**
 * OperatingRoomParser
 */	
public class OperatingRoomParser extends endoscopy.AbstractParser implements Serializable 
{
    public OperatingRoomParser(Main main, Province province) 
    {
    	super(main, province);
    }
    
    /**
     * Header enum for operating room file columns.
     */
    public enum Header implements ParserHeader 
    {
        OR_ID("or_id", false),
        ROOM_TYPE("room_type", true),
        TURNOVER_TIME("turnover_time", true);

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
        public boolean optional() {
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