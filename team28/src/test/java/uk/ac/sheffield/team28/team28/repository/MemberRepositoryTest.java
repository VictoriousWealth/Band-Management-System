package uk.ac.sheffield.team28.team28.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.team28.team28.Team28Application;

import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = Team28Application.class)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;


    @Test
    void randomTest() {
        assertTrue(true);
    }

/*    @Test
    void testExistsByEmail() {
        // Arrange
        String email = "test@example.com";
        Member member = new Member();
        member.setEmail(email);
        memberRepository.save(member);

        boolean exists = memberRepository.existsByEmail(email);

        assert (exists);
    }*/

//    @Test
//    void testFindByMemberType() {
//        // Arrange
//        MemberType memberType = MemberType.Adult;
//        Member member1 = new Member();
//        member1.setMemberType(memberType);
//        Member member2 = new Member();
//        member2.setMemberType(memberType);
//        memberRepository.save(member1);
//        memberRepository.save(member2);
//
//        // Act
//        List<Member> members = memberRepository.findByMemberType(memberType);
//
//        // Assert
//        assert members.size()==2;
////        assert members.forEach(m -> m.getMemberType() == memberType);
//    }
//
//    @Test
//    void testFindByBand() {
//        // Arrange
//
//
//        Member member1 = new Member();
//        member1.setBand(BandInPractice.Training);
//        Member member2 = new Member();
//        member2.setBand(BandInPractice.Training);
//        memberRepository.save(member1);
//        memberRepository.save(member2);
//
//        // Act
//        List<Member> members = memberRepository.findByBand(BandInPractice.Training);
//
//        // Assert
//        assert members.size()==2;
////        assert members.allMatch(m -> m.getBand().equals(BandInPractice.Training));
//    }
//
//    @Test
//    void testFindByEmail() {
//        // Arrange
//        String email = "testuser@example.com";
//        Member member = new Member();
//        member.setEmail(email);
//        memberRepository.save(member);
//
//        // Act
//        Optional<Member> foundMember = memberRepository.findByEmail(email);
//
//        // Assert
//        assert foundMember.isPresent();
//        assert foundMember.get().getEmail().equals(email);
//    }


}