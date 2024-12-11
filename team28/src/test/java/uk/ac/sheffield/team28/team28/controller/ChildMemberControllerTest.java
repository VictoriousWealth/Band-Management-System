package uk.ac.sheffield.team28.team28.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import uk.ac.sheffield.team28.team28.Team28Application;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChildMemberController.class)
@ContextConfiguration(classes = Team28Application.class)
public class ChildMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChildMemberService childMemberService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MockHttpSession session;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnChildDashboard() throws Exception {
        Long childMemberId = 123L;

        mockMvc.perform(get("/dashboard/{childMemberId}", childMemberId))
                .andExpect(status().isOk())
                .andExpect(view().name("childDashboard"))
                .andExpect(model().attributeExists("childMemberId"))
                .andExpect(model().attribute("childMemberId", childMemberId));
    }

    @Test
    void shouldRedirectToAuthoriseWhenNotAuthorized() throws Exception {
        session.setAttribute("isAuthorised", null);

        mockMvc.perform(get("/allow-to-go-parent").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authorise"))
                .andExpect(request().sessionAttribute("referer", "/dashboard"));
    }

    @Test
    void shouldRedirectToChildDashboardWhenAuthorized() throws Exception {
        session.setAttribute("isAuthorised", true);

        mockMvc.perform(get("/allow-to-go-parent").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/child/dashboard/{childMemberId}"));
    }

    @Test
    void shouldRedirectToAuthoriseWhenSessionAttributeIsFalse() throws Exception {
        session.setAttribute("isAuthorised", false);
    }
}
