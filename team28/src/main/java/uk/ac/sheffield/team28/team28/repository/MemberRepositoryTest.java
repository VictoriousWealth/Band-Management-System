package uk.ac.sheffield.team28.team28.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import uk.ac.sheffield.team28.team28.model.BandInPractice;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.MemberType;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = Member.class)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testExistsByEmail() {
        // Arrange
        String email = "test@example.com";
        Member member = new Member();
        member.setEmail(email);
        memberRepository.save(member);

        // Act
        boolean exists = memberRepository.existsByEmail(email);

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByMemberType() {
        // Arrange
        MemberType memberType = MemberType.Adult;
        Member member1 = new Member();
        member1.setMemberType(memberType);
        Member member2 = new Member();
        member2.setMemberType(memberType);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        List<Member> members = memberRepository.findByMemberType(memberType);

        // Assert
        assertThat(members).hasSize(2);
        assertThat(members).allMatch(m -> m.getMemberType() == memberType);
    }

    @Test
    void testFindByBand() {
        // Arrange


        Member member1 = new Member();
        member1.setBand(BandInPractice.Training);
        Member member2 = new Member();
        member2.setBand(BandInPractice.Training);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        List<Member> members = memberRepository.findByBand(BandInPractice.Training);

        // Assert
        assertThat(members).hasSize(2);
        assertThat(members).allMatch(m -> m.getBand().equals(BandInPractice.Training));
    }

    @Test
    void testFindByEmail() {
        // Arrange
        String email = "testuser@example.com";
        Member member = new Member();
        member.setEmail(email);
        memberRepository.save(member);

        // Act
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        // Assert
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo(email);
    }


}
