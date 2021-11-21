package com.avaliacao.meeting_agenda.type;

public enum VotingType {
	SIM("Sim"), NAO("Não");

	private final String text;

	/**
	 * @param text
	 */
	VotingType(final String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return text;
	}
	
	
	
    public static VotingType getEnum(String value) {
        for(VotingType v : values())
            if(v.text.equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }
}
