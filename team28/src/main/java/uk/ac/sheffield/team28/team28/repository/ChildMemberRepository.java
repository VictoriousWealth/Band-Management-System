package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.ac.sheffield.team28.team28.model.ChildMember;
import uk.ac.sheffield.team28.team28.model.Member;

import java.util.List;

public interface ChildMemberRepository extends JpaRepository<ChildMember, Long> {

    //Custom query to get child members given a parent id
    @Query("SELECT c FROM ChildMember c WHERE c.parent = :parent")
    List<ChildMember> findAllByParent(@Param("parent") Member parent);
}
