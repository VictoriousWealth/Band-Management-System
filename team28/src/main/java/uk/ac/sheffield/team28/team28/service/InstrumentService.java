package uk.ac.sheffield.team28.team28.service;

import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;

public class InstrumentService {

    private final InstrumentRepository instrumentRepository;

    public InstrumentService(InstrumentRepository instrumentRepository){
        this.instrumentRepository = instrumentRepository;
    }
}
