package uk.ac.sheffield.team28.team28.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.sheffield.team28.team28.model.Request;
import uk.ac.sheffield.team28.team28.model.Member;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsByDescription(String description);

    List<Request> findByDescription(String description);
    List<Request> findAllByRequester(Member requester);
    List<Request> findAllByRequesterAndAccepted(Member requester, boolean accepted);
    List<Request> findAllByAccepted(boolean accepted);

}
