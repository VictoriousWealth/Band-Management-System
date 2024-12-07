package uk.ac.sheffield.team28.team28.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.sheffield.team28.team28.dto.InstrumentDto;
import uk.ac.sheffield.team28.team28.dto.MusicDto;
import uk.ac.sheffield.team28.team28.dto.OrderDto;
import uk.ac.sheffield.team28.team28.model.*;
import uk.ac.sheffield.team28.team28.repository.InstrumentRepository;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;
import uk.ac.sheffield.team28.team28.repository.OrderRepository;
import uk.ac.sheffield.team28.team28.service.*;

import java.time.LocalDate;
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
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderRepository orderRepository;


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        //Get logged in member
        Member member = memberService.findMember();
        model.addAttribute("memberType", member.getMemberType().toString());

        //If member is a committee member, get all instruments, and orders
        if (member.getMemberType() == MemberType.Committee || member.getMemberType() == MemberType.Director){

            List<Instrument> instruments = instrumentRepository.findAll();
            model.addAttribute("instruments", instruments);

            //Get all music
            List<Music> music = musicRepository.findAll();
            model.addAttribute("musics", music);

            //Get band types
            List<BandInPractice> bands = Arrays.asList(BandInPractice.values());
            model.addAttribute("bands", bands);

            //Get all orders
            List<Order> orders = orderRepository.findByItemTypeAndNotFulfilled(ItemType.Music);
            model.addAttribute("musicOrders", orders);
            model.addAttribute("orderService", orderService);

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
        musicService.saveMusic(dto);
        return "redirect:/dashboard";
    }

    @PostMapping("/orderMusic")
    public String orderMusic(@ModelAttribute("order") OrderDto dto){
        Member member = memberService.findMember();
        dto.setMember(member);
        Item item = itemService.findById(dto.getItemId());
        dto.setItem(item);
        System.out.println("Local date is: " + LocalDate.now());
        dto.setOrderDate(LocalDate.now());
        orderService.save(dto);
        return "redirect:/dashboard";
    }

}
