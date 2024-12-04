package uk.ac.sheffield.team28.team28.service;

import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.model.Request;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.repository.MemberRepository;
import uk.ac.sheffield.team28.team28.repository.RequestRepository;

import java.util.List;

@Service
public class RequestService {
    private final MemberRepository memberRepository;
    RequestRepository requestRepository;

    RequestService(RequestRepository requestRepository, MemberRepository memberRepository) {
        this.requestRepository = requestRepository;
        this.memberRepository = memberRepository;
    }

    public boolean addRequest(Request request) {
        if (request == null) return false;
        if (request.getRequester() == null) return false;
        if (request.getDescription() == null) return false;
        if (request.getDescription().isBlank()) return false;

        requestRepository.save(request);
        return true;
    }

    public Request getRequestWithId(Long id) {
        return requestRepository.findById(id).orElse(null);
    }

    public List<Request> getAllRequestWhereRequesterIs(Member requester) {
        return requestRepository.findAllByRequester(requester);
    }

    public List<Request> getAllApprovedRequestWhereRequesterIs(Member requester) {
        return requestRepository.findAllByRequesterAndAccepted(requester, true);
    }

    public boolean approveRequest(Request request) {
        if (request == null) return false;
        if (request.getRequester() == null) return false;
        if (request.getDescription() == null) return false;
        if (request.getDescription().isBlank()) return false;
        request.setAccepted(true);
        requestRepository.save(request);
        return true;
    }

    public List<Request> getAllToBeApprovedRequests() {
        return requestRepository.findAllByAccepted(false);
    }

    public boolean deleteRequest(Request request) {
        if (request == null) return false;
        if (request.getRequester() == null) return false;
        requestRepository.delete(request);
        return true;
    }

}
