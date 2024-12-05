package uk.ac.sheffield.team28.team28.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.sheffield.team28.team28.dto.InstrumentDto;
import uk.ac.sheffield.team28.team28.dto.MusicDto;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;
import uk.ac.sheffield.team28.team28.service.InstrumentService;
import uk.ac.sheffield.team28.team28.service.MemberService;
import uk.ac.sheffield.team28.team28.service.MusicService;

import java.util.Arrays;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private InstrumentService instrumentService;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private MusicService musicService;


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        //Get logged in member
        Member member = memberService.findMember();
        model.addAttribute("memberType", member.getMemberType().toString());

        //If member is a committee member, get all instruments
        if (member.getMemberType() == MemberType.Committee || member.getMemberType() == MemberType.Director){

            List<Instrument> instruments = instrumentRepository.findAll();
            model.addAttribute("instruments", instruments);

            //Get all music
            List<Music> music = musicRepository.findAll();
            model.addAttribute("musics", music);

            //Get band types
            List<BandInPractice> bands = Arrays.asList(BandInPractice.values());
            model.addAttribute("bands", bands);

        } else if (member.getMemberType() == MemberType.Adult){

            //Get music based on band
            BandInPractice band = member.getBand();
            if (band != BandInPractice.None){
                List<Music> music =
                        musicRepository.findByBandInPracticeOrBandInPractice(band, BandInPractice.Both);
                model.addAttribute("musics", music);
            }
        }

        return "dashboard";
    }

    @PostMapping("/addInstrument")
    public String addInstrument(@ModelAttribute("instrument") InstrumentDto dto){
        instrumentService.saveInstrument(dto);
        return "redirect:/dashboard";
    }

    @PostMapping("/editInstrument")
    public String editInstrument(@ModelAttribute("instrument") InstrumentDto dto){
        instrumentService.updateInstrument(dto);
        return "redirect:/dashboard";
    }

    @PostMapping("/deleteInstrument")
    public String deleteInstrument(@RequestParam("instrumentId") Long id) {
        instrumentService.deleteInstrument(id);
        return "redirect:/dashboard";
    }

    @PostMapping("/addMusic")
    public String addMusic(@ModelAttribute("music") MusicDto dto){
        System.out.println("Band Type: " + dto.getBandInPractice());
        musicService.saveMusic(dto);
        return "redirect:/dashboard";
    }

}
