package uk.ac.sheffield.team28.team28.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.sheffield.team28.team28.dto.InstrumentDto;
import uk.ac.sheffield.team28.team28.model.Instrument;
import uk.ac.sheffield.team28.team28.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.service.InstrumentService;
import uk.ac.sheffield.team28.team28.service.MemberService;

import java.util.List;

@Controller // Use @Controller instead of @RestController for Thymeleaf views
@RequestMapping("/committee")
public class CommitteeController {

    private final MemberService memberService;
    private final InstrumentRepository instrumentRepository;
    private final InstrumentService instrumentService;

    public CommitteeController(MemberService memberService, InstrumentRepository instrumentRepository, InstrumentService instrumentService) {
        this.memberService = memberService;
        this.instrumentRepository = instrumentRepository;
        this.instrumentService = instrumentService;
    }


    @GetMapping()
    public String showCommitteeView(Model model) {
        Member member = memberService.findMember();
        model.addAttribute("member", member);
        model.addAttribute("memberType", member.getMemberType().toString());
        List<Instrument> instruments = instrumentRepository.findAll();
        model.addAttribute("instruments", instruments);
        return "committee";
    }

    @PostMapping("/addInstrument")
    public String addInstrument(@ModelAttribute("instrument") InstrumentDto dto){
        instrumentService.saveInstrument(dto);
        return "redirect:/committee";
    }

    @PostMapping("/editInstrument")
    public String editInstrument(@ModelAttribute("instrument") InstrumentDto dto){
        instrumentService.updateInstrument(dto);
        return "redirect:/committee";
    }

    @PostMapping("/deleteInstrument")
    public String deleteInstrument(@RequestParam("instrumentId") Long id) {
        instrumentService.deleteInstrument(id);
        return "redirect:/committee";
    }

}
