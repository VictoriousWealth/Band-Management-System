package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;

import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.Music;
import uk.ac.sheffield.team28.team28.model.MusicStatus;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;

@Service
public class MusicService {

    private final MusicRepository musicRepository;

    public MusicService(MusicRepository musicRepository){
        this.musicRepository = musicRepository;
    }

    public void putMusicIntoPractice(Long musicId, BandInPractice band) {
        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new IllegalArgumentException("Music not found"));
        
        music.setStatus(MusicStatus.IN_PRACTICE);
        music.setBandInPractice(band);
        musicRepository.save(music);
    }

    public void returnMusicToStorage(Long musicId) {
        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new IllegalArgumentException("Music not found"));

        music.setStatus(MusicStatus.STORAGE);
        music.setBandInPractice(BandInPractice.None);
        musicRepository.save(music);
    }
}
