package uk.ac.sheffield.team28.team28.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.service.MusicService;


@Controller()
@RequestMapping("/music")
public class MusicController {
    private MusicService musicService;
    @PostMapping("/{id}/put-into-practice")
    public ResponseEntity<String> putMusicIntoPractice(
            @PathVariable Long id,
            @RequestParam BandInPractice band) {
        musicService.putMusicIntoPractice(id, band);
        return ResponseEntity.ok("Music set to practice successfully!");
    }

    @PostMapping("/{id}/return-to-storage")
    public ResponseEntity<String> returnMusicToStorage(@PathVariable Long id) {
        musicService.returnMusicToStorage(id);
        return ResponseEntity.ok("Music returned to storage successfully!");
    }
}
