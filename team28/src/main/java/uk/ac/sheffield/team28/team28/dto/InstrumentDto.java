package uk.ac.sheffield.team28.team28.dto;

import jakarta.validation.constraints.NotBlank;

public class InstrumentDto {

    @NotBlank(message = "Instrument cannot be empty.")
    private String instrumentInput;
    @NotBlank(message = "Make cannot be blank.")
    private String make;
    @NotBlank(message = "Serial number cannot be blank.")
    private String serialNumber;
    private boolean inStorage;
    private String note;

    //Constructors
    public InstrumentDto(){}

    public InstrumentDto(String instrumentInput, String make, String serialNumber, Boolean inStorage,
                         String note){
        this.instrumentInput = instrumentInput;
        this.make = make;
        this.serialNumber = serialNumber;
        this.inStorage = inStorage;
        this.note = note;

    }

    //Getters and setters

    public String getInstrumentInput() {
        return instrumentInput;
    }

    public void setInstrumentInput(String instrumentInput) {
        this.instrumentInput = instrumentInput;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean getInStorage() {
        return inStorage;
    }

    public void setInStorage(Boolean inStorage) {
        this.inStorage = inStorage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
