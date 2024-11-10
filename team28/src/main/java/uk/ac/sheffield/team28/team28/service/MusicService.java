package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;

@Service
public class MusicService {

    private final MusicRepository musicRepository;

    public MusicService(MusicRepository musicRepository){
        this.musicRepository = musicRepository;
    }
}
