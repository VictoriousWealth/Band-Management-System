package uk.ac.sheffield.team28.team28.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.sheffield.team28.team28.controller.DirectorController;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;
import uk.ac.sheffield.team28.team28.repository.ChildMemberRepository;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.service.ChildMemberService;
import uk.ac.sheffield.team28.team28.service.MemberService;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectorControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ChildMemberService childMemberService;

    @Mock
    private ChildMemberRepository childMemberRepository;

    @InjectMocks
    private DirectorController directorController;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


}
