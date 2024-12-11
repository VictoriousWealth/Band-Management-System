package uk.ac.sheffield.team28.team28.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.mockito.Mockito;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import uk.ac.sheffield.team28.team28.config.SecurityConfig;


import org.springframework.context.annotation.Import;
import uk.ac.sheffield.team28.team28.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.config.SecurityConfig;
import uk.ac.sheffield.team28.team28.controller.DirectorController;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.CustomUserDetailsService;
import uk.ac.sheffield.team28.team28.service.MemberService;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DirectorController.class)
@ContextConfiguration(classes = Team28Application.class)
@Import(SecurityConfig.class)
@WithMockUser(username = "director@test.com", password = "Director1", roles = "DIRECTOR")
class DirectorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Model model;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private ChildMemberService childMemberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private ChildMemberRepository childMemberRepository;

    @Autowired
    private HttpSession httpSession;


    @BeforeEach
    public void setup() {
        String encodedPassword = new BCryptPasswordEncoder().encode("Director1");
        User testUser = new User("director@test.com", encodedPassword, Collections.emptyList());
        when(customUserDetailsService.loadUserByUsername("director@test.com")).thenReturn(testUser);

        Member testMember = new Member();
        testMember.setEmail("director@test.com");
        testMember.setMemberType(MemberType.DIRECTOR);
        Mockito.when(memberService.findMember()).thenReturn(testMember);
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .param("username", "director@test.com")
                        .param("password", "Director1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }


    @Test
    public void shouldReturnDirectorHome() throws Exception {

        mockMvc.perform(get("/director")
                .requestAttr("model", model)
                        .sessionAttr("session", httpSession))
                .andExpect(view().name("directorhome"))
                .andDo(print());
    }

    @Test
    public void shouldNotRemoveOneAdultMemberThatDoesntExists() throws Exception {

        when(memberService.findMemberById(30L)).thenThrow(new Exception("We dont want to delete yet"));

        mockMvc.perform(post("/director/removeMember/{tesId}", 30L)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?error=true"));
    }

    @Test
    public void shouldRemoveOneAdultMemberWithNoLoans() throws Exception {
        long idNotUsed = 0;
        while (true) {
            if (memberRepository.findById(idNotUsed).isPresent()) {
                idNotUsed++;
            } else {
                break;
            }
        }
        when(memberService.findMemberById(idNotUsed))
                .thenReturn(new Member(
                        idNotUsed,
                        "toBeRemoved@email.com",
                        new BCryptPasswordEncoder().encode("toBeRemoved"),
                        null,
                        "toBeRemoved",
                        "toBeRemoved", "toBeRemoved"

                ));

        mockMvc.perform(post("/director/removeMember/{tesId}", idNotUsed)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?success=true"));
    }

    @Test
    public void shouldRemoveOneAdultMemberWithOneChildBothOfWhichWithNoLoans() throws Exception {
        long parentIdNotUsed = 0;
        while (true) {
            if (memberRepository.findById(parentIdNotUsed).isPresent()) {
                parentIdNotUsed++;
            } else {
                break;
            }
        }
        Member testParentMember = new Member(
                parentIdNotUsed,
                "toBeRemoved@email.com",
                new BCryptPasswordEncoder().encode("toBeRemoved"),
                null,
                "toBeRemoved",
                "toBeRemoved", "toBeRemoved"

        );

        when(memberService.findMemberById(parentIdNotUsed))
                .thenReturn(testParentMember);
        Long childIdNotUsed = 0L;
        while (true) {
            if (childMemberRepository.findById(childIdNotUsed).isPresent()) {
                childIdNotUsed++;
            } else {
                break;
            }
        }
        ChildMember testChildMember = new ChildMember(
                childIdNotUsed,
                "toBeRemoved",
                "toBeRemoved",
                testParentMember,
                LocalDate.of(LocalDate.now().getYear()-10, Month.AUGUST, 12)
        );
        when(childMemberService.getChildByParent(testParentMember)).thenReturn(List.of(testChildMember));
        when(childMemberService.doAnyChildrenHaveLoans(testParentMember)).thenReturn(false);
        when(memberService.doesMemberHaveLoans(testParentMember)).thenReturn(false);

        mockMvc.perform(post("/director/removeMember/{tesId}", parentIdNotUsed)
                .param("redirectTo", "/director"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?success=true"));
    }


}
