package uk.ac.sheffield.team28.team28.controller;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.service.MemberService;

@WebMvcTest(MemberController.class)
@ContextConfiguration(classes = Team28Application.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Mock
    private BindingResult bindingResult;


}
