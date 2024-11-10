package uk.ac.sheffield.team28.team28.service;

import uk.ac.sheffield.team28.team28.repository.MiscRepository;

public class MiscSerivce {

    private final MiscRepository miscRepository;

    public MiscSerivce(MiscRepository miscRepository){
        this.miscRepository = miscRepository;
    }
}
