package uk.ac.sheffield.team28.team28.model;

public enum MemberType {
    Adult,
    Non_Adult,
    Committee,
    Director;
    
    @Override
    public String toString() {
        return switch (this) {
            case Non_Adult -> "Non-adult";
            case Adult -> "Adult";
            case Committee -> "Committee";
            case Director -> "Director";
            default -> throw new IllegalArgumentException("Unexpected value: " + this);
        };
    }
    
    static MemberType fromString(String s) {
        return switch (s) {
            case "Non-Adult" -> Non_Adult;
            case "Adult" -> Adult;
            case "Committee" ->  Committee;
            case "Director" -> Director;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
