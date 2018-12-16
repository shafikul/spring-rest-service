package bd.com.sagar.rest.core.type;


public enum OperatorType  {
	LESS_THAN("lt"), LESS_THAN_EQUAL("lte"), GREATER_THAN("gt"), GREATER_THAN_EQUAL("gte"), EQUAL("eq"), NOT_EQUAL(
			"ne");

	private String name;

	OperatorType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static OperatorType valueOfByName(String name) {
		OperatorType entityType = null;
        OperatorType[] types = OperatorType.values();

        for (int idx = 0; idx < types.length && entityType == null; idx++) {
            if (types[idx].getName().equals(name)) {
                entityType = types[idx];
            }
        }
        return entityType;
    }
}
